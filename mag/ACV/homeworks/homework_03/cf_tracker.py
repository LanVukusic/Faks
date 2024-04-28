import numpy as np
import cv2

from ex2_utils import get_patch
from ex3_utils import create_cosine_window, create_gauss_peak


class Tracker:
    def __init__(self, params):
        self.parameters = params

    def initialize(self, image, region):
        raise NotImplementedError

    def track(self, image):
        raise NotImplementedError


class CFTracker(Tracker):

    def name(self):
        return "CFTrackerF"

    def initialize(self, image, region):
        # Region (Left, Top, Width, Height)

        if len(region) == 8:
            x_ = np.array(region[::2])
            y_ = np.array(region[1::2])
            region = [
                np.min(x_),
                np.min(y_),
                np.max(x_) - np.min(x_) + 1,
                np.max(y_) - np.min(y_) + 1,
            ]

        self.enlarge = 1.5
        self.sigma = 1.5
        self.lmbd = 0.00001
        self.alpha = 0.08

        self.size = (region[2] * self.enlarge, region[3] * self.enlarge)
        self.position = (region[0] + region[2] / 2, region[1] + region[3] / 2)
        image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

        self.G = create_gauss_peak(self.size, self.sigma)
        self.size = (self.G.shape[1], self.G.shape[0])
        patch, _ = get_patch(image, self.position, self.size)
        self.cos_wndw = create_cosine_window((patch.shape[1], patch.shape[0]))
        patch = patch * self.cos_wndw

        self.G_hat = np.fft.fft2(self.G)
        patch_hat = np.fft.fft2(patch)
        self.H = (
            self.G_hat
            * np.conj(patch_hat)
            / (patch_hat * np.conj(patch_hat) + self.lmbd)
        )

    def track(self, image):
        image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
        patch, _ = get_patch(image, self.position, self.size)
        patch = patch * self.cos_wndw
        patch_hat = np.fft.fft2(patch)

        CR = np.fft.ifft2(self.H * patch_hat)
        y, x = np.where(CR == np.amax(CR))
        y = y[0] - self.size[1] if y[0] > self.size[1] / 2 else y[0]
        x = x[0] - self.size[0] if x[0] > self.size[0] / 2 else x[0]

        self.position = (self.position[0] + x, self.position[1] + y)

        patch_t1, _ = get_patch(image, self.position, self.size)
        patch_t1 = patch_t1 * self.cos_wndw
        patch_t1_hat = np.fft.fft2(patch_t1)

        H_t1 = (
            self.G_hat
            * np.conj(patch_t1_hat)
            / (patch_t1_hat * np.conj(patch_t1_hat) + self.lmbd)
        )
        self.H = (1 - self.alpha) * self.H + self.alpha * H_t1
        return [
            self.position[0] - self.size[0] / (2 * self.enlarge),
            self.position[1] - self.size[1] / (2 * self.enlarge),
            self.size[0] / self.enlarge,
            self.size[1] / self.enlarge,
        ]


class CFParams:
    def __init__(self):
        self.sigma = 1.75
        self.lmbd = 0.00001
        self.alpha = 0.05
