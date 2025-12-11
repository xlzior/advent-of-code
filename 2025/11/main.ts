import { readLines } from "../utils";

const sum = (a: number, b: number) => a + b;
const product = (a: number, b: number) => a * b;

const memo = new Map<string, number>();
function countWays(node: string, target: string, avoid: string[] = []): number {
  const key = `${node}:${target}`;
  if (memo.has(key)) {
    return memo.get(key)!;
  }
  let result = -1;
  if (avoid.includes(node)) {
    result = -1;
  } else if (node == target) {
    result = 1;
  } else if (adjList[node]) {
    result = adjList[node].map((neighbour) => countWays(neighbour, target))
      .filter((numWays) => numWays > 0)
      .reduce(sum, 0);
  }
  memo.set(key, result);
  return result;
}

const input = await readLines();
const adjList = Object.fromEntries(input.map((line) => {
  const [node, neighbours] = line.split(": ");
  return [node, neighbours.split(" ")];
}));

if (adjList["you"]) {
  const part1 = countWays("you", "out");
  console.log("Part 1:", part1);
}

if (adjList["svr"]) {
  const paths = [
    ["svr", "dac", "fft", "out"],
    ["svr", "fft", "dac", "out"],
  ];
  const part2 = paths.map((path) => {
    const edgeWays = Array.from({ length: path.length - 1 }).map((_, i) => {
      const a = path[i];
      const b = path[i + 1];
      const avoid = path.filter((node) => node != a && node != b);
      return countWays(a, b, avoid);
    });
    if (edgeWays.some((numWays) => numWays == -1)) {
      return 0;
    } else {
      return edgeWays.reduce(product);
    }
  }).reduce(sum);
  console.log("Part 2:", part2);
}
