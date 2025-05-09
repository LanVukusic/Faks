import axios from "axios";

export const fetcher = axios.create({
  baseURL: import.meta.env.VITE_BACKEND_URL,
  timeout: 4000,
  headers: {
    "Content-Type": "application/json",
    // Authorization: `Bearer ${auth?.currentUser?.uid || ""}`,
  },
});
