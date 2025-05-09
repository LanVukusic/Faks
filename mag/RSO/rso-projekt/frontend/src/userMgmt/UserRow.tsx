import {
  Group,
  Badge,
  Checkbox,
  Text,
  Button,
  Box,
  Select,
  Avatar,
} from "@mantine/core";
import { IconEdit } from "@tabler/icons";
import { useState } from "react";
import { EditUserModal } from "./Modal";
import { useEditUser } from "../api/users/editUser";
import { IUser } from "../api/users/listUsers";
import { useConfirmUser } from "../api/users/confirmUser";

interface IUserRowProps {
  item: IUser;
  i: number;
}

export const UserRow = ({ item: userIn, i }: IUserRowProps) => {
  const {
    error: errConfirm,
    loading: loadingConfirm,
    mutateUser: confirmUser,
  } = useConfirmUser(userIn.Uuid);

  // const { error, loading, editUserProps } = useEditUser();

  const mainCol = userIn.Role === "admin" ? "red" : "";

  const user = (
    <Group spacing="sm">
      <Avatar color={mainCol}>{userIn.Role === "admin" ? "AD" : "UP"}</Avatar>
      <div>
        <Text size="sm" weight={500} color={mainCol}>
          {userIn.Name} {userIn.Surname}
        </Text>
        <Text size="xs" color="dimmed">
          {userIn.Email}
        </Text>
      </div>
    </Group>
  );

  if (!userIn.Confirmed) {
    return (
      <tr key={userIn.Uuid}>
        <td>
          <EditUserModal mainCol={mainCol} user={userIn} />
        </td>
        <td>{user}</td>

        <td>
          <Text>
            <Badge color={mainCol} size="lg">
              {userIn.Room}
            </Badge>
          </Text>
        </td>

        <td colSpan={2}>
          <Box
            sx={(theme) => ({
              borderRadius: "5px",
              padding: "0.5rem",
            })}
            style={{
              backgroundColor: "rgba(0, 255, 50, 0.1)",
            }}
          >
            <Button
              size="xs"
              loading={loadingConfirm}
              onClick={() => {
                confirm("Zelite potrditi osebo?") && confirmUser();
              }}
              color={errConfirm ? "yellow" : "green"}
            >
              {errConfirm ? "Ponovi" : "Potrdi"}
            </Button>
          </Box>
        </td>
      </tr>
    );
  }

  return (
    <tr key={userIn.Uuid}>
      <td>
        {/* <Button leftIcon={<IconEdit />} variant="light" color={mainCol}>
          {i + 1}
        </Button> */}
        <EditUserModal mainCol={mainCol} user={userIn} />
      </td>
      <td
        style={{
          opacity: userIn.Disabled ? 0.2 : 1,
        }}
      >
        {user}
      </td>
      <td
        style={{
          opacity: userIn.Disabled ? 0.2 : 1,
        }}
      >
        <Badge color={mainCol} size="lg">
          {userIn.Room}
        </Badge>
      </td>
      <td
        style={{
          opacity: userIn.Disabled ? 0.2 : 1,
        }}
      >
        {userIn.Phone}
      </td>

      <td>
        {userIn.Disabled ? (
          <Badge color="red">Onemogočen</Badge>
        ) : (
          <Badge color="green">Aktiven</Badge>
        )}
      </td>
    </tr>
  );
};
