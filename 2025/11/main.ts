import { readLines } from "../utils";

const sum = (a, b) => a + b;

const memo = new Map<string, number>();
function getWaysOut(node: string): number {
  if (memo.has(node)) {
    return memo.get(node)!;
  }
  let result = -1;
  if (node == "out") {
    result = 1;
  } else {
    result = adjList[node].map((neighbour) => getWaysOut(neighbour)).reduce(
      sum,
    );
  }
  memo.set(node, result);
  return result;
}

const input = await readLines();
const adjList = Object.fromEntries(input.map((line) => {
  const [node, neighbours] = line.split(": ");
  return [node, neighbours.split(" ")];
}));

const part1 = getWaysOut("you");
console.log("Part 1:", part1);
