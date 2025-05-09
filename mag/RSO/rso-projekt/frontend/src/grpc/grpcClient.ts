import { GrpcWebFetchTransport } from "@protobuf-ts/grpcweb-transport";
import { WeatherServiceClient } from "./client_code/service.client";
import { useEffect, useState } from "react";
import { Weather } from "./client_code/service";

const transport = new GrpcWebFetchTransport({
  baseUrl: import.meta.env.VITE_BACKEND_WEATHER,
  format: "binary",
  meta: {
    token: "ojla",
  },
});

export const client = new WeatherServiceClient(transport);

export const useWeatherPolling = () => {
  const [weather, setWeather] = useState<Weather>();

  useEffect(() => {
    const interval = setInterval(() => {
      client.getWeather({}).then(
        (res) => {
          if (res.response) {
            setWeather({
              weather: res.response.weather,
            });
          }
        },
        (status) => {
          ({
            title: "Cant send graph update",
            message: JSON.stringify(status, null, 1),
          });
          console.error(status);
        }
      );

      return () => {
        clearInterval(interval);
      };
    }, 2000);
  }, []);

  return {
    weather,
  };
};
