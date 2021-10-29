"use strict";

import { NNPArray, NNP, Transform } from "./NNP.js";

// matrix getters
/**
 * returns generates nnp matrix from provided data
 * @param {Object} obj
 * @returns {NNPArray} array matrix
 */
const getPointMatrix = (obj) => {
  // check if vertices array is any good
  if (!obj?.vertices?.length || obj.vertices.length % 3 !== 0) {
    throw new Error("vertices are not present or not divisible by 3");
  }

  // process vertices
  const arrayBase = [[], [], [], []];
  for (let i = 0; i < obj.vertices.length; i += 3) {
    // read 3 coordinates
    arrayBase[0][i / 3] = obj.vertices[i + 0]; // X
    arrayBase[1][i / 3] = obj.vertices[i + 1]; // Y
    arrayBase[2][i / 3] = obj.vertices[i + 2]; // Z
    arrayBase[3][i / 3] = 1; // W
  }
  return NNP.array(arrayBase);
};

/**
 * returns generates an array of triangles from data
 * @param {Object} obj
 * @returns {Array<[number,number, number]>} array of 3tuples of points
 */
const getTriangleArr = (obj) => {
  // check if vertices array is any good
  if (!obj?.indices?.length || obj.indices.length % 3 !== 0) {
    throw new Error("indices are not present or not divisible by 3");
  }

  // process vertices
  /**
   * @type {Array<[number,number, number]>}
   */
  const arrayBase = [];
  for (let i = 0; i < obj.indices.length; i += 3) {
    // read 3 coordinates
    arrayBase.push([
      obj.indices[i + 0],
      obj.indices[i + 1],
      obj.indices[i + 2],
    ]);
  }
  return arrayBase;
};

/**
 * returns sclae array from scene object
 * @param {Object} obj
 * @returns {[number,number,number]} array of x y z scales
 */
const getModelScale = (obj) => {
  if (!(obj?.model?.scale?.length || obj.model.scale.length !== 3)) {
    throw new Error("model scale is passed incorrectly");
  }
  return obj.model.scale;
};

/**
 * returns rotation array from scene object
 * @param {Object} obj
 * @returns {[number,number,number]} array of x y z rotates
 */
const getModelRotation = (obj) => {
  if (!(obj?.model?.rotation?.length || obj.model.rotation.length !== 3)) {
    throw new Error("model rotation is passed incorrectly");
  }
  return obj.model.rotation;
};

/**
 * returns translation array from scene object
 * @param {Object} obj
 * @returns {[number,number,number]} array of x y z translates
 */
const getModelTranslation = (obj) => {
  if (
    !(obj?.model?.translation?.length || obj.model.translation.length !== 3)
  ) {
    throw new Error("model translation is passed incorrectly");
  }
  return obj.model.translation;
};

/**
 * returns rotation camera array from scene object
 * @param {Object} obj
 * @returns {[number,number,number]} array of x y z rotations
 */
const getCameraRotation = (obj) => {
  if (!(obj?.camera?.rotation?.length || obj.camera.rotation.length !== 3)) {
    throw new Error("camera rotation is passed incorrectly");
  }
  return obj.camera.rotation;
};

/**
 * returns translation array from scene object
 * @param {Object} obj
 * @returns {[number,number,number]} array of x y z translates
 */
const getCameraTranslation = (obj) => {
  if (
    !(obj?.camera?.translation?.length || obj.camera.translation.length !== 3)
  ) {
    throw new Error("camera translation is passed incorrectly");
  }
  return obj.camera.translation;
};

/**
 * returns perspective array from scene object
 * @param {Object} obj
 * @returns {number} perspective factor
 */
const getCameraPerspective = (obj) => {
  if (!obj?.camera?.perspective) {
    throw new Error("camera perspective is passed incorrectly");
  }
  return obj.camera.perspective;
};

/**
 * renders to a canvas
 * @param {Object} obj object containing scene infos
 * @param {number} width width of the canvas
 * @param {number} height height of the canvas
 * @param {CanvasRenderingContext2D} ctx drawing canvas
 */
export const render = (obj, width, height, ctx) => {
  // convert point to NNP array
  const scenePoints = getPointMatrix(obj);
  const sceneTriangles = getTriangleArr(obj);
  ctx.clearRect(0, 0, width, height);

  // initiate pipline
  console.clear();
  console.log("00 scene matrix");
  scenePoints.log();

  // scale
  console.log("01 Scaling");
  const scaleMat = Transform.scale(...getModelScale(obj));
  scaleMat.log();

  // rotate
  console.log("02 Rotation");
  const rotateMat = Transform.rotate(...getModelRotation(obj));

  // translation
  console.log("03 Translate");
  const translateMat = Transform.translate(...getModelTranslation(obj));

  console.log("04 model matrix");
  const modelMatrix = NNP.matmul(translateMat, NNP.matmul(rotateMat, scaleMat));
  modelMatrix.log();

  // world to camera
  console.log("04 World to camera");
  const xyzT = getCameraTranslation(obj);
  // invert coords translate
  xyzT[0] *= -1;
  xyzT[1] *= -1;
  xyzT[2] *= -1;

  const xyzR = getCameraRotation(obj);
  // invert coords rotate
  xyzR[0] *= -1;
  xyzR[1] *= -1;
  xyzR[2] *= -1;

  const viewMatrix = NNP.matmul(
    Transform.rotate(...xyzR),
    Transform.translate(...xyzT)
  );
  viewMatrix.log();

  // perspective projection
  console.log("05 Perspective projection");
  const perspectiveMat = Transform.perspective(getCameraPerspective(obj));
  perspectiveMat.log();

  let finalMatrix = NNP.matmul(perspectiveMat, viewMatrix);
  finalMatrix = NNP.matmul(finalMatrix, modelMatrix);
  const transformedPoints = NNP.matmul(finalMatrix, scenePoints);
  console.log("05.5 Final points");

  // normalize W
  console.log("06 Normalize W");
  const pointData = transformedPoints.toArray();
  for (let i = 0; i < pointData[0].length; i++) {
    const w = pointData[3][i];
    pointData[0][i] /= w;
    pointData[1][i] /= w;
    pointData[2][i] /= w;
    pointData[3][i] = 1;
  }
  const normalizedPoints = NNP.array(pointData);
  normalizedPoints.log();

  // to screen space
  console.log("07 To screen space");
  const screenPointsH = NNP.zeros(normalizedPoints.shape()[1], 2);
  const screenPoints = screenPointsH.toArray();

  const l = normalizedPoints.toArray()[0].length;
  for (let i = 0; i < l; i++) {
    const x = normalizedPoints.toArray()[0][i] + width / 2;
    const y = normalizedPoints.toArray()[1][i] + height / 2;
    screenPoints[i] = [x, y];
  }
  NNP.array(screenPoints).log();

  // render
  console.log("08 render");
  screenPoints.forEach((i) => {
    ctx.fillStyle = "orange";
    ctx.fillRect(i[0], i[1], 5, 5);
  });
  sceneTriangles.forEach((triangle) => {
    ctx.beginPath();
    ctx.moveTo(screenPoints[triangle[0]][0], screenPoints[triangle[0]][1]);
    ctx.lineTo(screenPoints[triangle[1]][0], screenPoints[triangle[1]][1]);
    ctx.lineTo(screenPoints[triangle[2]][0], screenPoints[triangle[2]][1]);
    ctx.closePath();
    ctx.stroke();
  });
};
