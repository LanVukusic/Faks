# Use an official Python base image
FROM python:3.13-slim

# Set the working directory in the container
WORKDIR /app

# Copy the application requirements
COPY requirements.txt ./

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the application source code
COPY . .

# Expose the ports your app uses
EXPOSE 8080
EXPOSE 8000

# Command to run the app
# Replace `app.py` with your application's entry point
CMD ["python", "server.py"]
