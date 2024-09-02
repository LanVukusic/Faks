import torch
import torch.nn as nn

INPUT_SIZE = 250

class  PointLiteNet(nn.Module):
    def __init__(self, points_num:int, point_dims:int):
        super(PointLiteNet, self).__init__()
        self.num_points = points_num

        # path point encoder
        self.point_enc = nn.Sequential(
            nn.Flatten(),
            nn.Linear(points_num*point_dims, 128),
            nn.ReLU(),
            nn.Linear(128, 256),
            nn.ReLU(),
            nn.Linear(256, 256),
            nn.ReLU(),
        )

        # decoder
        self.merge = nn.Sequential(
            nn.Linear(256, 128),
            nn.ReLU(),
            nn.Linear(128, points_num*point_dims),
        )


    def forward(self, points):
        point = self.point_enc(points)
        out = self.merge(point)
        return out.reshape((-1, 2, self.num_points))


class  PointDefussionNet(nn.Module):
    def __init__(self, points_num:int, point_dims:int):
        super(PointDefussionNet, self).__init__()
        self.num_points = points_num


        # target image encoder
        # self.target_enc = nn.Sequential(
        #     nn.Conv2d(1, 16, 3, 1, 1),
        #     nn.ReLU(),
        #     nn.Conv2d(16, 32, 3, 1, 1),
        #     nn.ReLU(),
        #     nn.Conv2d(32, 16, 3, 1, 1),
        #     nn.ReLU(),
        # )
        
        # current image encoder
        self.curr_enc = nn.Sequential(
            nn.Conv2d(1, 16, 3, 2, 1), # 16x125x125
            nn.ReLU(), 
            nn.Conv2d(16, 32, 3, 2, 1), # 32x
            nn.ReLU(),
            nn.Conv2d(32, 16, 3, 1, 1), # 32x32x16
            nn.ReLU(),
            nn.Flatten(),
            nn.Linear(63*63*16, 256)
        )

        # path point encoder
        self.point_enc = nn.Sequential(
            nn.Flatten(),
            nn.Linear(points_num*point_dims, 128),
            nn.ReLU(),
            nn.Linear(128, 256),
            nn.ReLU(),
            nn.Linear(256, 256),
            nn.ReLU(),
        )

        # decoder
        self.merge = nn.Sequential(
            nn.Linear(3*256, 128),
            nn.ReLU(),
            nn.Linear(128, points_num*point_dims),
    
        )


    def forward(self, target, current, points):
        target = self.curr_enc(target)
        current = self.curr_enc(current)
        point = self.point_enc(points)

        c = torch.concat((target, current, point), dim=1)
        out = self.merge(c)
        return out.reshape((-1, 2, self.num_points))


