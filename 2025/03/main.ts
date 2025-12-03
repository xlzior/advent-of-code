import { readLines } from "../utils";

class Key {
  private static pool = new Map<string, Key>();

  private constructor(public line: string, public n: number) {}

  static of(s: string, n: number): Key {
    const hash = `${s}:${n}`;
    if (!this.pool.has(hash)) {
      this.pool.set(hash, new Key(s, n));
    }
    return this.pool.get(hash)!;
  }
}

const memo = new Map<Key, number>();

function findLargestJoltage(line: string, n: number): number {
  const key = Key.of(line, n);
  if (memo.has(key)) {
    return memo.get(key)!;
  }

  let maxSeen = -1;
  if (n === 0) {
    maxSeen = 0;
  } else if (line.length === 0) {
    maxSeen = -1;
  } else if (line.length >= n) {
    // don't take the first number
    maxSeen = Math.max(maxSeen, findLargestJoltage(line.slice(1), n));

    // take the first number
    maxSeen = Math.max(
      maxSeen,
      parseInt(line[0]) * Math.pow(10, n - 1) +
        findLargestJoltage(line.slice(1), n - 1),
    );
  }
  memo.set(key, maxSeen);
  return maxSeen;
}

const input = await readLines();
const part1 = input.map((line) => findLargestJoltage(line, 2)).reduce((a, b) =>
  a + b
);
const part2 = input.map((line) => findLargestJoltage(line, 12)).reduce((a, b) =>
  a + b
);
console.log("Part 1:", part1);
console.log("Part 2:", part2);
