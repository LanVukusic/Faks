import pandas as pd

# Load the food price data
df = pd.read_excel("food.xlsx")

# Melt the dataframe to transform it into a long format
df = df.melt(
    id_vars=["Unnamed: 0"], var_name="food_item", value_name="percentage_change"
)

# Rename the columns for clarity
df = df.rename(columns={"Unnamed: 0": "date"})

# Convert the date column to datetime objects.
df["date"] = pd.to_datetime(df["date"], format="%Y")

# Sort by date and food_item
df = df.sort_values(by=["food_item", "date"])

# Calculate the price index from percentage change, starting from a base of 100 for each food item
df["price_index"] = df.groupby("food_item")["percentage_change"].transform(
    lambda x: (1 + x / 100).cumprod() * 100
)

# Select the relevant columns for the output
output_df = df[["date", "food_item", "price_index"]]

# Save the processed data to a JSON file
output_df.to_json("food_prices.json", orient="records")

print("Food price data processing complete. food_prices.json has been created.")
