import numpy as np
from datetime import datetime, timedelta

# import tensorflow as tf
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
from sklearn import preprocessing as pp

# sori tensorflow rad te mam smo....
from sklearn.linear_model import LinearRegression

# defining global variables
learning_rate = 0.005
training_epochs = 500

# tensorboard
log_dir = (
    "logs/fit/"
    + datetime.now().strftime("%Y%m%d-%H%M%S")
    + f"P:{learning_rate}_{training_epochs}"
)

# print(tf.__version__)

# testing training split
def split_data(data_x, data_y, train_percentage):
    # split data
    train_size = int(len(data_x) * train_percentage)
    train_x = data_x[:train_size]
    train_y = data_y[:train_size]
    test_x = data_x[train_size:]
    test_y = data_y[train_size:]

    return train_x, train_y, test_x, test_y


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


# output_df = PolynomialFeatures_labeled(input_df, 2)
def polify(data, feature_name, power):
    poly = pp.PolynomialFeatures(power, include_bias=False)
    # pandas dataframe to numpy array
    polified = poly.fit_transform(data[feature_name].values.reshape(-1, 1))
    # numpy array to pandas dataframe
    polified = pd.DataFrame(polified, columns=poly.get_feature_names([feature_name]))
    return polified


def get_data_x(filename):
    df = pd.read_csv(filename, delimiter=",", header="infer")

    df = df[["Driver ID", "Departure time"]]

    d = DateManipulator()

    df["Departure time"] = pd.to_datetime(df["Departure time"])

    # times
    df["Departure hour"] = df["Departure time"].apply(lambda x: x.hour)
    df["Departure month"] = df["Departure time"].apply(lambda x: x.month)

    # df["Departure inmin"] = df["Departure hour"] * 60 + df["Departure minute"]
    # df["Arrival inmin"] = df["Arrival hour"] * 60 + df["Arrival minute"]
    # get day of the week
    df["Departure day"] = df["Departure time"].apply(lambda x: x.weekday())

    one_hot_drivers = pd.get_dummies(df["Driver ID"])
    one_hot_week_day = pd.get_dummies(
        df["Departure day"],
        prefix="day",
    )
    one_hot_month = pd.get_dummies(df["Departure month"], prefix="month")
    poly_month = polify(df, "Departure month", 2)
    poly_hours = polify(df, "Departure hour", 4)

    train_df = pd.concat(
        [
            poly_hours,
            # one_hot_drivers,
            one_hot_week_day,
            poly_month,
        ],
        axis=1,
    )

    print("shapee", train_df.shape)
    # print(train_df.head())
    # print(train_df.describe())

    return train_df.to_numpy()


def get_data_y(filename):
    df = pd.read_csv(filename, delimiter=",", header="infer")

    df = df[["Departure time", "Arrival time"]]

    d = DateManipulator()

    df["Departure time"] = pd.to_datetime(df["Departure time"])
    df["Arrival time"] = pd.to_datetime(df["Arrival time"])

    # get travel time
    df["Travel time"] = df["Arrival time"] - df["Departure time"]
    df["Travel time"] = df["Travel time"].apply(lambda x: x.total_seconds())

    return df["Travel time"].to_numpy()


def seconds_to_date(departure, seconds):
    d = DateManipulator()
    return d.addSeconds(departure, seconds)


def main():

    data_x = get_data_x("train_pred.csv")
    data_y = get_data_y("train_pred.csv")

    # split data
    train_x, train_y, test_x, test_y = split_data(data_x, data_y, 0.8)

    test_stamps = pd.read_csv("test.tsv", delimiter=",", header="infer")[
        "Departure time"
    ]
    test_x = get_data_x("test.tsv")

    print(data_x.shape, data_y.shape, test_x.shape)
    DATA_LEN = data_x.shape[1]

    # # defining the model using tensorflow 2
    # model = tf.keras.Sequential()
    # model.add(tf.keras.layers.Dense(1, input_shape=(DATA_LEN,)))
    # model.add(tf.keras.layers.Dense(1))

    reg = LinearRegression(fit_intercept=True, normalize=True)
    reg.fit(train_x, train_y)

    # reg = LinearRegression().fit(data_x, data_y)
    print(reg.score(test_x, test_y))
    y_pred = reg.predict(test_x)

    mae = np.mean(np.abs(y_pred - test_y))
    print(mae)

    out = reg.predict(test_x)
    out = [str(seconds_to_date(test_stamps[i], out[i]))[:-3] for i in range(len(out))]

    df_out = pd.DataFrame(out)
    df_out.to_csv("out.csv", index=False)
    exit(0)

    # # learning rate decay
    # lr_schedule = tf.keras.optimizers.schedules.ExponentialDecay(
    #     learning_rate,
    #     decay_steps=training_epochs,
    #     decay_rate=0.95,
    #     staircase=True,
    # )

    # model.compile(
    #     optimizer=tf.keras.optimizers.Adam(learning_rate=lr_schedule),
    #     loss=tf.keras.losses.MeanAbsoluteError(),
    # )

    # # training the model

    # # init tensorboard logging
    # tensorboard_callback = tf.keras.callbacks.TensorBoard(
    #     log_dir=log_dir, histogram_freq=1
    # )

    # # fit the model
    # model.fit(
    #     data_x,
    #     data_y,
    #     epochs=training_epochs,
    #     callbacks=[tensorboard_callback],
    #     # validation_data=(x_test, y_test),
    # )


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
