import { showNotification } from "@mantine/notifications";
import { useState } from "react";
import useSWR, { mutate } from "swr";
import { fetcher } from "../swrFetcher";

export interface ITermin {
  ID: string;
  Uuid: string; // user id
  Date: Date;
  Termin: number;
  Washer: number;
}

type AddTermin = Omit<ITermin, "id">;

const addTermin = (termin: AddTermin) => {
  const url = "/reservations";
  termin.Date.setDate(termin.Date.getDate() + 1);

  return new Promise<void>((resolve, reject) => {
    fetcher
      .post<AddTermin>(url, {
        ...termin,
        Date: termin.Date,
      })
      .then((res) => {
        resolve();
      })
      .catch((e) => {
        reject(e);
      })
      .finally(() => {
        // store.dispatch(popLoad());
      });
  });
};

export const useAddTermin = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<Error | null>(null);

  const addTerminF = (termin: AddTermin) => {
    return new Promise<void>((resolve, reject) => {
      setLoading(true);
      addTermin(termin)
        .then(() => {
          mutate((key: string) => {
            typeof key === "string" && key.includes("Reservation");
          });
          mutate("termin");
          showNotification({
            color: "green",
            title: "Dodano!",
            message: `Dodan termin`,
          });
          resolve();
        })
        .catch((e) => {
          setError(e);
          showNotification({
            color: "red",
            title: "Napaka!",
            message: `Napaka pri dodajanju ${e.message}`,
          });
          reject(e);
        })
        .finally(() => {
          setLoading(false);
        });
    });
  };

  return {
    loading,
    error,
    addTerminF,
  };
};
