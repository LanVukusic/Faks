import { useToggle, upperFirst } from "@mantine/hooks";
import { useForm } from "@mantine/form";
import {
  TextInput,
  PasswordInput,
  Text,
  Paper,
  Group,
  PaperProps,
  Button,
  Divider,
  Checkbox,
  Anchor,
  Stack,
  NumberInput,
  Title,
  Flex,
} from "@mantine/core";
import {
  createUserWithEmailAndPassword,
  getAuth,
  onAuthStateChanged,
  signInWithEmailAndPassword,
} from "firebase/auth";
import { showNotification } from "@mantine/notifications";

import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { useRegisterUser } from "./api/registerUser";
import { addUser } from "./store/store";
import { user } from "firebase-functions/v1/auth";
import { IUser } from "./api/users/listUsers";

type BasicUser = Omit<
  IUser,
  | "disabled"
  | "uuid"
  | "confirmed"
  | "role"
  | "UpdatedAt"
  | "CreatedAt"
  | "Uuid"
  | "Disabled"
  | "Confirmed"
  | "Role"
>;
export interface IRegisterForm extends BasicUser {
  Password: string;
}

function roomNumberInRange(val: number) {
  return (
    (val > 0 && val < 29) ||
    (val > 100 && val < 129) ||
    (val > 200 && val < 229) ||
    (val > 300 && val < 329)
  );
}

export function AuthenticationForm(props: PaperProps) {
  const [type, toggle] = useToggle(["login", "register"]);
  const [loading, setLoading] = useState(false);
  const redirect = useNavigate();

  //swr registration
  const { error, loading: regLoad, registerUser } = useRegisterUser();

  const form = useForm<IRegisterForm>({
    initialValues: {
      Name: "",
      Surname: "",
      Room: 0,
      Email: "enei@enei.enei",
      Password: "eneienei",
      Phone: "+386",
    },

    validate: {
      Name: (val) => (type === "register" && !val ? "Manjka ime." : null),
      Surname: (val) =>
        type === "register" && !val ? "Manjka priimek." : null,
      Email: (val) =>
        /^\S+@\S+$/.test(val) ? null : "Neveljaven email naslov.",
      Password: (val) =>
        val.length < 8 ? "Geslo mora vsebovati vsaj 8 znakov." : null,
      Room: (val: number | undefined) =>
        type == "login" || (val && roomNumberInRange(val))
          ? null
          : "Neveljavna številka sobe.",
    },
  });

  return (
    <Flex
      direction="column"
      align="center"
      justify="center"
      style={{
        width: "100vw",
        height: "100vh",
      }}
    >
      <Title order={1} py={30}></Title>
      <Paper radius="md" p="xl" withBorder {...props}>
        <form
          autoComplete="on"
          onSubmit={form.onSubmit((a) => {
            setLoading(true);
            if (type == "register") {
              registerUser(a)
                .then(() => {
                  setLoading(false);
                  redirect("/");
                  console.log("redirecting");
                })
                .finally(() => setLoading(false));
            } else {
              // signInWithEmailAndPassword(auth, a.email, a.password)
              //   .then((userCredential) => {
              //     addUser({
              //       email: userCredential?.user?.email || "",
              //       name: userCredential?.user?.displayName || "",
              //       phone: userCredential?.user?.phoneNumber || "",
              //       role: "",
              //       uuid: userCredential?.user?.uid || "",
              //     });
              //     redirect("/");
              //   })
              //   .catch((error) => {
              //     const errorCode = error.code;
              //     const errorMessage = error.message;
              //     showNotification({
              //       title: `Prijavna napaka ${errorCode}`,
              //       message: errorMessage,
              //       color: "red",
              //     });
              //   })
              //   .finally(() => {
              //     setLoading(false);
              //   });
            }
          })}
        >
          <Stack>
            {type === "register" && (
              <>
                <TextInput
                  label="Ime"
                  placeholder="Ime"
                  value={form.values.Name}
                  onChange={(event) =>
                    form.setFieldValue("name", event.currentTarget.value)
                  }
                  error={form.errors.name}
                />

                <TextInput
                  label="Priimek"
                  placeholder="Priimek"
                  value={form.values.Surname}
                  onChange={(event) =>
                    form.setFieldValue("surname", event.currentTarget.value)
                  }
                  error={form.errors.surname}
                />

                <TextInput
                  label="Telefon"
                  type="tel"
                  placeholder="+386 40 123 456"
                  value={form.values.Phone}
                  onChange={(event) =>
                    form.setFieldValue("phone", event.currentTarget.value)
                  }
                  error={form.errors.surname}
                />

                <NumberInput
                  label="Številka sobe"
                  placeholder="401"
                  value={form.values.Room}
                  onChange={(num) => form.setFieldValue("room", num || 0)}
                  hideControls
                  error={form.errors.room}
                />
              </>
            )}

            <TextInput
              label="Email"
              placeholder="nekdo@gmail.com"
              value={form.values.Email}
              onChange={(event) =>
                form.setFieldValue("email", event.currentTarget.value)
              }
              error={form.errors.email && "Napačen email"}
            />

            <PasswordInput
              label="Geslo"
              placeholder="Geslo"
              value={form.values.Password}
              onChange={(event) =>
                form.setFieldValue("password", event.currentTarget.value)
              }
              error={
                form.errors.password && "Geslo mora vsebovati vsaj 8 znakov."
              }
            />
          </Stack>

          <Group position="apart" mt="xl">
            <Anchor
              component="button"
              type="button"
              color="dimmed"
              onClick={() => toggle()}
              size="xs"
            >
              {type === "register"
                ? "Že imaš račun? Prijava"
                : "Še nimaš računa? Registracija"}
            </Anchor>
            <Button type="submit" loading={loading}>
              {type === "register" ? "Registracija" : "Prijava"}
            </Button>
          </Group>
        </form>
      </Paper>
    </Flex>
  );
}
