import { readLines } from "../utils";

function applyOperation(nums: number[], op: string) {
  return nums.reduce((a, b) => op == "+" ? a + b : a * b);
}

function applyOperations(nums: number[][], ops: string[]) {
  return ops.map((op, i) => applyOperation(nums[i], op)).reduce((a, b) =>
    a + b
  );
}

const input = await readLines();

const operations = input[input.length - 1].trim().split(/\s+/);
const rawNums = input.slice(0, input.length - 1);
const rows = rawNums.map((row) =>
  row.trim().split(/\s+/).map((n) => parseInt(n))
);
const normal = rows[0].map((_, c) => rows.map((row) => row[c]));
const ceph = rawNums[0].split("").map((_, c) => rawNums.map((row) => row[c]))
  .map((splitted) => splitted.join("").trim()).map((n) => n == "" ? "\n" : n)
  .join(" ").split("\n").map((nums) =>
    nums.trim().split(" ").map((n) => parseInt(n))
  );

const part1 = applyOperations(normal, operations);
const part2 = applyOperations(ceph, operations);

console.log("Part 1:", part1);
console.log("Part 2:", part2);
