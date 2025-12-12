import { readLines } from "../utils";

const sum = (a, b) => a + b;

const input = await readLines();
const sections = input.join("\n").split("\n\n");
const shapes = sections.slice(0, sections.length - 1).map((shape) =>
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
      size: blueprint.join("").split("").filter((c) => c == "#").length,
      count: shapeCounts[i],
    })),
  };
});

const part1 = problems.map(({ height, width, shapes }) => {
  const numPresents = shapes.map(({ count }) => count).reduce(sum);
  const num3x3Spaces = Math.floor(height / 3) * Math.floor(width / 3);

  if (numPresents <= num3x3Spaces) {
    return true;
  }
  const fillCount = shapes.map(({ count, size }) => count * size).reduce(sum);
  if (fillCount > height * width) {
    return false;
  }
  console.log({ height, width, shapes });
}).filter((x) => x).length;

console.log("Part 1:", part1, "/", problems.length);
