import numpy as np
from datetime import datetime, timedelta
import pandas as pd
from sklearn import preprocessing as pp


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


def polify(data, feature_name, power):
    poly = pp.PolynomialFeatures(power)
    # pandas dataframe to numpy array
    polified = poly.fit_transform(data[feature_name].values.reshape(-1, 1))
    # numpy array to pandas dataframe
    polified = pd.DataFrame(polified, columns=poly.get_feature_names([feature_name]))
    return polified


def get_data(filename):
    df = pd.read_csv(filename, delimiter=",", header="infer")

    df = df[["Driver ID", "Departure time", "Arrival time"]]

    d = DateManipulator()

    df["Departure time"] = df["Departure time"].apply(lambda x: pd.to_datetime(x))
    df["Arrival time"] = df["Arrival time"].apply(lambda x: pd.to_datetime(x))
    df["Departure hour"] = df["Departure time"].apply(lambda x: x.hour)
    df["Departure month"] = df["Departure time"].apply(lambda x: x.month)
    # get day of the week
    df["Departure day"] = df["Departure time"].apply(lambda x: x.weekday())
    # get travel time
    df["Travel time"] = df["Arrival time"] - df["Departure time"]
    df["Travel time"] = df["Travel time"].apply(lambda x: x.total_seconds())

    one_hot_drivers = pd.get_dummies(df["Driver ID"])

    # one hot encode departure day
    one_hot_departure_day = pd.get_dummies(df["Departure day"])

    poly_hours = polify(df, "Departure hour", 4)
    poly_weekday = polify(df, "Departure day", 2)

    train_df = pd.concat(
        [poly_hours, one_hot_departure_day],
        axis=1,
    )

    print(train_df.head())
    print(train_df.describe())
    print(df["Travel time"].describe())

    return train_df, df[["Travel time"]]


def main():
    data_x, data_y = get_data("train_pred.csv")


if __name__ == "__main__":
    main()
