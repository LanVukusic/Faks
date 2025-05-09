import asyncio
import python_weather
from fastapi import FastAPI
import uvicorn
from grpc_server.service_pb2 import Weather, Void
from grpc_server.service_pb2_grpc import WeatherServiceServicer, add_WeatherServiceServicer_to_server
from sonora.asgi import grpcASGI
from starlette.middleware.cors import CORSMiddleware
from starlette.middleware.base import BaseHTTPMiddleware
from starlette.types import ASGIApp

# FastAPI app for health check
app = FastAPI()

@app.get("/health")
async def healthcheck():
    return {"status": "ok"}

# gRPC service
class MyService(WeatherServiceServicer):
    async def GetWeather(self, request, context):
        """get the weather string description"""
        async with python_weather.Client() as client:
            weather = await client.get("Ljubljana")
            return Weather(weather=f"Current temperature: {weather.temperature}°C")

class GRPCWebMiddleware(BaseHTTPMiddleware):
    def __init__(self, app: ASGIApp):
        super().__init__(app)

    async def dispatch(self, request, call_next):
        response = await call_next(request)
        response.headers["Content-Type"] = "application/grpc-web+proto"
        del response.raw_headers[0]  # Modify raw headers as necessary
        return response

async def grpc_server():
    application = grpcASGI(uvicorn, False)
    add_WeatherServiceServicer_to_server(MyService(), application)

    # Add CORS middleware
    application = CORSMiddleware(
        application,
        allow_origins=["*"],  # Adjust this to your allowed origins
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
    )

    # Wrap the app with GRPCWebMiddleware
    application = GRPCWebMiddleware(application)

    config = uvicorn.Config(application, port=8080, log_level="info")
    server = uvicorn.Server(config)
    await server.serve()

async def fastapi_server():
    config = uvicorn.Config(app, host="0.0.0.0", port=8000, log_level="info")
    server = uvicorn.Server(config)
    await server.serve()

async def main():
    await asyncio.gather(
        grpc_server(),
        fastapi_server()
    )

if __name__ == "__main__":
    asyncio.run(main())
