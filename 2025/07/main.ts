import { readLines } from "../utils";
import util from "node:util";

class Point {
  row: number;
  col: number;

  private static pool = new Map<string, Point>();
  private constructor(row: number, col: number) {
    this.row = row;
    this.col = col;
  }

  [util.inspect.custom]() {
    return `(${this.row},${this.col})`;
  }

  static of(row: number, col: number): Point {
    const hash = `${row}:${col}`;
    if (!this.pool.has(hash)) {
      this.pool.set(hash, new Point(row, col));
    }
    return this.pool.get(hash)!;
  }

  public add(other: Point): Point {
    return Point.of(this.row + other.row, this.col + other.col);
  }
}

const down = Point.of(1, 0);
const left = Point.of(0, -1);
const right = Point.of(0, 1);

class Manifold {
  height: number;
  start: Point;
  splitters: Set<Point>;
  memo: Map<Point, number>;

  constructor(input: string[]) {
    this.height = input.length;
    const startCol = input[0].indexOf("S");
    this.start = Point.of(0, startCol);
    this.splitters = new Set<Point>();
    this.memo = new Map<Point, number>();

    input.forEach((row, r) => {
      row.split("").forEach((col, c) => {
        if (col == "^") {
          this.splitters.add(Point.of(r, c));
        }
      });
    });
  }

  countSplits() {
    let numSplits = 0;
    const visited = new Set<Point>();
    const stack = [this.start];

    const addToStack = (beam: Point) => {
      if (visited.has(beam) || beam.row > this.height) {
        return;
      }
      visited.add(beam);
      stack.push(beam);
    };
    while (stack.length > 0) {
      const beam = stack.pop()!;
      const belowBeam = beam.add(down);
      if (this.splitters.has(belowBeam)) {
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
    } else if (this.memo.has(curr)) {
      return this.memo.get(curr)!;
    } else if (this.splitters.has(curr)) {
      const result = this.countTimelines(curr.add(left)) +
        this.countTimelines(curr.add(right));
      this.memo.set(curr, result);
      return result;
    } else {
      const result = this.countTimelines(curr.add(down));
      this.memo.set(curr, result);
      return result;
    }
  }
}

const input = await readLines();
const manifold = new Manifold(input);

console.log("Part 1:", manifold.countSplits());
console.log("Part 2:", manifold.countTimelines(manifold.start));
