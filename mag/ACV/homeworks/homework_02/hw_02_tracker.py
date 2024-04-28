import numpy as np
from ex2_utils import (
    Tracker,
    get_patch,
    create_epanechnik_kernel,
    extract_histogram,
    backproject_histogram,
    generate_responses_1,
)


BINS = 6
ENLARGE = 5
EPSILON = 1e-1
ERR_EPSILON = 0.5
FORGETTING_K = 0.005


class MeanshiftTracker(Tracker):
    def __init__(self):
        self.frames = 0

        return

    def initialize(self, image, region):

        if len(region) == 8:
            x_ = np.array(region[::2])
            y_ = np.array(region[1::2])
            region = [
                np.min(x_),
                np.min(y_),
                np.max(x_) - np.min(x_) + 1,
                np.max(y_) - np.min(y_) + 1,
            ]

        # self.window = masx(region[2], region[3]) * ENLARGE
        left = max(region[0], 0)
        top = max(region[1], 0)

        right = min(region[0] + region[2], image.shape[1] - 1)
        bottom = min(region[1] + region[3], image.shape[0] - 1)

        self.template = image[int(top) : int(bottom), int(left) : int(right)]
        self.position = np.array((region[0] + region[2] / 2, region[1] + region[3] / 2))
        self.size = (region[2], region[3])

        self.kernel = create_epanechnik_kernel(region[2], region[3], 4)
        self.tempalte = extract_histogram(self.template, BINS)

    def track(self, image: np.ndarray):
        self.frames += 1

        # MEANSHIFT PART
        shift = np.ones((2)) * np.inf
        h, w = self.kernel.shape
        xi, yi = np.meshgrid(
            np.arange(-(w // 2), w // 2 + 1), np.arange(-(h // 2), h // 2 + 1)
        )

        i = 0
        while np.max(np.abs(shift)) > ERR_EPSILON:
            if i > 1000:
                print("ERRORED", np.max(np.abs(shift)), i)
            # get image patch
            patch, _ = get_patch(image, self.position, [w, h])
            # from the slides
            # https://ucilnica.fri.uni-lj.si/pluginfile.php/28213/mod_resource/content/17/ACVM_MSTracking.pdf - page 50
            p = extract_histogram(patch, BINS)
            v = np.sqrt(self.tempalte / (p + EPSILON))
            wi = backproject_histogram(patch, v, BINS)

            div = np.sum(wi)
            shift_x = np.sum(xi * wi) / div
            shift_y = np.sum(yi * wi) / div
            shift = np.array([shift_x, shift_y])
            self.position += shift

            i += 1

        # DONE TRACKING, UPDATE TEMPLATE HIST
        patch, _ = get_patch(image, self.position, self.size)
        new_template = extract_histogram(patch, BINS)
        # update template with forgetting factor - FORGETTING_K
        self.tempalte = (1 - FORGETTING_K) * self.tempalte + FORGETTING_K * new_template

        return [
            self.position[0] - self.size[0] / 2,
            self.position[1] - self.size[1] / 2,
            self.size[0],
            self.size[1],
        ]
