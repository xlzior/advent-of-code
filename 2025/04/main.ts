import { readLines } from "../utils";

let input = await readLines();
let grid = input.map((line) => line.split(""));
const height = grid.length;
const width = grid[0].length;

function getCell(grid: string[][], r: number, c: number): string {
  if (0 <= r && r < height && 0 <= c && c < width) {
    return grid[r][c];
  } else {
    return "";
  }
}

function isRemovable(grid: string[][], r: number, c: number): boolean {
  let count = 0;
  for (const dr of [-1, 0, 1]) {
    for (const dc of [-1, 0, 1]) {
      if (dr == 0 && dc == 0) {
        continue;
      }
      if (getCell(grid, r + dr, c + dc) == "@") {
        count++;
      }
    }
  }
  return count < 4;
}

function removeRolls(grid: string[][]): [string[][], number] {
  let numRemovableRolls = 0;
  const rollsToRemove = [];
  for (let r = 0; r < height; r++) {
    for (let c = 0; c < width; c++) {
      if (getCell(grid, r, c) == "@" && isRemovable(grid, r, c)) {
        numRemovableRolls++;
        rollsToRemove.push([r, c]);
      }
    }
  }
  for (const [r, c] of rollsToRemove) {
    grid[r][c] = ".";
  }
  return [grid, numRemovableRolls];
}

let numRollsRemoved;
let allNumRollsRemoved = [];
while (numRollsRemoved != 0) {
  [grid, numRollsRemoved] = removeRolls(grid);
  allNumRollsRemoved.push(numRollsRemoved);
}

console.log("Part 1:", allNumRollsRemoved[0]);
console.log("Part 2:", allNumRollsRemoved.reduce((a, b) => a + b));
