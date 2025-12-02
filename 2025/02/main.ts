import { readLines } from "../utils";

function isRepeated(id: string, n: number): boolean {
  if (id.length < n || id.length % n != 0) {
    return false;
  }

  for (let i = 0; i < id.length / n; i++) {
    for (let j = 0; j < n; j++) {
      if (id[j] != id[j + i * n]) {
        return false;
      }
    }
  }
  return true;
}

const input = await readLines();
const ranges = input[0].split(",");
let part1 = 0;
let part2 = 0;
for (const range of ranges) {
  const [start, end] = range.split("-").map((e) => parseInt(e));
  for (let i = start; i <= end; i++) {
    const id = i.toString();
    if (isRepeated(id, id.length / 2)) {
      part1 += i;
    }
    for (let n = 1; n <= id.length / 2; n++) {
      if (isRepeated(id, n)) {
        part2 += i;
        break;
      }
    }
  }
}

console.log("Part 1:", part1);
console.log("Part 2:", part2);
