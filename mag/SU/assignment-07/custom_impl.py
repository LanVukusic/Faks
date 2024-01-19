import torch


class Conv2d(torch.nn.Module):
    def __init__(self, in_channels, out_channels, kernel_size, stride, padding=1):
        super().__init__()
        self.in_channels = in_channels
        self.out_channels = out_channels
        self.kernel_size = kernel_size
        self.stride = stride

        self.weight = torch.nn.Parameter(
            torch.randn(out_channels, in_channels, kernel_size, kernel_size)
        )
        self.bias = torch.nn.Parameter(torch.ones(out_channels))

    def forward(self, x):
        batch_size, in_channels, h, w = x.shape
        out_channels, _, kh, kw = self.weight.shape

        x_unfolded = torch.nn.Unfold(kernel_size=(kh, kw), stride=self.stride)(x)
        out_unfolded = (
            x_unfolded.transpose(1, 2).matmul(self.weight.view(out_channels, -1).t())
            + self.bias
        ).transpose(1, 2)
        out = out_unfolded.view(
            batch_size,
            out_channels,
            (h - self.kernel_size) // self.stride + 1,
            (w - self.kernel_size) // self.stride + 1,
        )
        return out


class ReLU(torch.nn.Module):
    def __init__(self):
        super().__init__()

    def forward(self, x):
        return torch.max(torch.tensor(0.0), x)


class MaxPool2d(torch.nn.Module):
    def __init__(self, kernel_size, stride):
        super().__init__()
        self.kernel_size = kernel_size
        self.stride = stride

    def forward(self, x):
        batch_size, in_channels, h, w = x.size()

        unfold = torch.nn.Unfold(
            kernel_size=(self.kernel_size, self.kernel_size), stride=self.stride
        )
        x_unfolded = unfold(x)

        x_unfolded = x_unfolded.view(
            batch_size, in_channels, self.kernel_size * self.kernel_size, -1
        )

        max_values, _ = torch.max(x_unfolded, dim=2)

        out_height = (h - self.kernel_size) // self.stride + 1
        out_width = (w - self.kernel_size) // self.stride + 1
        output = max_values.view(batch_size, in_channels, out_height, out_width)

        return output
