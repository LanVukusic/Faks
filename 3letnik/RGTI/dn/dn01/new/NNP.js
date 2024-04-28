"use strict";
import { Matrix } from "./Matrix.js";

/**
 * Internal matrix array representation
 */
export class NNPArray {
  /**
   * @type {Array<Array<number>>}
   */
  state = [];

  /**
   * creates a new not numpy array
   * @param {Array<Array<number>>} arr
   */
  constructor(arr) {
    this.state = arr;
  }

  /**
   * converts matrix to JS native 2d array
   * @returns {Array<Array<number>>} JS array representation of matrix
   */
  toArray() {
    return this.state;
  }

  /**
   * determines the shape of the matrix
   * @returns {[number, number]} shape in [height, width] form
   */
  shape() {
    return [this.state.length, this.state[0]?.length];
  }

  /**
   * prints out the matrix array
   */
  log() {
    // let out = "";
    // this.state.forEach((row) => {
    //   out += row.join(" ") + "\n";
    // });
    // console.log(out);
    console.table(this.state);
  }
}

/**
 * Not numpy, majkemi
 */
export class NNP {
  /**
   * creates a new nnp.array from 2d js array
   * @param {Array<Array<number>>} arr
   * @returns {NNPArray} nnp array representation of input
   */
  static array(arr) {
    return new NNPArray(arr);
  }

  /**
   * convert a unified Matrix to internal representation
   * @param {Matrix} mat
   */
  static fromMatrix(mat) {
    return NNP.array(mat.toArray());
  }

  /**
   * creates a zero filled matrix
   * @param {number} h height of matrix
   * @param {number} w width of matrix
   * @returns {NNPArray} nnp array filled with 0
   */
  static zeros(h, w) {
    const arrayBase = [];
    for (let i = 0; i < h; i++) {
      arrayBase.push([]);
      for (let j = 0; j < w; j++) {
        arrayBase[i].push(0);
      }
    }
    return new NNPArray(arrayBase);
  }

  /**
   * Multiply two matrices a X b
   * @param {NNPArray} X first matrix
   * @param {NNPArray} Y second matrix
   * @returns {NNPArray}
   */
  static matmul(X, Y) {
    const a = X.toArray();
    const b = Y.toArray();

    if (a[0].length !== b.length) {
      console.log(a, b);
      throw new Error("Matrix a and b are of incompatible shape");
    }
    const out = NNP.zeros(a.length, b[0].length).toArray();

    for (let i = 0; i < a.length; i++) {
      for (let j = 0; j < b[0].length; j++) {
        for (let k = 0; k < b.length; k++) {
          out[i][j] += a[i][k] * b[k][j];
        }
      }
    }
    return NNP.array(out);
  }
}

export class Transform {
  /**
   *
   * @param {number} x
   * @param {number} y
   * @param {number} z
  * @returns {NNPArray}

   */
  static scale = (x, y, z) => {
    return NNP.array([
      [x, 0, 0, 0],
      [0, y, 0, 0],
      [0, 0, z, 0],
      [0, 0, 0, 1],
    ]);
  };

  /**
   *
   * @param {number} x
   * @param {number} y
   * @param {number} z
   * @returns {NNPArray}
   */
  static translate = (x, y, z) => {
    return NNP.array([
      [1, 0, 0, x],
      [0, 1, 0, y],
      [0, 0, 1, z],
      [0, 0, 0, 1],
    ]);
  };

  /**
   *
   * @param {number} phi angle in radians
   * @returns {NNPArray} rotation matrix around X axis
   */
  static rotateX = (phi) => {
    return NNP.array([
      [1, 0, 0, 0],
      [0, Math.cos(phi), -Math.sin(phi), 0],
      [0, Math.sin(phi), Math.cos(phi), 0],
      [0, 0, 0, 1],
    ]);
  };

  /**
   *
   * @param {number} phi angle in radians
   * @returns {NNPArray} rotation matrix around Y axis
   */
  static rotateY = (phi) => {
    return NNP.array([
      [Math.cos(phi), 0, Math.sin(phi), 0],
      [0, 1, 0, 0],
      [-Math.sin(phi), 0, Math.cos(phi), 0],
      [0, 0, 0, 1],
    ]);
  };

  /**
   *
   * @param {number} phi angle in radians
   * @returns {NNPArray} rotation matrix around Z axis
   */
  static rotateZ = (phi) => {
    return NNP.array([
      [Math.cos(phi), -Math.sin(phi), 0, 0],
      [Math.sin(phi), Math.cos(phi), 0, 0],
      [0, 0, 1, 0],
      [0, 0, 0, 1],
    ]);
  };

  /**
   * rotate arround all axis
   * @param {number} x
   * @param {number} y
   * @param {number} z
   * @returns {NNPArray} rotation matrix around all axis
   */
  static rotate = (x, y, z) => {
    // cumulative rotation matrix
    return NNP.matmul(
      NNP.matmul(this.rotateX(x), this.rotateY(y)),
      this.rotateZ(z)
    );
  };

  /**
   *
   * @param {number} d  perspective factor
   * @returns {NNPArray} perspective projection matrix
   */
  static perspective = (d) => {
    return NNP.array([
      [1, 0, 0, 0],
      [0, 1, 0, 0],
      [0, 0, 1, 0],
      [0, 0, d, 0],
    ]);
  };
}
