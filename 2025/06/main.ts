import { readLines } from "../utils";

function applyOperation(nums: number[], op: string) {
  return nums.reduce((a, b) => op == "+" ? a + b : a * b);
}

function applyOperations(nums: number[][], ops: string[]) {
  return ops.map((op, i) => applyOperation(nums[i], op)).reduce((a, b) =>
    a + b
  );
}

function transpose(array: any[][]): any[][] {
  return array[0].map((_, c) => array.map((row) => row[c]));
}

const input = await readLines();
const operations = input[input.length - 1].trim().split(/\s+/);
const lines = input.slice(0, input.length - 1);

const normal = transpose(
  lines.map((row) => row.trim().split(/\s+/).map(Number)),
);
const ceph = transpose(lines.map((line) => line.split("")))
  .map((splitted) => splitted.join("").trim() || "\n")
  .join(" ").split("\n").map((nums) => nums.trim().split(" ").map(Number));

const part1 = applyOperations(normal, operations);
const part2 = applyOperations(ceph, operations);

console.log("Part 1:", part1);
console.log("Part 2:", part2);
