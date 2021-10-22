// rotation matrixes
export const transRotX = (phi: number) => {
  return new Matrix([
    [1, 0, 0, 0],
    [0, Math.cos(phi), -Math.sin(phi), 0],
    [0, Math.sin(phi), Math.cos(phi), 0],
    [0, 0, 0, 1],
  ]);
};
export const transRotY = (phi: number) => {
  return new Matrix([
    [Math.cos(phi), 0, Math.sin(phi), 0],
    [0, 1, 0, 0],
    [-Math.sin(phi), 0, Math.cos(phi), 0],
    [0, 0, 0, 1],
  ]);
};
export const transRotZ = (phi: number) => {
  return new Matrix([
    [Math.cos(phi), -Math.sin(phi), 0, 0],
    [Math.sin(phi), Math.cos(phi), 0, 0],
    [0, 0, 1, 0],
    [0, 0, 0, 1],
  ]);
};

export const RotateXYZ = (rot: [number, number, number]) => {
  // cumulative rotation matrix
  return transRotX(rot[0]).matmul(transRotY(rot[1])).matmul(transRotZ(rot[2]));
};

// translate XYZ
export const translateXYZ = (x: number, y: number, z: number) => {
  return new Matrix([
    [1, 0, 0, x],
    [0, 1, 0, y],
    [0, 0, 1, z],
    [0, 0, 0, 1],
  ]);
};

// scaling matrix
export const scaleXYZ = (x: number, y: number, z: number) => {
  return new Matrix([
    [x, 0, 0, 0],
    [0, y, 0, 0],
    [0, 0, z, 0],
    [0, 0, 0, 1],
  ]);
};

// returns a scalar (dot) product from two arrays (vectors)
const scalarProd = (vec1: Array<number>, vec2: Array<number>): number => {
  return vec1.map((x, i) => vec1[i] * vec2[i]).reduce((a, b) => a + b);
};

// world to camera system
export const worldToCam = (
  pos: [number, number, number],
  rot: [number, number, number]
) => {
  // camera position _scalar_prod_ base vectors of world point
  const px = scalarProd(pos, [1, 0, 0]);
  const py = scalarProd(pos, [0, 1, 0]);
  const pz = scalarProd(pos, [0, 0, 1]);

  const mainBasis = new Matrix([
    [1, 0, 0, 1],
    [0, 1, 0, 1],
    [0, 0, -1, 1], // TOOODOOO NEVEM ČE JE TLE MINUS AL PLUS REALNO
    [0, 0, 0, 1],
  ]);

  // rotate the cameras own cord system based on camera rotation to get the bse vectors for the world to camera matrix
  const camBasis = RotateXYZ(rot).matmul(mainBasis);
  const vecx = camBasis.rowAsVector(0);
  const vecy = camBasis.rowAsVector(1);
  const vecz = camBasis.rowAsVector(2);

  return new Matrix([
    [vecx[0], vecy[0], vecz[0], -px],
    [vecx[1], vecy[1], vecz[1], -py],
    [vecx[2], vecy[2], vecz[2], -pz],
    [0, 0, 0, 1],
  ]);
};

// utils
export const rad2deg = (rad: number): number => {
  return rad / Math.PI;
};
export const deg2rad = (deg: number): number => {
  return (deg * Math.PI) / 180;
};

export class Matrix {
  private state: Array<Array<number>> = [];

  isNum(ins: Array<Array<number>> | [number, number]): ins is [number, number] {
    return (ins as Array<Array<number>>)[0]?.length === undefined;
  }

  constructor(ins: Array<Array<number>> | [number, number]) {
    if (this.isNum(ins)) {
      this.state = [];
      // h
      for (let xs = 0; xs < ins[0]; xs++) {
        // w
        this.state[xs] = [];
        for (let ys = 0; ys < ins[1]; ys++) {
          this.state[xs].push(0);
        }
      }
    } else {
      this.state = ins;
    }
  }

  toArray(): Array<Array<number>> {
    return this.state;
  }

  /**
   * Multiply matrices A and B, where a is this matrix and B is provided one
   * @param matrix is the second matrix in the multiplication.
   * @returns a new matrix, result of multiplication
   */
  matmul(matrix: Matrix): Matrix {
    let a = this.state;
    let b = matrix.toArray();

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

  // get a row as a vector
  rowAsVector(index: number): Array<number> {
    return this.state.map((i) => i[index]);
  }

  debug(msg?: string) {
    let str = msg ? `${msg} \n` : "";
    this.state.forEach((row) => {
      row.forEach((col, c) => {
        str += `${col} `;
      });
      str += "\n";
    });
    console.log(str);
  }
}
