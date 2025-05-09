import useSWR from "swr";
import { IUser } from "./listUsers";
import { fetcher } from "../swrFetcher";

export const fetchUser = () => {
  const url = `/users/self`;
  return new Promise<IUser>((resolve, reject) => {
    fetcher
      .get<IUser>(url)
      .then((res) => {
        resolve(res.data);
      })
      .catch((e) => {
        reject(e);
      })
      .finally(() => {
        // store.dispatch(popLoad());
      });
  });
};

export const useFetchSelf = () => {
  return useSWR<IUser>("users/self", () => fetchUser());
};
