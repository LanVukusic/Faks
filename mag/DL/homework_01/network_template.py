import pandas as pd
import numpy as np
import pickle
import matplotlib.pyplot as plt


class Network(object):
    def __init__(self, sizes, optimizer="sgd", lr_decay=0, weight_decay=0):
        # weights connect two layers, each neuron in layer L is connected to every neuron in layer L+1,
        # the weights for that layer of dimensions size(L+1) X size(L)
        # the bias in each layer L is connected to each neuron in L+1, the number of weights necessary for the bias
        # in layer L is therefore size(L+1).
        # The weights are initialized with a He initializer: https://arxiv.org/pdf/1502.01852v1.pdf
        self.num_layers = len(sizes)
        self.learning_rate_decay = lr_decay
        self.l2_alpha = weight_decay
        self.weights = [
            ((2 / sizes[i - 1]) ** 0.5) * np.random.randn(sizes[i], sizes[i - 1])
            for i in range(1, len(sizes))
        ]
        self.biases = [np.zeros((x, 1)) for x in sizes[1:]]
        self.optimizer = optimizer
        if self.optimizer == "adam":
            # Implement the buffers necessary for the Adam optimizer.
            self.m_w = [np.zeros_like(w) for w in self.weights]
            self.v_w = [np.zeros_like(w) for w in self.weights]
            self.m_b = [np.zeros_like(b) for b in self.biases]
            self.v_b = [np.zeros_like(b) for b in self.biases]
            self.t = 0  # Initialize time step

        # initializ metric loggin
        self.losses_m = []
        self.lrs_m = []

    def plot(self):
        def moving_average(x, w):
            return np.convolve(x, np.ones(w), "valid") / w

        plt.plot(moving_average(self.losses_m, 10), label="loss")
        plt.plot(moving_average(self.losses_m, 100), label="loss smooth 100")
        plt.legend()
        # plt.plot(self.lrs_m, label="learning rate decay")
        plt.yscale("log")
        plt.show()

    def train(
        self,
        training_data,
        training_class,
        val_data,
        val_class,
        epochs,
        mini_batch_size,
        eta,
    ):
        # training data - numpy array of dimensions [n0 x m], where m is the number of examples in the data and
        # n0 is the number of input attributes
        # training_class - numpy array of dimensions [c x m], where c is the number of classes
        # epochs - number of passes over the dataset
        # mini_batch_size - number of examples the network uses to compute the gradient estimation

        iteration_index = 0
        eta_current = eta

        n = training_data.shape[1]
        for epoch in range(epochs):
            print("Epoch" + str(epoch))
            loss_avg = 0.0
            mini_batches = [
                (
                    training_data[:, k : k + mini_batch_size],
                    training_class[:, k : k + mini_batch_size],
                )
                for k in range(0, n, mini_batch_size)
            ]
            for mini_batch in mini_batches:
                output, Zs, As = self.forward_pass(mini_batch[0])
                # print("out", output)
                # print("zs", Zs)
                gw, gb = self.backward_pass(output, mini_batch[1], Zs, As)

                self.update_network(gw, gb, eta_current)

                # Implement the learning rate schedule for Task 5
                eta_current = lr_decay(iteration_index, eta, self.learning_rate_decay)

                loss = cross_entropy(mini_batch[1], output)
                loss_avg += loss

                iteration_index += 1

                # logging
                self.losses_m.append(loss)
                self.lrs_m.append(eta_current)

            print("Epoch {} complete".format(epoch))
            print("Current LR: {}".format(eta_current))
            print("Loss:" + str(loss_avg / len(mini_batches)))
            # self.plot()
            if epoch % 10 == 0:
                self.eval_network(val_data, val_class)
            print()

    def eval_network(self, validation_data, validation_class):
        # validation data - numpy array of dimensions [n0 x m], where m is the number of examples in the data and
        # n0 is the number of input attributes
        # validation_class - numpy array of dimensions [c x m], where c is the number of classes
        n = validation_data.shape[1]
        loss_avg = 0.0
        tp = 0.0
        for i in range(validation_data.shape[1]):
            example = np.expand_dims(validation_data[:, i], -1)
            example_class = np.expand_dims(validation_class[:, i], -1)
            example_class_num = np.argmax(validation_class[:, i], axis=0)
            output, Zs, activations = self.forward_pass(example)
            output_num = np.argmax(output, axis=0)[0]
            tp += int(example_class_num == output_num)

            loss = cross_entropy(example_class, output)
            loss_avg += loss
        print("Validation Loss:" + str(loss_avg / n))
        print("Classification accuracy: " + str(tp / n))

    def l2(self):
        l2_regularization = 0.5 * self.l2_alpha * np.sum(self.weights[1:] ** 2)
        return l2_regularization

    def update_network(self, gw, gb, eta):
        if self.optimizer == "sgd":
            for i in range(len(self.weights)):
                self.weights[i] -= eta * gw[i]
                self.biases[i] -= eta * gb[i]

        elif self.optimizer == "adam":
            beta1 = 0.9
            beta2 = 0.999
            epsilon = 1e-8
            self.t += 1  # Increment time step

            for i in range(len(self.weights)):
                self.m_w[i] = beta1 * self.m_w[i] + (1 - beta1) * gw[i]
                self.m_b[i] = beta1 * self.m_b[i] + (1 - beta1) * gb[i]
                self.v_w[i] = beta2 * self.v_w[i] + (1 - beta2) * np.square(gw[i])
                self.v_b[i] = beta2 * self.v_b[i] + (1 - beta2) * np.square(gb[i])

                m_w_hat = self.m_w[i] / (1 - np.power(beta1, self.t))
                m_b_hat = self.m_b[i] / (1 - np.power(beta1, self.t))
                v_w_hat = self.v_w[i] / (1 - np.power(beta2, self.t))
                v_b_hat = self.v_b[i] / (1 - np.power(beta2, self.t))

                # update bias
                self.biases[i] -= eta * m_b_hat / (np.sqrt(v_b_hat) + epsilon)

                # update weights
                if self.l2_alpha > 0:
                    self.weights[i] -= eta * (
                        (m_w_hat / (np.sqrt(v_w_hat) + epsilon))
                        + self.weights[i] * self.l2_alpha
                    )
                else:
                    # no weight decay
                    self.weights[i] -= eta * m_w_hat / (np.sqrt(v_w_hat) + epsilon)
        else:
            raise ValueError("Unknown optimizer:" + self.optimizer)

    def forward_pass(self, inputs):
        # input - numpy array of dimensions [n0 x m], where m is the number of examples in the mini batch and
        # n0 is the number of input attributes
        Zs = []
        As = [inputs]
        out = inputs

        for bias, weight in zip(self.biases[:-1], self.weights[:-1]):
            computed_z = (weight @ out) + bias
            Zs.append(computed_z)

            out = sigmoid(computed_z)
            As.append(out)

        # last softmax layer
        computed_z = (self.weights[-1] @ out) + self.biases[-1]
        Zs.append(computed_z)
        out = softmax(computed_z)
        As.append(out)

        return out, Zs, As

    def backward_pass(self, output, target, Zs, activations):
        nabla_b = [np.zeros_like(i) for i in self.biases]  # wrong shape
        nabla_w = [np.zeros_like(i) for i in self.weights]  # wrong shape

        batch_size = output.shape[1]

        delta = softmax_dLdZ(output, target)

        nabla_b[-1] = np.mean(delta, axis=1, keepdims=True)
        nabla_w[-1] = delta @ activations[-2].T / batch_size

        for i in range(2, len(self.weights)):
            z = Zs[-i]
            sp = sigmoid_prime(z)
            delta = self.weights[-i + 1].T @ delta * sp
            nabla_b[-i] = np.mean(delta, axis=1, keepdims=True)
            nabla_w[-i] = delta @ activations[-i - 1].T / batch_size

        return nabla_w, nabla_b


