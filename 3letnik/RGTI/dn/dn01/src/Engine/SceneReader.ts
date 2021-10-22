import {
  Matrix,
  scaleXYZ,
  translateXYZ,
  transRotX,
  transRotY,
  transRotZ,
} from "./Matrix";

interface ITransform {
  rotation: [number, number, number];
  translation: [number, number, number];
  perspective?: number;
  scale?: [number, number, number];
}

export interface IScene {
  vertices: Array<number>;
  indices: Array<number>;
  camera: ITransform;
  model: ITransform;
}

export class Point {
  public x: number = 0;
  public y: number = 0;
  public z: number = 0;

  constructor(x: number, y: number, z: number) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  getVector() {
    return [this.x, this.y, this.z];
  }
}

class Triangle {
  public a: Point = new Point(0, 0, 0);
  public b: Point = new Point(0, 0, 0);
  public c: Point = new Point(0, 0, 0);

  public indexes: Array<number> = [];

  constructor(a: Point, b: Point, c: Point) {
    this.a = a;
    this.b = b;
    this.c = c;
  }

  toObjectMatrix(): Matrix {
    return new Matrix([
      this.a.getVector(),
      this.b.getVector(),
      this.b.getVector(),
    ]);
  }
}

export class SceneReader {
  public points: Array<Point> = [];
  public triangles: Array<Triangle> = [];
  public pointMatrix: Matrix = new Matrix([]);

  /**
   * reads json from string
   * @param json string, representing the scene
   */
  readFromJson(data: IScene): void {
    // process points
    for (let i = 0; i < data.vertices.length; i += 3) {
      // read 3 coordinates
      const x = data.vertices[i + 0];
      const y = data.vertices[i + 1];
      const z = data.vertices[i + 2];

      // create a point and push it
      console.log(x, y, z);

      this.points.push(new Point(x, y, z));
    }

    // process triangles
    for (let i = 0; i < data.indices.length; i += 3) {
      // read 3 points
      const a = this.points[data.indices[i + 0]];
      const b = this.points[data.indices[i + 1]];
      const c = this.points[data.indices[i + 2]];

      // create a triangle and push it
      const tempTringle = new Triangle(a, b, c);
      tempTringle.indexes = [
        data.indices[i + 0],
        data.indices[i + 1],
        data.indices[i + 2],
      ];
      this.triangles.push(tempTringle);
    }

    const a = new Matrix([4, this.points.length]);
    const matSource = a.toArray();

    this.points.forEach((point, i) => {
      matSource[0][i] = point.x;
      matSource[1][i] = point.y;
      matSource[2][i] = point.z;
      matSource[3][i] = 1;
    });
    this.pointMatrix = new Matrix(matSource);
  }

  rotate(x: number, y: number, z: number): void {
    this.pointMatrix = transRotX(x).matmul(this.pointMatrix);
    this.pointMatrix = transRotY(y).matmul(this.pointMatrix);
    this.pointMatrix = transRotZ(z).matmul(this.pointMatrix);
  }

  translate(x: number, y: number, z: number) {
    translateXYZ(x, y, z).debug("transifč mejtič debugič");
    this.pointMatrix = translateXYZ(x, y, z).matmul(this.pointMatrix);
  }

  scale(x: number, y: number, z: number) {
    scaleXYZ(x, y, z).debug("4, čeknem scale matrix");
    this.pointMatrix.debug("5, čeknem objekt za ga povecat");
    scaleXYZ(x, y, z).matmul(this.pointMatrix).debug("6, čeknem resultresult");
    this.pointMatrix = scaleXYZ(x, y, z).matmul(this.pointMatrix);
  }
}
