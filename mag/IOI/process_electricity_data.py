import pandas as pd

# Load the Excel data
df = pd.read_excel("electricity_by_time.xlsx")

# Select the relevant columns
df = df[["Unnamed: 0", "electricity"]]

# Rename the columns
df.columns = ["date", "consumption"]

# Convert the date column to datetime objects
df["date"] = pd.to_datetime(df["date"], format="%YM%m")

# Save the processed data to a JSON file
df.to_json("electricity_by_time.json", orient="records")

print(
    "Electricity data processing complete. electricity_by_time.json has been created."
)