def lr_decay(step: int, base_lr: float, decay: float):
    return base_lr * np.exp(-step * decay)


def softmax(Z):
    expZ = np.exp(Z - np.max(Z))
    return expZ / expZ.sum(axis=0, keepdims=True)


def softmax_dLdZ(output, target):
    # partial derivative of the cross entropy loss w.r.t Z at the last layer
    return output - target


def cross_entropy(y_true, y_pred, epsilon=1e-12):
    targets = y_true.transpose()
    predictions = y_pred.transpose()
    predictions = np.clip(predictions, epsilon, 1.0 - epsilon)
    N = predictions.shape[0]
    ce = -np.sum(targets * np.log(predictions + 1e-9)) / N
    return ce


def sigmoid(z):
    return 1.0 / (1.0 + np.exp(-z))


def sigmoid_prime(z):
    return sigmoid(z) * (1 - sigmoid(z))


def unpickle(file):
    with open(file, "rb") as fo:
        return pickle.load(fo, encoding="bytes")


def load_data_cifar(train_file, test_file):
    train_dict = unpickle(train_file)
    test_dict = unpickle(test_file)
    train_data = np.array(train_dict["data"]) / 255.0
    train_class = np.array(train_dict["labels"])
    train_class_one_hot = np.zeros((train_data.shape[0], 10))
    train_class_one_hot[np.arange(train_class.shape[0]), train_class] = 1.0
    test_data = np.array(test_dict["data"]) / 255.0
    test_class = np.array(test_dict["labels"])
    test_class_one_hot = np.zeros((test_class.shape[0], 10))
    test_class_one_hot[np.arange(test_class.shape[0]), test_class] = 1.0
    return (
        train_data.transpose(),
        train_class_one_hot.transpose(),
        test_data.transpose(),
        test_class_one_hot.transpose(),
    )


if __name__ == "__main__":
    train_file = "./data/train_data.pckl"
    test_file = "./data/test_data.pckl"
    train_data, train_class, test_data, test_class = load_data_cifar(
        train_file, test_file
    )
    val_pct = 0.1
    val_size = int(train_data.shape[1] * val_pct)
    val_data = train_data[..., :val_size]
    val_class = train_class[..., :val_size]
    train_data = train_data[..., val_size:]
    train_class = train_class[..., val_size:]
    # The Network takes as input a list of the numbers of neurons at each layer. The first layer has to match the
    # number of input attributes from the data, and the last layer has to match the number of output classes
    # The initial settings are not even close to the optimal network architecture, try increasing the number of layers
    # and neurons and see what happens.
    net = Network([train_data.shape[0], 100, 100, 10], optimizer="sgd")
    net.train(train_data, train_class, val_data, val_class, 20, 64, 0.01)
    net.eval_network(test_data, test_class)
