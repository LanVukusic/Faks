import { Box, Flex, Heading } from "@chakra-ui/react";
import React, { useRef } from "react";
import { Matrix, worldToCam } from "./Matrix";
import { IScene, Point, SceneReader } from "./SceneReader";

interface ISceneHold {
  scene: SceneReader;
  valid: boolean;
  desc: IScene;
}

export const Renderer = ({ scene, valid, desc }: ISceneHold) => {
  const height = 600;
  const width = 800;

  const canvasRef = useRef<HTMLCanvasElement>(null);
  const canvas = canvasRef.current;
  const ctx = canvas?.getContext("2d");

  console.log("2 jadan renderer dobi sceno, ", scene.pointMatrix);

  const drawLine = (s: Point, e: Point) => {
    if (!ctx) return;

    ctx.beginPath();
    // Staring point (10,45)
    ctx.moveTo(s.x, s.y);
    // End point (180,47)
    ctx.lineTo(e.x, e.y);
    // Make the line visible
    ctx.lineWidth = 2;
    ctx.stroke();
  };

  if (valid) {
    // https://www.scratchapixel.com/lessons/3d-basic-rendering/computing-pixel-coordinates-of-3d-point/mathematics-computing-2d-coordinates-of-3d-points

    // drawing pipeline

    // scale
    const sc = desc.model.scale;
    if (sc) {
      console.log("3, skaliram ta jajca, ", desc.model.scale);

      scene.scale(sc[0], sc[1], sc[2]);
    }
    scene.pointMatrix.debug("7, pogledam ce je prou skalirano, ");

    // rotate
    const rt = desc.model.rotation;
    console.log("8, rotiram ta faking garbage, rotation", desc.model.rotation);
    scene.rotate(rt[0], rt[1], rt[2]);
    scene.pointMatrix.debug("9 rotirano govno zglea tko");

    // translate
    const tr = desc.model.translation;
    console.log("10, transičam to, ", desc.model.translation);
    scene.translate(tr[0], tr[1], tr[2]);
    scene.pointMatrix.debug("11, transič zglea tko");

    // local to world transformation
    // i believe that there is nothing to do, since world and local coordinates are the same. we just have a single object here

    // local to camera transformation
    // inverse transforms
    let camPersp = desc.camera.perspective; // int
    const camRot = desc.camera.rotation; // [x,y,z]
    const camPos = desc.camera.translation; // [x,y,z]

    if (!camPersp) camPersp = 1;

    // world to screen
    // // orto
    // const m = new Matrix([
    //   [1, 0, 0, 0],
    //   [0, 1, 0, 0],
    //   [0, 0, 1, 0],
    //   [0, 0, 0, 1],
    // ]);

    //persp
    const worldCamMatrix = worldToCam(camPos, camRot);
    const m = new Matrix([
      [1, 0, 0, 0],
      [0, 1, 0, 0],
      [0, 0, 1, 0],
      [0, 0, 0, 1 / camPersp],
    ]);

    const camPoints = worldCamMatrix.matmul(scene.pointMatrix);

    const screenPoints = m.matmul(camPoints);

    // draw
    const pointArr = screenPoints.toArray();
    const finalPoints: Array<Point> = [];
    for (let i = 0; i < pointArr[0].length; i++) {
      finalPoints.push(new Point(pointArr[0][i], pointArr[1][i], 0));
    }

    ctx?.clearRect(0, 0, 999, 999);
    ctx?.translate(0, height);
    ctx?.scale(1, -1);
    scene.triangles.forEach((triangle, i) => {
      const pointIndexes = triangle.indexes;
      drawLine(finalPoints[pointIndexes[0]], finalPoints[pointIndexes[1]]);
      drawLine(finalPoints[pointIndexes[1]], finalPoints[pointIndexes[2]]);
      drawLine(finalPoints[pointIndexes[0]], finalPoints[pointIndexes[2]]);
    });
    // drawLine(new Point(0, 0, 0), new Point(50, 50, 0));
  }

  return (
    <Box>
      <Heading as="h1" p="2" textAlign="left">
        Izris
      </Heading>
      <Flex
        border="2px solid"
        m="2"
        borderRadius="5px"
        borderColor="teal.500"
        justifyContent="center"
        alignItems="center"
      >
        <canvas
          height={`${height}px`}
          width={`${width}px`}
          ref={canvasRef}
        ></canvas>
      </Flex>
    </Box>
  );
};
