import { readLines } from "../utils";

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
    const target = targetStr.split("").map((c) => c == "#" ? 1 : 0);
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

const sum = (a: number, b: number) => a + b;

function sumButtons(buttons: number[][], length: number): number[] {
  const initialState = Array.from({ length: length }).map((_) => 0);
  return buttons.reduce((state, button) => {
    return state.map((s, i) => s + button[i]);
  }, initialState);
}
function isEqual(A: number[], B: number[]): boolean {
  return A.length === B.length && A.every((a, i) => a == B[i]);
}
function powerSet<T>(arr: T[]): T[][] {
  const result: T[][] = [[]];

  for (const item of arr) {
    const newSubsets = result.map((sub) => [...sub, item]);
    result.push(...newSubsets);
  }

  return result;
}
function solvePart1(buttons: number[][], target: number[]): number[][][] {
  const result = [];
  const allSubsets = powerSet(buttons).sort((a, b) => a.length - b.length);
  for (const subset of allSubsets) {
    const finalState = sumButtons(subset, target.length).map((a) => a % 2);
    if (isEqual(finalState, target)) {
      result.push(subset);
    }
  }
  return result;
}

const memo = new Map<string, number>();
function solvePart2(buttons: number[][], joltage: number[]): number {
  if (joltage.some((a) => a < 0)) {
    return Infinity;
  }
  if (joltage.every((a) => a === 0)) {
    return 0;
  }

  const key = `${buttons.toString()}:${joltage.toString()}`;
  if (memo.has(key)) {
    return memo.get(key)!;
  }
  const subsets = solvePart1(buttons, joltage.map((a) => a % 2));
  let minButtonPresses = Infinity;
  for (const subset of subsets) {
    const firstSubproblem = sumButtons(subset, joltage.length);
    const secondSubproblem = joltage.map((a, i) =>
      (a - firstSubproblem[i]) / 2
    );
    const firstStep = subset.length;
    const secondStep = 2 * solvePart2(buttons, secondSubproblem);
    const numButtonPresses = firstStep + secondStep;
    minButtonPresses = Math.min(minButtonPresses, numButtonPresses);
  }
  const result = minButtonPresses;
  memo.set(key, result);
  return result;
}

const lines = await getInput();
const part1 = lines.map(({ target, buttons }) =>
  solvePart1(buttons, target).map((subset) => subset.length).reduce(
    (a, b) => Math.min(a, b),
  )
).reduce(sum);
console.log("Part 1:", part1);

const part2 = lines.map(({ buttons, joltage }, i) => {
  const result = solvePart2(buttons, joltage);
  return result;
})
  .reduce(sum);
console.log("Part 2:", part2);
