import { readLines } from "../utils";

function isInside(points: number[][], point: number[]): boolean {
  let crossings = 0;
  for (let i = 0; i < points.length; i++) {
    const a = points[i];
    const b = points[(i + 1) % points.length];

    if (a[1] == b[1]) {
      // y-coordinates the same, horizontal line
      const minX = Math.min(a[0], b[0]);
      const maxX = Math.max(a[0], b[0]);
      const withinX = minX <= point[0] && point[0] <= maxX;
      if (withinX && a[1] == point[1]) {
        return true;
      }
    } else {
      // y-coordinates different, vertical line
      const minY = Math.min(a[1], b[1]);
      const maxY = Math.max(a[1], b[1]);
      // NOTE: half-open interval here is important
      const withinY = minY < point[1] && point[1] <= maxY;

      if (withinY && a[0] == point[0]) {
        // on the line
        return true;
      } else if (withinY && a[0] > point[0]) {
        crossings++;
      }
    }
  }
  return crossings % 2 === 1;
}

function getRectangleCorners(firstCorner: number[], secondCorner: number[]) {
  return [
    firstCorner,
    [firstCorner[0], secondCorner[1]],
    secondCorner,
    [secondCorner[0], firstCorner[1]],
  ];
}

function isRectangleInscribed(
  firstCorner: number[],
  secondCorner: number[],
  points: number[][],
): boolean {
  // make sure that all 4 corners of the rectangle are inside the polygon
  for (const [x, y] of getRectangleCorners(firstCorner, secondCorner)) {
    if (!isInside(points, [x, y])) {
      return false;
    }
  }
  // make sure that rectangle edges do not cross any polygon edges
  const rectanglePoints = getRectangleCorners(firstCorner, secondCorner);
  for (let i = 0; i < points.length; i++) {
    const p1 = points[i];
    const p2 = points[(i + 1) % points.length];
    for (let j = 0; j < 4; j++) {
      const r1 = rectanglePoints[j];
      const r2 = rectanglePoints[(j + 1) % 4];
      if (intersects(p1, p2, r1, r2)) {
        return false;
      }
    }
  }

  return true;
}

function intersects(a1: number[], a2: number[], b1: number[], b2: number[]) {
  // parallel
  if ((a1[0] == a2[0]) == (b1[0] == b2[0])) {
    return false;
  }
  let h1, h2, v1, v2;
  if (a1[0] == a2[0]) {
    // x-coordinates are the same, vertical line
    v1 = a1;
    v2 = a2;
    h1 = b1;
    h2 = b2;
  } else {
    v1 = b1;
    v2 = b2;
    h1 = a1;
    h2 = a2;
  }
  const vx = v1[0];
  const hy = h1[1];
  if (v1[1] > v2[1]) {
    const temp = v1;
    v1 = v2;
    v2 = temp;
  }
  if (h1[0] > h2[0]) {
    const temp = h1;
    h1 = h2;
    h2 = temp;
  }
  return h1[0] < vx && vx < h2[0] && v1[1] < hy && hy < v2[1];
}

const input = await readLines();

const points = input.map((line) => line.split(",").map(Number));
let part1 = 0;
let part2 = 0;
for (let i = 0; i < points.length; i++) {
  for (let j = i + 1; j < points.length; j++) {
    const a = points[i];
    const b = points[j];
    const width = Math.abs(a[0] - b[0]) + 1;
    const height = Math.abs(a[1] - b[1]) + 1;
    const area = height * width;
    part1 = Math.max(part1, area);
    if (isRectangleInscribed(a, b, points)) {
      part2 = Math.max(part2, area);
    }
  }
}

console.log("Part 1:", part1);
console.log("Part 2:", part2);
