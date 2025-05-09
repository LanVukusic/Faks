import { Box, Flex, RingProgress, Text } from "@mantine/core";
import { Calendar } from "@mantine/dates";
import "dayjs/locale/sl";
import { ITermin } from "../api/termin/addTermin";
import { useIsMobile } from "../hooks/media";

interface ICalProps {
  setDate: (date: Date) => void;
  data: ITermin[];
  month: Date;
  setMonth: (date: Date) => void;
}

export function Cal({ setDate, data, month: thisMonth, setMonth }: ICalProps) {
  const today = new Date(new Date().setHours(0, 0, 0, 0));
  const maxDate = new Date();
  maxDate.setMonth(today.getMonth() + 1);

  const mobile = useIsMobile();

  console.log(data);

  return (
    <>
      {/* {JSON.stringify(data, null, 2)} */}
      <Calendar
        locale="sl"
        lang="sl"
        weekdayLabelFormat="ddd"
        firstDayOfWeek="monday"
        month={thisMonth}
        onMonthChange={setMonth}
        onChange={setDate}
        fullWidth
        size={mobile ? "md" : "xl"}
        renderDay={(date) => {
          const day = date.getDate();
          const month = date.getMonth();
          const isPast = date < today;
          return (
            <Flex
              align="center"
              justify="center"
              opacity={isPast ? "0.8" : "1"}
              //
            >
              {!isPast && data && month == thisMonth.getMonth() ? (
                <RingProgress
                  m={0}
                  p={0}
                  size={mobile ? 40 : 60}
                  thickness={mobile ? 4 : 6}
                  roundCaps
                  label={
                    <Text
                      size={mobile ? "sm" : "lg"}
                      weight={day == today.getDate() ? 800 : 400}
                      color={day == today.getDate() ? "cyan" : "gray"}
                      pt="2px"
                      sx={(theme) => ({
                        color:
                          day == today.getDate() ? theme.primaryColor : "gray",
                      })}
                    >
                      {day}
                    </Text>
                  }
                  sections={[
                    {
                      value:
                        (data.filter((t) => new Date(t.Date).getDate() == day)
                          .length /
                          8) *
                        50,
                      color: "cyan",
                    },
                  ].filter((s) => s.value > 0)}
                />
              ) : (
                <Box>{day}</Box>
              )}
            </Flex>
          );
        }}
        minDate={today}
        maxDate={maxDate}
        styles={(theme) => ({
          cell: {
            border: `1px solid ${
              theme.colorScheme === "dark"
                ? theme.colors.dark[4]
                : theme.colors.gray[2]
            }`,
          },
          day: {
            borderRadius: 0,
            // height: "100px",
            fontSize: theme.fontSizes.lg,
          },
          weekday: { fontSize: theme.fontSizes.lg },
          weekdayCell: {
            fontSize: theme.fontSizes.xl,
            backgroundColor:
              theme.colorScheme === "dark"
                ? theme.colors.dark[5]
                : theme.colors.gray[0],
            border: `1px solid ${
              theme.colorScheme === "dark"
                ? theme.colors.dark[4]
                : theme.colors.gray[2]
            }`,
            height: 100,
          },
        })}
      />
      {/* <Notification
        loading={!data && !error}
        title="Uploading data to the server"
        disallowClose
      >
        Please wait until data is uploaded, you cannot close this notification
        yet
      </Notification> */}
    </>
  );
}
