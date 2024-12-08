package main

import (
	"fmt"

	"github.com/xlzior/aoc2024/utils"
)

func containsXMAS(grid utils.Grid, start utils.Pair, dir utils.Pair) bool {
	return grid.GetCell(start.Plus(dir.Times(0))) == 'X' &&
		grid.GetCell(start.Plus(dir.Times(1))) == 'M' &&
		grid.GetCell(start.Plus(dir.Times(2))) == 'A' &&
		grid.GetCell(start.Plus(dir.Times(3))) == 'S'
}

func containsCrossMAS(grid utils.Grid, start utils.Pair, dir utils.Pair) bool {
	dir2 := utils.Pair{R: -dir.C, C: dir.R}
	return grid.GetCell(start) == 'A' &&
		grid.GetCell(start.Plus(dir.Times(1))) == 'M' &&
		grid.GetCell(start.Plus(dir.Times(-1))) == 'S' &&
		grid.GetCell(start.Plus(dir2.Times(1))) == 'M' &&
		grid.GetCell(start.Plus(dir2.Times(-1))) == 'S'
}

func main() {
	lines := utils.ReadLines()
	grid := utils.Grid{Grid: lines}

	var nsew = []utils.Pair{{0, 1}, {0, -1}, {1, 0}, {-1, 0}}
	var diagonals = []utils.Pair{{-1, 1}, {-1, -1}, {1, 1}, {1, -1}}
	var allDirections = []utils.Pair{}
	allDirections = append(allDirections, nsew...)
	allDirections = append(allDirections, diagonals...)

	part1 := 0
	part2 := 0
	for i := 0; i < grid.NumRows(); i++ {
		for j := 0; j < grid.NumCols(); j++ {
			for _, dir := range allDirections {
				if containsXMAS(grid, utils.Pair{R: i, C: j}, dir) {
					part1++
				}
			}
			for _, dir := range diagonals {
				if containsCrossMAS(grid, utils.Pair{R: i, C: j}, dir) {
					part2++
				}
			}
		}
	}
	fmt.Println("Part 1:", part1)
	fmt.Println("Part 2:", part2)
}
