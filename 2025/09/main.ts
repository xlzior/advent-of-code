import { readLines } from "../utils";

const memo = new Map<string, boolean>();
function isInside(points: number[][], point: number[]): boolean {
  if (memo.has(point.toString())) {
    return memo.get(point.toString())!;
  }
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
        const result = true;
        memo.set(point.toString(), result);
        return result;
      }
    } else {
      // y-coordinates different, vertical line
      const minY = Math.min(a[1], b[1]);
      const maxY = Math.max(a[1], b[1]);
      // NOTE: half-open interval here is important
      const withinY = minY < point[1] && point[1] <= maxY;

      if (point[0] == 0 && point[1] == 3) {
        // console.log({ point, a, b, result: withinY && a[0] > point[0] });
      }
      if (withinY && a[0] == point[0]) {
        // on the line
        const result = true;
        memo.set(point.toString(), result);
        return result;
      } else if (withinY && a[0] > point[0]) {
        crossings++;
      }
    }
  }
  const result = crossings % 2 === 1;
  memo.set(point.toString(), result);
  return result;
}

function allInside(
  firstCorner: number[],
  secondCorner: number[],
  points: number[][],
): boolean {
  const minX = Math.min(firstCorner[0], secondCorner[0]);
  const minY = Math.min(firstCorner[1], secondCorner[1]);
  const maxX = Math.max(firstCorner[0], secondCorner[0]);
  const maxY = Math.max(firstCorner[1], secondCorner[1]);
  for (let x = minX; x <= maxX; x++) {
    for (let y = minY; y <= maxY; y++) {
      if (!isInside(points, [x, y])) {
        return false;
      }
    }
  }
  return true;
}

const input = await readLines();

const points = input.map((line) => line.split(",").map(Number));
let part1 = 0;
let part2 = 0;
for (let i = 0; i < points.length; i++) {
  for (let j = i + 1; j < points.length; j++) {
    console.log(i, j);
    const a = points[i];
    const b = points[j];
    const width = Math.abs(a[0] - b[0]) + 1;
    const height = Math.abs(a[1] - b[1]) + 1;
    const area = height * width;
    part1 = Math.max(part1, area);
    if (allInside(a, b, points)) {
      part2 = Math.max(part2, area);
    }
  }
}

console.log("Part 1:", part1);
console.log("Part 2:", part2);
