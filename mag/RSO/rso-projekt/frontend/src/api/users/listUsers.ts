import useSWR from "swr";
import { fetcher } from "../swrFetcher";

export interface IUser {
  Uuid: string;
  Phone: string;
  Name: string;
  Surname: string;
  Room: number;
  Email: string;
  Disabled: boolean;
  Confirmed: boolean;
  Role: "admin" | "user";
  CreatedAt: Date;
  UpdatedAt: Date;
}

export const fetchUsers = () => {
  const url = "/users";
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

export const useFetchUsers = () => {
  return useSWR<IUser[]>("users", fetchUsers);
};
