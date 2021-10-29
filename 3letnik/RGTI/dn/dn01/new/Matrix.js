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

  /**
   * multiply two matrices and return the product
   * Multiply this, with matrixB from the right
   * @param {Matrix} matrixB Second matrix in matrix multiplication.
   * @returns {Matrix}
   */
  matmul(matrixB) {
    let a = this.m;
    let b = matrixB.toArray();

    if (a[0].length !== b.length) {
      console.log(a, b);
      throw new Error("Matrix a and b are of incompatible shape");
    }
    const out = new Matrix([a.length, b[0].length]).toArray();

    for (let i = 0; i < a.length; i++) {
      for (let j = 0; j < b[0].length; j++) {
        for (let k = 0; k < b.length; k++) {
          out[i][j] += a[i][k] * b[k][j];
        }
      }
    }
    return new Matrix(out);
  }

  /**
   *
   * @param {number} index of column
   * @returns {Array<number>} column as vector
   */
  colAsVector(index) {
    return this.m.map((i) => i[index]);
  }

  // STATICS
  // rotation matrixes
  /**
   *
   * @param {number} phi angle in radians
   * @returns {Matrix} rotation matrix around X axis
   */
  static rotateX = (phi) => {
    return new Matrix([
      [1, 0, 0, 0],
      [0, Math.cos(phi), -Math.sin(phi), 0],
      [0, Math.sin(phi), Math.cos(phi), 0],
      [0, 0, 0, 1],
    ]);
  };

  /**
   *
   * @param {number} phi angle in radians
   * @returns {Matrix} rotation matrix around Y axis
   */
  static rotateY = (phi) => {
    return new Matrix([
      [Math.cos(phi), 0, Math.sin(phi), 0],
      [0, 1, 0, 0],
      [-Math.sin(phi), 0, Math.cos(phi), 0],
      [0, 0, 0, 1],
    ]);
  };

  /**
   *
   * @param {number} phi angle in radians
   * @returns {Matrix} rotation matrix around Z axis
   */
  static rotateZ = (phi) => {
    return new Matrix([
      [Math.cos(phi), -Math.sin(phi), 0, 0],
      [Math.sin(phi), Math.cos(phi), 0, 0],
      [0, 0, 1, 0],
      [0, 0, 0, 1],
    ]);
  };

  /**
   *
   * @param {[number, number, number]} rot array of three angle in radians; X, Y, Z
   * @returns {Matrix} rotation matrix around X axis
   */
  static rotateXYZ = (rot) => {
    // cumulative rotation matrix
    return this.rotateX(rot[0])
      .matmul(this.rotateY(rot[1]))
      .matmul(this.rotateZ(rot[2]));
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
    return new Matrix([
      [1, 0, 0, x],
      [0, 1, 0, y],
      [0, 0, 1, z],
      [0, 0, 0, 1],
    ]);
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
    return new Matrix([
      [x, 0, 0, 0],
      [0, y, 0, 0],
      [0, 0, z, 0],
      [0, 0, 0, 1],
    ]);
  };

  /**
   * Returns a matrix that transforms points from world to camera vector space
   * @param { [number, number, number]} pos array of 3 translations
   * @param { [number, number, number]} rot array of 3 rotations in radians
   * @returns {Matrix} world to camera view transformation matrix
   */
  static worldToCam = (pos, rot) => {
    const posInverse = Matrix.translate(-pos[0], -pos[1], -pos[2]);
    const rotInverse = Matrix.rotateXYZ([-rot[0], -rot[1], -rot[2]]);

    const inverseTransform = rotInverse.matmul(posInverse);
    console.log("trans inv", inverseTransform);

    return inverseTransform;
  };
}
