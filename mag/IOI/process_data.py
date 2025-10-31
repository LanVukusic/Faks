import pandas as pd
import geopandas as gpd

# Load the tabular data
df = pd.read_excel("obcine.xlsx").dropna()

# Load the geojson data
gdf = gpd.read_file("obcine.geojson")

# Rename the municipality column for consistency
gdf = gdf.rename(columns={"OB_UIME": "obcina"})

# Ensure CRS is set for accurate calculations, then reproject to a suitable CRS for area/centroid
gdf = gdf.set_crs("EPSG:4326")
gdf_proj = gdf.to_crs("EPSG:3857")  # Use a projected CRS for calculations

# Calculate the area of each municipality
gdf["area"] = gdf_proj["geometry"].area

# Calculate the centroid of each municipality
gdf["centroid_lon"] = gdf["geometry"].centroid.x
gdf["centroid_lat"] = gdf["geometry"].centroid.y

# Merge the two dataframes
merged_df = pd.merge(
    df, gdf[["obcina", "area", "centroid_lon", "centroid_lat"]], on="obcina", how="left"
)

# Save the result to a new json file
merged_df.to_json("obcine.json", orient="records")

print("Data processing complete. obcine.json has been updated.")
