import { showNotification } from "@mantine/notifications";
import { useState } from "react";
import { mutate } from "swr";
import { fetcher } from "../swrFetcher";

export const confirmUser = (uuid: string) => {
  const url = `/users/confirm/${uuid}`;

  return new Promise((resolve, reject) => {
    fetcher
      .post(url)
      .then((res) => {
        showNotification({
          color: "green",
          title: "Sprejeto!",
          message: `Uporabnik ${uuid} sprejet!`,
        });
        resolve(res.data);
      })
      .catch((e: Error) => {
        reject(e);
        showNotification({
          color: "red",
          title: "Napaka!",
          message: `Napaka ${e.message} pri sprejemu uporabnika!`,
        });
      })
      .finally(() => {
        // store.dispatch(popLoad());
      });
  });
};

export const useConfirmUser = (uuid: string) => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const mutateUser = () => {
    setLoading(true);
    confirmUser(uuid)
      .then(() => {
        mutate("users");
      })
      .catch((e) => {
        setError(e);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  return {
    loading,
    error,
    mutateUser,
  };
};
