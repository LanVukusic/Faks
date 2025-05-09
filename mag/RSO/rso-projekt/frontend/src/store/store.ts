import { atom } from "nanostores";

type SelfUser = {
  Uuid: string;
  Name: string;
  Email: string;
  Phone: string;
  Role: string;
};

export const me = atom<SelfUser | null>({
  Uuid: "17587b7c-8038-470a-829b-0b417dde45fd",
  Phone: "12345678960",
  Name: "Tina",
  Email: "eve.miller@example.com,mm",
  Role: "admin",
});

export function addUser(user: SelfUser) {
  me.set(user);
}

export function removeUser() {
  me.set(null);
}
