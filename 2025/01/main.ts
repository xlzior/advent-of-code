import { readLines } from "../utils";

function day01(input: string[]) {
  let part1 = 0;
  let part2 = 0;
  let curr = 50;

  for (const line of input) {
    const direction = line[0] == "R" ? 1 : -1;
    const steps = parseInt(line.substring(1));
    let next = curr + direction * steps;

    if (next % 100 == 0) {
      part1++;
    }
    for (let i = curr; i != next; i += direction) {
      if (i % 100 == 0) {
        part2++;
      }
    }
    curr = next;
  }

  return [part1, part2];
}

const input = await readLines();
const [part1, part2] = day01(input);
console.log("Part 1:", part1);
console.log("Part 2:", part2);
