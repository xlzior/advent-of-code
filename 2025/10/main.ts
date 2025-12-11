import { readLines } from "../utils";

function toNumber(array: boolean[]): number {
  return array.reduce((acc, bit) => (acc << 1) | Number(bit), 0);
}
function powerSet<T>(arr: T[]): T[][] {
  const result: T[][] = [[]];

  for (const item of arr) {
    // for each existing subset, clone it and add the new item
    const newSubsets = result.map((sub) => [...sub, item]);
    result.push(...newSubsets);
  }

  return result;
}

async function getInput() {
  const input = await readLines();
  const pattern =
    /\[(?<targetStr>[.#]+)\] (?<buttonsStr>(\([\d,]+\) )+)\{(?<joltageStr>([\d,])+)\}/;
  return input.map((line) => {
    const match = line.match(pattern);
    if (!match?.groups) {
      throw new Error(`could not pattern match line ${line}`);
    }
    const { targetStr, buttonsStr, joltageStr } = match.groups;
    const target = toNumber(targetStr.split("").map((c) => c == "#"));
    const buttons = buttonsStr.trim().split(" ").map((buttonRaw) => {
      const line = Array.from(buttonRaw.matchAll(/\d+/g)).map((match) =>
        Number(match[0])
      );
      return Array.from({ length: targetStr.length }).map((_, i) =>
        line.includes(i)
      );
    });
    const joltage = joltageStr.split(",").map(Number);
    return { target, buttons, joltage };
  });
}

const lines = await getInput();
const part1 = lines.map(({ target, buttons }) => {
  const allSubsets = powerSet(buttons.map(toNumber)).sort((a, b) =>
    a.length - b.length
  );
  const solution = allSubsets.find((subset) =>
    subset.reduce((acc, curr) => acc ^ curr, 0) == target
  );
  return solution?.length!;
}).reduce((a, b) => a + b);
console.log("Part 1:", part1);

const part2 = lines.map(({ buttons, joltage }) => {
  return 0;
}).reduce((a, b) => a + b);
console.log("Part 2:", part2);
