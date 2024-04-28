import matplotlib.pyplot as plt

x_labels = ["640x480", "800x600", "1600x900", "1920x1080", "3840x2160"]
data_cpu = [362782, 566286, 1696267, 2440723, 9751417]
data_gpu = [2136, 2441, 4590, 6082, 21394]

plot = plt.plot(x_labels, data_cpu, label="CPU")
plot = plt.plot(x_labels, data_gpu, label="GPU")
plt.xlabel("resolution")
plt.yscale("log")
plt.show()
