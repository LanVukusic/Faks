import {
  Avatar,
  Box,
  Button,
  Divider,
  Flex,
  Input,
  Modal,
  NumberInput,
  Select,
  Stack,
  Switch,
  Text,
  Title,
} from "@mantine/core";
import { useForm } from "@mantine/form";
import {
  IconAt,
  IconDoor,
  IconEdit,
  IconLock,
  IconPhone,
  IconUserCircle,
} from "@tabler/icons";
import { useEffect, useState } from "react";
import { useEditUser } from "../api/users/editUser";
import { IUser } from "../api/users/listUsers";
import { TerminTable } from "./TerminTable";

export interface IModalProps {
  mainCol: string;
  user: IUser;
}

export function EditUserModal({ mainCol, user }: IModalProps) {
  const [opened, setOpened] = useState(false);
  const { error, loading, editUserProps } = useEditUser();

  let form = useForm({
    initialValues: {
      ...user,
    },
  });

  useEffect(() => {
    form.setValues(user);
    form.setTouched({});
    form.setDirty({});
  }, [user]);

  const handleSubmit = (newUser: IUser) => {
    editUserProps(newUser).then(() => {
      setOpened(false);
    });
  };

  return (
    <>
      <Modal
        opened={opened}
        onClose={() => {
          setOpened(false);
          form.reset();
        }}
        title={
          <Flex m="lg" mb={"2rem"} direction="row" align={"center"}>
            {/* <Flex align="center" justify="center"> */}
            <Avatar radius="xl" size="lg" color="blue">
              {user.Room}
            </Avatar>
            <Box m={"xl"} />
            <Box>
              <Title>{`${user.Name} ${user.Surname}`}</Title>
              {/* <Text py={"xs"} size="xs" opacity={0.4}>
                {user.uuid}
              </Text> */}
            </Box>
            {/* </Flex> */}
          </Flex>
        }
      >
        <Text size="xs" opacity={0.4}>
          <strong>UUID: </strong>
          {user.Uuid}
        </Text>
        <form
          onSubmit={form.onSubmit((values) => {
            handleSubmit(values);
          })}
        >
          <Stack spacing={"xs"}>
            <Divider label={"Kontakt"} labelPosition="right" />
            {/* <Input icon={<IconAt />} placeholder="email" /> */}
            <Input
              icon={<IconAt size={16} />}
              placeholder="Email"
              __staticSelector=""
              value={"lan.vukusic@gmail.com"}
              {...form.getInputProps("Email")}
            />
            <Input
              icon={<IconUserCircle size={16} />}
              placeholder="ime"
              __staticSelector=""
              {...form.getInputProps("Name")}
            />
            <Input
              icon={<IconUserCircle size={16} />}
              placeholder="priimek"
              {...form.getInputProps("Surname")}
            />
            <Input
              icon={<IconPhone size={16} />}
              placeholder="telefon"
              type="tel"
              {...form.getInputProps("Phone")}
            />
            <NumberInput
              icon={<IconDoor size={16} />}
              placeholder={350}
              type="number"
              {...form.getInputProps("Room")}
            />
            <Switch
              onLabel="Bannan"
              offLabel="Aktiven"
              size="lg"
              color="red"
              {...form.getInputProps("Disabled", { type: "checkbox" })}
            />
            <Divider label={"Administracija"} labelPosition="right" />
            <Select
              {...form.getInputProps("Role")}
              label="Uporabniški tip"
              placeholder="Uporabniški tip"
              icon={<IconLock size={14} />}
              data={[
                { value: "admin", label: "admin" },
                { value: "user", label: "user" },
              ]}
            />
            <Flex my="md">
              <Button
                variant="light"
                style={{ flex: "1" }}
                type="submit"
                m={"sm"}
                disabled={!form.isTouched()}
                loading={loading}
              >
                Shrani
              </Button>
            </Flex>
            <Divider label={"Termini"} labelPosition="right" />

            <TerminTable uuid={user.Uuid} />
          </Stack>
        </form>
      </Modal>

      <Button
        leftIcon={<IconEdit />}
        variant="light"
        color={mainCol}
        onClick={() => setOpened(true)}
      >
        uredi
      </Button>
    </>
  );
}
