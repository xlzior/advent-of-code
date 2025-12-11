import { equalTo, solve } from "yalps";
import { readLines } from "../utils";

function toNumber(array: number[]): number {
  return array.reduce((acc, bit) => (acc << 1) | bit, 0);
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
    const target = toNumber(targetStr.split("").map((c) => c == "#" ? 1 : 0));
    const buttons = buttonsStr.trim().split(" ").map((buttonRaw) => {
      const line = Array.from(buttonRaw.matchAll(/\d+/g)).map((match) =>
        Number(match[0])
      );
      return Array.from({ length: targetStr.length }).map((_, i) =>
        line.includes(i) ? 1 : 0
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
  const constraints = Object.fromEntries(
    joltage.map((jolt, i) => [`j${i}`, equalTo(jolt)]),
  );
  const variables = Object.fromEntries(buttons.map((button, i) => {
    return [`b${i}`, {
      sum: 1,
      ...Object.fromEntries(button.map((b, i) => [`j${i}`, b])),
    }];
  }));
  const solution = solve({
    direction: "minimize",
    objective: "sum",
    constraints: constraints,
    variables: variables,
    integers: Object.keys(variables),
  });
  return solution.variables.map((v) => v[1]).reduce((a, b) => a + b);
}).reduce((a, b) => a + b);
console.log("Part 2:", part2);
