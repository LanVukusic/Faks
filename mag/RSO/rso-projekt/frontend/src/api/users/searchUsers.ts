import useSWR from "swr";
import { fetcher } from "../swrFetcher";
import { IUser } from "./listUsers";

export const fetchSearchUsers = (search: string) => {
  const url = `/users/search?search_text=${search}`;
  return new Promise<IUser[]>((resolve, reject) => {
    fetcher
      .get<IUser[]>(url)
      .then((res) => {
        // console.log(res);
        resolve(res.data);
      })
      .catch((e) => {
        reject(e);
        // console.error(e);
      })
      .finally(() => {
        // store.dispatch(popLoad());
      });
  });
};

export const useSearchUsers = () => {
  return useSWR<IUser[]>("users", fetchSearchUsers);
};
