import { readLines } from "../utils";

function findLargestJoltage(line: string): number {
  let maxSeen = 0;
  for (let i = 0; i < line.length - 1; i++) {
    const largestOnesDigit = Math.max(
      ...line.slice(i + 1).split("").map((c) => parseInt(c)),
    );
    maxSeen = Math.max(
      maxSeen,
      parseInt(line[i]) * 10 + largestOnesDigit,
    );
  }
  return maxSeen;
}

const input = await readLines();

const part1 = input.map(findLargestJoltage).reduce((a, b) => a + b);
console.log("Part 1:", part1);
