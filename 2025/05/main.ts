import { readLines } from "../utils";

const input = await readLines();
let part1 = 0;

class Range {
  start: number;
  end: number;
  constructor(start: number, end: number) {
    this.start = start;
    this.end = end;
  }
  public includes(n: number) {
    return this.start <= n && n <= this.end;
  }
  public overlaps(range: Range) {
    return this.start <= range.end && this.end >= range.start;
  }
  public size() {
    return this.end - this.start + 1;
  }
}

class RangeManager {
  ranges: Range[];
  constructor() {
    this.ranges = [];
  }

  public add(range: Range) {
    const overlaps: Range[] = [];
    for (const existing of this.ranges) {
      if (
        existing.includes(range.start) || existing.includes(range.end) ||
        range.includes(existing.start) || range.includes(existing.end)
      ) {
        overlaps.push(existing);
      }
    }
    if (overlaps.length > 0) {
      this.ranges = this.ranges.filter((range) => !overlaps.includes(range));
      overlaps.push(range);
      const start = Math.min(...overlaps.map((range) => range.start));
      const end = Math.max(...overlaps.map((range) => range.end));
      this.ranges.push(new Range(start, end));
    } else {
      this.ranges.push(range);
    }
  }
}

const manager = new RangeManager();
for (const line of input) {
  if (line.includes("-")) {
    const [start, end] = line.split("-");
    manager.add(new Range(parseInt(start), parseInt(end)));
  } else {
    const n = parseInt(line);
    for (const range of manager.ranges) {
      if (range.includes(n)) {
        part1++;
        break;
      }
    }
  }
}

const part2 = manager.ranges.map((range) => range.size()).reduce((a, b) =>
  a + b
);
console.log("Part 1:", part1);
console.log("Part 2:", part2);
