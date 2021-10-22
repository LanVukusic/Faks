import * as React from "react";
import {
  ChakraProvider,
  Box,
  Text,
  Link,
  theme,
  Heading,
  Container,
  Textarea,
  Alert,
  AlertIcon,
  AlertTitle,
  AlertDescription,
  Divider,
  Flex,
  Button,
  useClipboard,
} from "@chakra-ui/react";
import { IScene, SceneReader } from "./Engine/SceneReader";
import { Renderer } from "./Engine/Render";

const example: IScene = {
  vertices: [0, 0, 0, 100, 0, 0, 0, 100, 0],
  indices: [0, 1, 2],
  camera: {
    perspective: 1,
    rotation: [0, 0, 0],
    translation: [0, 0, 0],
  },
  model: {
    scale: [1, 1, 1],
    rotation: [0, 0, 0],
    translation: [0, 0, 0],
  },
};

export const App = () => {
  // things for handling example
  const [value] = React.useState(JSON.stringify(example, null));
  const { hasCopied, onCopy } = useClipboard(value);

  // things for validating json
  const [jsonText, setJsonText] = React.useState("");
  const [valid, setValid] = React.useState(false);
  const [scene, setScene] = React.useState<SceneReader>(new SceneReader());
  const [inputProps, setInputProps] = React.useState<IScene | null>(null);

  const handleChange = (state: string) => {
    setJsonText(state);
    try {
      // data is valid
      const data: IScene = JSON.parse(state);
      if (data.indices && data.vertices && data.camera && data.model) {
        const sceneObj = new SceneReader();
        sceneObj.readFromJson(data);
        console.log("najprej preberem ta kurac, ", sceneObj.pointMatrix);

        setInputProps(data);
        setScene(sceneObj);
        setValid(true);
      }
    } catch (e) {
      console.log(e);

      // data is invalid
      setValid(false);
    }
  };

  return (
    <ChakraProvider theme={theme}>
      <Box textAlign="center" fontSize="xl">
        <Heading as="h1" p="10">
          Domača naloga 01
        </Heading>
        <Container maxW="container.lg">
          <Box textAlign="left" p="2">
            <Text>
              V okviru prve domače naloge v programskem jeziku JavaScript (ES6)
              na 2D HTML platnu (kontekst 2d) brez uporabe zunanjih knjižnic
              realizirajte del grafičnega cevovoda za prikaz interaktivne 3D
              grafike.
            </Text>
            <br />
            <Text>
              Navodila so objavljna na &nbsp;
              <Link
                color="teal.500"
                href="https://ucilnica.fri.uni-lj.si/mod/assign/view.php?id=45272"
              >
                Učilnici
              </Link>
            </Text>
            <Text>
              Koda je na &nbsp;
              <Link
                color="teal.500"
                href="https://github.com/LanVukusic/Faks/tree/master/3letnik/RGTI"
              >
                GitHub-u
              </Link>
            </Text>
            <br />

            <Text>
              Zaradi preference strogo tipiziranih jezikov uporabljam &nbsp;
              <Link color="teal.500" href="https://www.typescriptlang.org/">
                Typescript. &nbsp;
              </Link>
              Vsa koda ki, je zahtevana v navodilih se nahaja v mapi
              <b>"Engine".</b>
            </Text>
          </Box>

          <br />
          <Divider />
          <br />

          <Heading as="h1" p="2" textAlign="left">
            Vnos podatkov
          </Heading>

          <Flex mb={2} alignItems="center">
            <Text textAlign="left" backgroundColor="gray.100" p="2" m="2">
              {value}
            </Text>
            <Button onClick={onCopy} ml={2} color="teal.500">
              {hasCopied ? "Copied" : "Copy"}
            </Button>
          </Flex>

          <Textarea
            placeholder="prostor za JSON"
            value={jsonText}
            onChange={(e) => {
              handleChange(e.target.value);
            }}
            isInvalid={!valid}
            resize={"vertical"}
            rows={6}
            fontSize="1.4rem"
          />
          {!valid && (
            <Alert status="error" variant="subtle" mt="5">
              <AlertIcon />
              <AlertTitle mr={2}>Neprepoznan objekt.</AlertTitle>
              <AlertDescription>Vnesite veljaven objekt.</AlertDescription>
            </Alert>
          )}
          {valid && (
            <Alert status="success" variant="subtle" mt="5">
              <AlertIcon />
              <AlertTitle mr={2}>Objekt prepoznan.</AlertTitle>
              <AlertDescription>Vnešen je veljaven objekt.</AlertDescription>
            </Alert>
          )}

          <br />
          <Divider />
          <br />
          {inputProps && (
            <Renderer scene={scene} valid={valid} desc={inputProps}></Renderer>
          )}
        </Container>
      </Box>
    </ChakraProvider>
  );
};
