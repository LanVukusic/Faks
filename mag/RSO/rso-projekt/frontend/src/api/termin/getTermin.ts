import useSWR, { mutate } from "swr";
import { ITermin } from "./addTermin";
import { fetcher } from "../swrFetcher";

const getTermin = (id: string) => {
  const url = `/reservations/${id}`;
  return new Promise<ITermin>((resolve, reject) => {
    fetcher
      .get<ITermin>(url)
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
export const useGetTermin = (uuid: string) => {
  return useSWR<ITermin>(["reservations/" + uuid, "termin"], () =>
    getTermin(uuid)
  );
};

const getTerminsByUser = (uuid: string, active?: boolean) => {
  // const stillActive = active ? "/active" : "";
  const stillActive = "";

  // const url = `//reservations/user/${uuid}${stillActive}`;
  const url = `/reservations/user/${uuid}`;
  return new Promise<ITermin[]>((resolve, reject) => {
    fetcher
      .get<ITermin[]>(url)
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
export const useGetTerminsByUser = (uuid: string, active: boolean = true) => {
  // const stillActive = active ? "/active" : "";
  return useSWR<ITermin[]>(["/reservations/user/" + uuid, "termin"], () =>
    getTerminsByUser(uuid, active)
  );
};

// const getTerminsInRange = (start: number, end: number) => {
//   const url = `/getTerminsInRange/${start}/${end}`;
//   return new Promise<ITermin[]>((resolve, reject) => {
//     fetcher
//       .get<ITermin[]>(url)
//       .then((res) => {
//         resolve(res.data);
//       })
//       .catch((e) => {
//         reject(e);
//       })
//       .finally(() => {
//         // store.dispatch(popLoad());
//       });
//   });
// };
// export const useGetTerminsInRange = (start: number, end: number) => {
//   return useSWR<ITermin[]>("getTerminsInRange/" + start + "/" + end, () =>
//     getTerminsInRange(start, end)
//   );
// };

const getTerminsMonthly = (month: Date) => {
  const url = `/reservations/month/${month.toISOString()}`;
  return new Promise<ITermin[]>((resolve, reject) => {
    fetcher
      .get<ITermin[]>(url)
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

export const useGetTerminsMonthly = (month: Date) => {
  return useSWR<ITermin[]>(
    [`/getTerminsMonthly/${month.toISOString()}`, "termin"],
    () => getTerminsMonthly(month)
  );
};
