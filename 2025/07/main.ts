import { readLines } from "../utils";
import util from "node:util";

class Point {
  constructor(readonly row: number, readonly col: number) {
  }

  key() {
    return `(${this.row},${this.col})`;
  }

  [util.inspect.custom]() {
    return this.key;
  }

  public add(other: Point): Point {
    return new Point(this.row + other.row, this.col + other.col);
  }
}

const down = new Point(1, 0);
const left = new Point(0, -1);
const right = new Point(0, 1);

class Manifold {
  height: number;
  start: Point;
  splitters: Set<string>;
  memo: Map<string, number>;

  constructor(input: string[]) {
    this.height = input.length;
    const startCol = input[0].indexOf("S");
    this.start = new Point(0, startCol);
    this.splitters = new Set<string>();
    this.memo = new Map<string, number>();

    input.forEach((row, r) => {
      row.split("").forEach((col, c) => {
        if (col == "^") {
          this.splitters.add(new Point(r, c).key());
        }
      });
    });
  }

  countSplits() {
    let numSplits = 0;
    const visited = new Set<string>();
    const stack = [this.start];

    const addToStack = (beam: Point) => {
      if (visited.has(beam.key()) || beam.row > this.height) {
        return;
      }
      visited.add(beam.key());
      stack.push(beam);
    };
    while (stack.length > 0) {
      const beam = stack.pop()!;
      const belowBeam = beam.add(down);
      if (this.splitters.has(belowBeam.key())) {
        numSplits++;
        for (const dir of [left, right]) {
          addToStack(belowBeam.add(dir));
        }
      } else {
        addToStack(belowBeam);
      }
    }
    return numSplits;
  }

  countTimelines(curr: Point): number {
    if (curr.row > this.height) {
      return 1;
    } else if (this.memo.has(curr.key())) {
      return this.memo.get(curr.key())!;
    } else if (this.splitters.has(curr.key())) {
      const result = this.countTimelines(curr.add(left)) +
        this.countTimelines(curr.add(right));
      this.memo.set(curr.key(), result);
      return result;
    } else {
      const result = this.countTimelines(curr.add(down));
      this.memo.set(curr.key(), result);
      return result;
    }
  }
}

const input = await readLines();
const manifold = new Manifold(input);

console.log("Part 1:", manifold.countSplits());
console.log("Part 2:", manifold.countTimelines(manifold.start));
