/**
 *class represeting a matrix
 */
export class Matrix {
  m = [];

  constructor(...m) {
    this.m = new Array(16).fill(0);
    this.m.splice(0, m.length, ...m);
  }

  /**
   * default func
   * @returns {Array<Array<number>>} array representing a matrix
   */
  toArray() {
    return [...this.m];
  }

  /**
   * default func
   * @returns {string} string representation of a flatened matrix
   */
  toString() {
    return `(${this.m.join(",")})`;
  }
  a;

  // STATICS
  // rotation matrixes
  /**
   *
   * @param {number} phi angle in radians
   * @returns {Matrix} rotation matrix around X axis
   */
  static rotateX = (phi) => {
    return new Matrix(
      [
        [1, 0, 0, 0],
        [0, Math.cos(phi), -Math.sin(phi), 0],
        [0, Math.sin(phi), Math.cos(phi), 0],
        [0, 0, 0, 1],
      ].flat()
    );
  };

  /**
   *
   * @param {number} phi angle in radians
   * @returns {Matrix} rotation matrix around Y axis
   */
  static rotateY = (phi) => {
    return new Matrix(
      [
        [Math.cos(phi), 0, Math.sin(phi), 0],
        [0, 1, 0, 0],
        [-Math.sin(phi), 0, Math.cos(phi), 0],
        [0, 0, 0, 1],
      ].flat()
    );
  };

  /**
   *
   * @param {number} phi angle in radians
   * @returns {Matrix} rotation matrix around Z axis
   */
  static rotateZ = (phi) => {
    return new Matrix(
      [
        [Math.cos(phi), -Math.sin(phi), 0, 0],
        [Math.sin(phi), Math.cos(phi), 0, 0],
        [0, 0, 1, 0],
        [0, 0, 0, 1],
      ].flat()
    );
  };

  // translate XYZ
  /**
   *
   * @param {number} x ammount to move object along X axis
   * @param {number} y ammount to move object along Y axis
   * @param {number} z ammount to move object along Z axis
   * @returns {Matrix} translation matrix
   */
  static translate = (x, y, z) => {
    return new Matrix(
      [
        [1, 0, 0, x],
        [0, 1, 0, y],
        [0, 0, 1, z],
        [0, 0, 0, 1],
      ].flat()
    );
  };

  // scaling matrix
  /**
   *
   * @param {number} y ammount to scale object along Y axis
   * @param {number} z ammount to scale object along Z axis
   * @param {number} x ammount to scale object along X axis
   * @returns {Matrix} scaling matrix
   */
  static scale = (x, y, z) => {
    return new Matrix(
      [
        [x, 0, 0, 0],
        [0, y, 0, 0],
        [0, 0, z, 0],
        [0, 0, 0, 1],
      ].flat()
    );
  };

  /**
   *
   * @param {number} d  perspective factor
   * @returns {Matrix} perspective projection matrix
   */
  static perspective = (d) => {
    return new Matrix(
      [
        [1, 0, 0, 0],
        [0, 1, 0, 0],
        [0, 0, 1, 0],
        [0, 0, d, 0],
      ].flat()
    );
  };
}
