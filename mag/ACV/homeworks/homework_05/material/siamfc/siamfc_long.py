from __future__ import absolute_import, division, print_function

import torch
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
import numpy as np
import time
import cv2
import sys
import os
from collections import namedtuple
from torch.optim.lr_scheduler import ExponentialLR
from torch.utils.data import DataLoader
from got10k.trackers import Tracker

from . import ops
from .backbones import AlexNetV1
from .heads import SiamFC
from .losses import BalancedLoss
from .datasets import Pair
from .transforms import SiamFCTransforms


# __all__ = ['TrackerSiamFCLong']

def cartesian_product(*arrays):
    la = len(arrays)
    dtype = np.result_type(*arrays)
    arr = np.empty([len(a) for a in arrays] + [la], dtype=dtype)
    for i, a in enumerate(np.ix_(*arrays)):
        arr[...,i] = a
    return arr.reshape(-1, la)


class Net(nn.Module):

    def __init__(self, backbone, head):
        super(Net, self).__init__()
        self.backbone = backbone
        self.head = head
    
    def forward(self, z, x):
        z = self.backbone(z)
        x = self.backbone(x)
        return self.head(z, x)


class TrackerSiamFCLong(Tracker):

    def __init__(self, net_path=None, **kwargs):
        super(TrackerSiamFCLong, self).__init__('SiamFC', True)
        self.cfg = self.parse_args(**kwargs)

        # setup GPU device if available
        self.cuda = torch.cuda.is_available()
        self.device = torch.device('cuda:0' if self.cuda else 'cpu')

        # setup model
        self.net = Net(
            backbone=AlexNetV1(),
            head=SiamFC(self.cfg.out_scale))
        ops.init_weights(self.net)
        
        # load checkpoint if provided
        if net_path is not None:
            self.net.load_state_dict(torch.load(
                net_path, map_location=lambda storage, loc: storage))
        self.net = self.net.to(self.device)

        # setup criterion
        self.criterion = BalancedLoss()

        # setup optimizer
        self.optimizer = optim.SGD(
            self.net.parameters(),
            lr=self.cfg.initial_lr,
            weight_decay=self.cfg.weight_decay,
            momentum=self.cfg.momentum)
        
        # setup lr scheduler
        gamma = np.power(
            self.cfg.ultimate_lr / self.cfg.initial_lr,
            1.0 / self.cfg.epoch_num)
        self.lr_scheduler = ExponentialLR(self.optimizer, gamma)

    def parse_args(self, **kwargs):
        # default parameters
        cfg = {
            # basic parameters
            'out_scale': 0.001,
            'exemplar_sz': 127,
            'instance_sz': 255,
            'context': 0.5,
            # inference parameters
            'scale_num': 3,
            'scale_step': 1.0375,
            'scale_lr': 0.59,
            'scale_penalty': 0.9745,
            'window_influence': 0.176,
            'response_sz': 17,
            'response_up': 16,
            'total_stride': 8,
            # train parameters
            'epoch_num': 50,
            'batch_size': 8,
            'num_workers': 16,  # 32
            'initial_lr': 1e-2,
            'ultimate_lr': 1e-5,
            'weight_decay': 5e-4,
            'momentum': 0.9,
            'r_pos': 16,
            'r_neg': 0}
        
        for key, val in kwargs.items():
            if key in cfg:
                cfg.update({key: val})
        return namedtuple('Config', cfg.keys())(**cfg)
    
    @torch.no_grad()
    def init(self, img, box):
        # set to evaluation mode
        self.net.eval()

        # convert box to 0-indexed and center based [y, x, h, w]
        box = np.array([
            box[1] - 1 + (box[3] - 1) / 2,
            box[0] - 1 + (box[2] - 1) / 2,
            box[3], box[2]], dtype=np.float32)
        self.center, self.target_sz = box[:2], box[2:]

        # create hanning window
        self.upscale_sz = self.cfg.response_up * self.cfg.response_sz
        self.hann_window = np.outer(
            np.hanning(self.upscale_sz),
            np.hanning(self.upscale_sz))
        self.hann_window /= self.hann_window.sum() # normalize hann window

        # search scale factors
        self.scale_factors = self.cfg.scale_step ** np.linspace( # linspace scales from -something to +something to create an array of scales
            -(self.cfg.scale_num // 2),
            self.cfg.scale_num // 2, self.cfg.scale_num)

        # exemplar and search sizes
        context = self.cfg.context * np.sum(self.target_sz)
        self.z_sz = np.sqrt(np.prod(self.target_sz + context))
        self.x_sz = self.z_sz * \
            self.cfg.instance_sz / self.cfg.exemplar_sz
        
        # exemplar image
        self.avg_color = np.mean(img, axis=(0, 1))
        z = ops.crop_and_resize( # Get the input image and output a safe/padded patch 
            img, self.center, self.z_sz,
            out_size=self.cfg.exemplar_sz,
            border_value=self.avg_color)
        
        # exemplar features
        z = torch.from_numpy(z).to(self.device).permute(2, 0, 1).unsqueeze(0).float() # create a torch tensor from numpy array, with switched channel dims
        # print("init Z", z.shape)
        self.kernel = self.net.backbone(z) # create an image embedding
        # print("init Z out", self.kernel.shape)

        # CUSTOM THINGS
        self.acc_buff_size = 20
        self.accuracy_buffer = [] # track first 10 frames of the sequence to determine good tresholds
        self.treshold = 2 # 2 is a very low trashold. will be updated at runtime
        self.target_visible = [1] # each frame should report the target visibility for easier debugging
        self.random_search_heads = 6
        self.tau = 0.01
    
    @torch.no_grad()
    def update(self, img):
        # this update is actually "forward" 
        # set to evaluation mode
        self.net.eval()

        if self.target_visible[-1] == 1:
            # target was okay in previous step, continue with normal tracking
            # search images
            # print("OK", self.center, self.x_sz, self.cfg.instance_sz)
            # print(self.center)
            x = [
                    ops.crop_and_resize(
                        img, self.center, self.x_sz * scale_factor,
                        out_size=self.cfg.instance_sz,
                        border_value=self.avg_color)
                for scale_factor in self.scale_factors
                ]
            x = np.stack(x, axis=0)
            # to torch format
            x = torch.from_numpy(x).to(self.device).permute(0, 3, 1, 2).float()
            # embed image
            x = self.net.backbone(x) # generate image embeddings from the input image
        else:
            # print("-")
            # initialize target finding
            xs = np.linspace(0, img.shape[1], self.random_search_heads)
            ys = np.linspace(0, img.shape[0], self.random_search_heads)
            cart_prod = cartesian_product(xs,ys)
            
            pos = cart_prod

            out = []
            for p in pos:
                p = np.array(p)
                x = ops.crop_and_resize(img, p, self.x_sz, out_size=self.cfg.instance_sz, border_value=self.avg_color)
                out.append(x)

            x = np.stack(out, axis=0)

            # get the highest vaalued box
            x = torch.from_numpy(x).to(self.device).permute(0, 3, 1, 2).float()
            # embed image
            x = self.net.backbone(x) # generate image embeddings from the input image
            # generate response from initial ground truth embedding and new image
            responses = self.net.head(self.kernel, x) # generate peak response by the NN
            responses = responses.squeeze(1).cpu().numpy() # return numpy array from torch tensor. Squeeze channel dim
            # upsample responses and penalize scale changes
            responses = np.stack([cv2.resize(
                u, (self.upscale_sz, self.upscale_sz),
                interpolation=cv2.INTER_CUBIC)
                for u in responses])
            responses[:self.cfg.scale_num // 2] *= self.cfg.scale_penalty
            responses[self.cfg.scale_num // 2 + 1:] *= self.cfg.scale_penalty
            # TBH, no idea why scale changes should be penalized but i guess this is how it should be done
            # peak location
            peak_values = np.amax(responses, axis=(1, 2))
            max_resp = max(0, peak_values.max())
            max_ind = np.argmax(peak_values)
            self.center = np.array(pos[max_ind], dtype=np.float16)

            if(max_resp < self.treshold):
                self.target_visible.append(0)
            else:
                self.target_visible.append(1)

            box = np.array([0,0,0,0])
            return box, max_resp, self.target_visible[-1], self.treshold
            
        


        # generate response from initial ground truth embedding and new image
        responses = self.net.head(self.kernel, x) # generate peak response by the NN
        responses = responses.squeeze(1).cpu().numpy() # return numpy array from torch tensor. Squeeze channel dim

        # upsample responses and penalize scale changes
        responses = np.stack([cv2.resize(
            u, (self.upscale_sz, self.upscale_sz),
            interpolation=cv2.INTER_CUBIC)
            for u in responses])
        responses[:self.cfg.scale_num // 2] *= self.cfg.scale_penalty
        responses[self.cfg.scale_num // 2 + 1:] *= self.cfg.scale_penalty
        # TBH, no idea why scale changes should be penalized but i guess this is how it should be done


        # peak location
        peak_value = np.argmax(np.amax(responses, axis=(1, 2)))
        response = responses[peak_value]

        # peak scale
        max_resp = max(0, response.max())
        if(max_resp < self.treshold):
            # if self.target_visible[-1] == 1:
            #     print("target lost", max_resp, self.treshold)
            self.target_visible.append(0)
        else:
            # if self.target_visible[-1] == 0:
            #     print("target found", max_resp, self.treshold)
            self.target_visible.append(1)
            if(len(self.accuracy_buffer)< self.acc_buff_size):
                # expand accuracy buffer
                self.accuracy_buffer.append(max_resp) # add confidence sample
                mean = (sum(self.accuracy_buffer)/len(self.accuracy_buffer))
                self.treshold = (mean / 1.1) - 0.2 # update treshold for target visibility
            else:
                # self.accuracy_buffer.append(max_resp) # add confidence sample
                # self.accuracy_buffer = self.accuracy_buffer[1:] # rolling buffer
                self.treshold = 3.8
                # self.treshold = (1-self.tau) * self.treshold + (self.tau) * (max_resp / 1.3)


        response_normd = response
        response_normd -= response_normd.min()
        response_normd /= response_normd.sum() + 1e-16
        response_normd = (1 - self.cfg.window_influence) * response_normd + \
            self.cfg.window_influence * self.hann_window
        loc = np.unravel_index(response_normd.argmax(), response_normd.shape)

        # locate target center
        disp_in_response = np.array(loc) - (self.upscale_sz - 1) / 2
        disp_in_instance = disp_in_response * \
            self.cfg.total_stride / self.cfg.response_up
        disp_in_image = disp_in_instance * self.x_sz * \
            self.scale_factors[peak_value] / self.cfg.instance_sz
        self.center += disp_in_image

        # update target size
        scale =  (1 - self.cfg.scale_lr) * 1.0 + \
            self.cfg.scale_lr * self.scale_factors[peak_value]
        self.target_sz *= scale
        self.z_sz *= scale
        self.x_sz *= scale

        # return 1-indexed and left-top based bounding box
        box = np.array([
            self.center[1] + 1 - (self.target_sz[1] - 1) / 2,
            self.center[0] + 1 - (self.target_sz[0] - 1) / 2,
            self.target_sz[1], self.target_sz[0]])

        return box, max_resp, self.target_visible[-1], self.treshold
    
    def train_step(self, batch, backward=True):
        # set network mode
        self.net.train(backward)

        # parse batch data
        z = batch[0].to(self.device, non_blocking=self.cuda)
        x = batch[1].to(self.device, non_blocking=self.cuda)

        with torch.set_grad_enabled(backward):
            # inference
            responses = self.net(z, x)

            # calculate loss
            labels = self._create_labels(responses.size())
            loss = self.criterion(responses, labels)
            
            if backward:
                # back propagation
                self.optimizer.zero_grad()
                loss.backward()
                self.optimizer.step()
        
        return loss.item()

    @torch.enable_grad()
    def train_over(self, seqs, val_seqs=None,
                   save_dir='pretrained'):
        # set to train mode
        self.net.train()

        # create save_dir folder
        if not os.path.exists(save_dir):
            os.makedirs(save_dir)

        # setup dataset
        transforms = SiamFCTransforms(
            exemplar_sz=self.cfg.exemplar_sz,
            instance_sz=self.cfg.instance_sz,
            context=self.cfg.context)
        dataset = Pair(
            seqs=seqs,
            transforms=transforms)
        
        # setup dataloader
        dataloader = DataLoader(
            dataset,
            batch_size=self.cfg.batch_size,
            shuffle=True,
            num_workers=self.cfg.num_workers,
            pin_memory=self.cuda,
            drop_last=True)
        
        # loop over epochs
        for epoch in range(self.cfg.epoch_num):
            # update lr at each epoch
            self.lr_scheduler.step(epoch=epoch)

            # loop over dataloader
            for it, batch in enumerate(dataloader):
                loss = self.train_step(batch, backward=True)
                print('Epoch: {} [{}/{}] Loss: {:.5f}'.format(
                    epoch + 1, it + 1, len(dataloader), loss))
                sys.stdout.flush()
            
            # save checkpoint
            if not os.path.exists(save_dir):
                os.makedirs(save_dir)
            net_path = os.path.join(
                save_dir, 'siamfc_alexnet_e%d.pth' % (epoch + 1))
            torch.save(self.net.state_dict(), net_path)
    
    def _create_labels(self, size):
        # skip if same sized labels already created
        if hasattr(self, 'labels') and self.labels.size() == size:
            return self.labels

        def logistic_labels(x, y, r_pos, r_neg):
            dist = np.abs(x) + np.abs(y)  # block distance
            labels = np.where(dist <= r_pos,
                              np.ones_like(x),
                              np.where(dist < r_neg,
                                       np.ones_like(x) * 0.5,
                                       np.zeros_like(x)))
            return labels

        # distances along x- and y-axis
        n, c, h, w = size
        x = np.arange(w) - (w - 1) / 2
        y = np.arange(h) - (h - 1) / 2
        x, y = np.meshgrid(x, y)

        # create logistic labels
        r_pos = self.cfg.r_pos / self.cfg.total_stride
        r_neg = self.cfg.r_neg / self.cfg.total_stride
        labels = logistic_labels(x, y, r_pos, r_neg)

        # repeat to size
        labels = labels.reshape((1, 1, h, w))
        labels = np.tile(labels, (n, c, 1, 1))

        # convert to tensors
        self.labels = torch.from_numpy(labels).to(self.device).float()
        
        return self.labels
