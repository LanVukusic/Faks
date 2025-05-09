import {
  Group,
  Avatar,
  Text,
  Accordion,
  Stack,
  Table,
  Flex,
  Title,
  Button,
  Box,
  createStyles,
} from "@mantine/core";
import { IconPhoneCall, IconAt, IconLanguage, IconDoor } from "@tabler/icons";
import { useAddTermin } from "../api/termin/addTermin";
import { IUser } from "../api/users/listUsers";

export interface RowProps {
  hourFrom: number;
  hourTo: number;
  uuid?: string;
  machine: number;
}

const useStyles = createStyles((theme) => ({
  icon: {
    color:
      theme.colorScheme === "dark"
        ? theme.colors.dark[3]
        : theme.colors.gray[5],
  },

  name: {
    fontFamily: `Greycliff CF, ${theme.fontFamily}`,
  },
}));

function usertoAvatar(user: IUser) {
  const col = user.role == "admin" ? "purple" : "blue";
  return (
    <Avatar radius="xl" size="md" color={col}>
      {user.name[0]} {user.surname[0]}
    </Avatar>
  );
}

// export function AccordionLabel({ hourFrom, hourTo, machine, user }: RowProps) {
//   return (
//     <Group noWrap align="center">
//       {user && usertoAvatar(user)}
//       {!user && (
//         <Avatar
//           radius="xl"
//           size="md"
//           color={machine == 1 ? "orange" : "cyan"}
//           variant="outline"
//         >
//           {/* {machine} */}
//           <Box
//             style={{
//               borderRadius: "100%",
//               backgroundColor: machine == 1 ? "orange" : "cyan",
//               height: "0.3rem",
//               width: "0.3rem",
//             }}
//           />
//         </Avatar>
//       )}
//       <div>
//         <Text>{`${hourFrom} - ${hourTo}h`}</Text>
//       </div>
//     </Group>
//   );
// }

export function AccordionList() {
  const { addTerminF, error, loading } = useAddTermin();
  const { classes } = useStyles();

  const ins: RowProps[] = [];

  const items = ins.map((item, i) => (
    <Accordion.Item value={"item_" + i} key={"item_" + i}>
      <Accordion.Control>{/* <Accordion. {...item} /> */}</Accordion.Control>
      <Accordion.Panel>
        {item.user && (
          <>
            <Text size="lg" weight={500} className={classes.name}>
              {item.user.name} {item.user.surname}
            </Text>

            <Group noWrap spacing={10} mt={3}>
              <IconAt stroke={1.5} size={16} className={classes.icon} />
              <Text size="xs" color="dimmed">
                {item.user.email}
              </Text>
            </Group>

            <Group noWrap spacing={10} mt={5}>
              <IconPhoneCall stroke={1.5} size={16} className={classes.icon} />
              <Text size="xs" color="dimmed">
                {item.user.phone}
              </Text>
            </Group>

            <Group noWrap spacing={10} mt={5}>
              <IconDoor stroke={1.5} size={16} className={classes.icon} />
              <Text size="xs" color="dimmed">
                {item.user.room}
              </Text>
            </Group>
          </>
        )}
        {!item.user && (
          <Box m="md">
            <Text>
              Prijavi se na termin od <strong>{item.hourFrom}:00</strong> do{" "}
              <strong>{item.hourTo}:00 </strong>
              ure
            </Text>
            <Box m="md" />
            <Button
              variant="light"
              loading={loading}
              onClick={() => {
                console.log(item.user);

                item.user &&
                  addTerminF({
                    uuid: item.user.uuid,
                    washer: item.machine,
                    termin: i,
                    date: new Date().getDate(),
                  });
              }}
            >
              Prijava
            </Button>
          </Box>
        )}
      </Accordion.Panel>
    </Accordion.Item>
  ));

  return (
    <Accordion
      chevronPosition="right"
      variant="default"
      style={{
        flex: 1,
      }}
    >
      {items}
    </Accordion>
  );
}
