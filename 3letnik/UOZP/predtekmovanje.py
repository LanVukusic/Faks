import numpy as np
from datetime import datetime, timedelta
import tensorflow as tf
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

# defining global variables
learning_rate = 0.004
training_epochs = 400
num_coeffs = 20

# tensorboard
log_dir = (
    "logs/fit/"
    + datetime.now().strftime("%Y%m%d-%H%M%S")
    + "P:{}_{}_{}".format(learning_rate, training_epochs, num_coeffs)
)
print(tf.__version__)

mean = 0
std = 0


class DateManipulator:
    def __init__(self):
        pass

    # returns datetime object from string in format 2012-01-13 12:27:04.000
    def toDate(self, dateString):
        return datetime.strptime(dateString, "%Y-%m-%d %H:%M:%S.%f")

    # returns date as a string
    def toString(self, date):
        return date.strftime("%Y-%m-%d %H:%M:%S.%f")

    # returns hour as int from date
    def getHour(self, dateString):
        return self.toDate(dateString).hour

    # returns day of the week [0-6]
    def getDayOfWeek(self, dateString):
        return self.toDate(dateString).weekday()

    # returns abs difference between 2 dates in seconds
    def diff(self, dateString1, dateString2):
        return abs(
            (self.toDate(dateString1) - self.toDate(dateString2)).total_seconds()
        )

    # adds seconds to a date
    def addSeconds(self, dateString, seconds):
        return self.toDate(dateString) + timedelta(seconds=seconds)


# create a function that reads tab seperated values and returns a list of lists
def parse_file(file_name):
    with open(file_name) as f:
        content = f.read()
        lines = content.split("\n")

        parsed_lines = []
        for line in lines:
            if len(line) == 0:
                continue
            words = line.split("\t")
            parsed_lines.append(words)

        return parsed_lines


def categorical_buckets(data):
    # num_buckets = len(set(data))
    mappings = list(set(data))
    return np.array([mappings.index(x) for x in data])


def get_is_weekend(date):
    dm = DateManipulator()
    if dm.getDayOfWeek(date) >= 5:
        return 1
    else:
        return 0


def is_at_nigh(date):
    dm = DateManipulator()
    t = dm.getHour(date)
    if (t >= 20 and t <= 23) or (t >= 0 and t <= 7):
        return 1
    else:
        return 0


def is_rush_hour(date):
    dm = DateManipulator()
    hour = dm.getHour(date)
    if (hour >= 7 and hour <= 9) or (hour >= 15 and hour <= 17):
        return 1
    else:
        return 0


def is_holiday(date):
    dm = DateManipulator()
    if dm.getDayOfWeek(date) == 5 or dm.getDayOfWeek(date) == 6:
        return 1
    else:
        return 0


# get features from dataset
def getFeatures(data, isTrain):
    dataset = []
    dm = DateManipulator()

    j = len(data)

    for sample in data:
        hour = dm.getHour(sample[0])
        day_of_week = dm.getDayOfWeek(sample[0])
        is_night = is_at_nigh(sample[0])
        is_weekend = get_is_weekend(sample[0])
        is_rush = is_rush_hour(sample[0])

        dataset.append([is_night, is_weekend, hour, is_rush, day_of_week])
        if isTrain:
            duration = dm.diff(sample[0], sample[1])
            dataset[-1].append(duration)
        # print(dataset[-1])

    return np.array(dataset)


# predefining the model
def model(x, y):
    # this predicts the y based on the given weight
    temp = []
    for i in range(num_coeffs):
        temp.append(tf.add(w[i], tf.pow(x, i)))
    prediction = tf.add(tf.reduce_sum(temp), b)
    # this is the cost function
    errors = tf.square(y - prediction)
    return [prediction, errors]


# split dataset into train and test
def split_dataset(dataset, y_data, ratio=0.85):
    train_size = int(len(dataset) * ratio)
    train_set = dataset[:train_size]
    test_set = dataset[train_size:]
    y_train = y_data[:train_size]
    y_test = y_data[train_size:]
    return train_set, y_train, test_set, y_test


# standardize dataset
def standardize(dataset):
    mean = np.mean(dataset, axis=0)
    std = np.std(dataset, axis=0)

    return (dataset - mean) / std


def destandaize(dataset, mean, std):
    return (dataset * std) + mean


def main():
    # d1 = "2012-01-13 12:27:04.000"
    # d2 = "2012-01-13 13:00:33.000"

    # dm = DateManipulator()
    # print(dm.toString(dm.addSeconds(d1, dm.diff(d1, d2))))

    train_data_parsed = np.vstack(parse_file("train_pred.csv.gz")[1:])
    train_data = np.vstack(
        [
            np.array(train_data_parsed[:, 6]),  # departure  0
            np.array(train_data_parsed[:, 8]),  # arrival  1
            np.array(train_data_parsed[:, 1]),  # driver id  2 BAD COLUMN
            np.array(train_data_parsed[:, 0]),  # registration  3
        ]
    ).T

    train_data_features = getFeatures(train_data, True)  # get features from train data

    DATASET_LEN = 5
    x = np.array([x[0:DATASET_LEN] for x in train_data_features])
    y = np.array([x[DATASET_LEN] for x in train_data_features])

    x_train, y_train, x_test, y_test = split_dataset(x, y, ratio=0.7)

    print(x_train, y_train)
    # exit(0)

    # defining the model using tensorflow 2
    model = tf.keras.Sequential()
    model.add(tf.keras.layers.Dense(num_coeffs, input_shape=(DATASET_LEN,)))
    model.add(tf.keras.layers.Dense(1))

    # learning rate decay
    lr_schedule = tf.keras.optimizers.schedules.ExponentialDecay(
        learning_rate,
        decay_steps=training_epochs,
        decay_rate=0.95,
        staircase=True,
    )

    model.compile(
        optimizer=tf.keras.optimizers.Adam(learning_rate=lr_schedule),
        loss=tf.keras.losses.MeanAbsoluteError(),
    )

    # training the model

    # init tensorboard logging
    tensorboard_callback = tf.keras.callbacks.TensorBoard(
        log_dir=log_dir, histogram_freq=1
    )

    # fit the model
    model.fit(
        x_train,
        y_train,
        epochs=training_epochs,
        callbacks=[tensorboard_callback],
        validation_data=(x_test, y_test),
    )

    # testing the model
    # y_pred = model.predict(x_test)
    # print predictions
    # print("Predictions:")
    # print(y_pred)

    # plotting the results
    # plt.scatter(y_test, y_pred)
    # plt.xlabel("True Values")
    # plt.ylabel("Predictions")
    # plt.show()


if __name__ == "__main__":
    main()
