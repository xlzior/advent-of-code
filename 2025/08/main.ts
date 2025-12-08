import { readLines } from "../utils";

function getDistance(a: number[], b: number[]) {
  const [ax, ay, az] = a;
  const [bx, by, bz] = b;
  return Math.pow(ax - bx, 2) + Math.pow(ay - by, 2) + Math.pow(az - bz, 2);
}

const input = await readLines();

const boxes = input.map((line) => line.split(",").map(Number));
const edges: [number[], number[]][] = [];
for (let i = 0; i < boxes.length; i++) {
  for (let j = i + 1; j < boxes.length; j++) {
    edges.push([boxes[i], boxes[j]]);
  }
}

const groups = new Map<string, number>();
boxes.forEach((box, i) => groups.set(box.toString(), i + 1));
edges.sort((edge1, edge2) =>
  getDistance(edge1[0], edge1[1]) - getDistance(edge2[0], edge2[1])
);
edges.forEach((edge, i) => {
  if (i > 1000) return;
  const [a, b] = edge;
  const aGroup = groups.get(a.toString())!;
  const bGroup = groups.get(b.toString())!;
  if (aGroup != bGroup) {
    groups.forEach((value, key) => {
      if (value == bGroup) {
        groups.set(key, aGroup);
      }
    });
  }
});

const groupSizes = new Map<number, number>();

groups.forEach((group, _) => {
  groupSizes.set(group, (groupSizes.get(group) || 0) + 1);
});
const groupSizesList = [...groupSizes.values()];
groupSizesList.sort((a, b) => b - a);
console.log("Part 1:", groupSizesList.slice(0, 3).reduce((a, b) => a * b));
