import { readLines } from "../utils";

const input = await readLines();
const sections = input.join("\n").split("\n\n");
const shapes = sections.slice(0, sections.length - 2).map((shape) =>
  shape.split("\n").slice(1)
);
const problems = sections[sections.length - 1].split("\n").map((line) => {
  const match = Array.from(line.matchAll(/\d+/g)).map((match) =>
    Number(match[0])
  );
  const [height, width, ...shapeCounts] = match;
  return {
    height,
    width,
    shapes: shapes.map((blueprint, i) => ({
      blueprint,
      count: shapeCounts[i],
    })),
  };
});

const part1 = problems.map(({ height, width, shapes }) => {
  const numPresents = shapes.map(({ count }) => count).reduce((a, b) => a + b);
  const num3x3Spaces = Math.floor(height / 3) * Math.floor(width / 3);
  return numPresents <= num3x3Spaces;
}).filter((x) => x).length;

console.log("Part 1:", part1, "/", problems.length);
