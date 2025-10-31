import pandas as pd

# Load the datasets
energy_df = pd.read_json("electricity_by_time.json")
food_df = pd.read_json("food_prices.json")

# Convert date columns to datetime objects
energy_df["date"] = pd.to_datetime(energy_df["date"])
food_df["date"] = pd.to_datetime(food_df["date"])

# Set date as index for resampling
energy_df = energy_df.set_index("date")

# Resample energy data to the start of the month
energy_df = energy_df.resample("MS").mean()

# Pivot the food data to have food items as columns
food_pivot_df = food_df.pivot(index="date", columns="food_item", values="price_index")

# Merge the two dataframes
combined_df = pd.merge(
    energy_df, food_pivot_df, left_index=True, right_index=True, how="outer"
)

# Forward-fill the food price data to fill missing months
combined_df = combined_df.ffill()

# Reset the index to have 'date' as a column again
combined_df = combined_df.reset_index()

# Melt the dataframe to bring food items back into a single column for Vega-Lite
combined_df = combined_df.melt(
    id_vars=["date", "consumption"], var_name="food_item", value_name="price_index"
)

# Rename for clarity in the visualization
combined_df = combined_df.rename(columns={"consumption": "energy_consumption"})

# Save the combined data to a new JSON file
combined_df.to_json("combined_data.json", orient="records")

print("Data combination complete. combined_data.json has been created.")
