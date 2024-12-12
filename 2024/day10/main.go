package main

import (
	"fmt"

	"github.com/xlzior/aoc2024/utils"
)

func countTrails(grid utils.Grid, start utils.Pair) (int, int) {
	peaks := make(map[utils.Pair]bool)
	paths := 0
	var nsew = []utils.Pair{{0, 1}, {0, -1}, {1, 0}, {-1, 0}}
	var dfs func(utils.Pair)

	dfs = func(p1 utils.Pair) {
		curr := grid.GetCell(p1)
		if curr == '9' {
			peaks[p1] = true
			paths++
		}
		for _, dir := range nsew {
			p2 := p1.Plus(dir)
			next := grid.GetCell(p2)
			if next == curr+1 {
				dfs(p2)
			}
		}
	}

	dfs(start)
	return len(peaks), paths
}

func main() {
	lines := utils.ReadLines()
	grid := utils.Grid{Grid: lines}

	part1 := 0
	part2 := 0
	for _, start := range grid.FindAll('0') {
		p1, p2 := countTrails(grid, start)
		part1 += p1
		part2 += p2
	}
	fmt.Println("Part 1:", part1)
	fmt.Println("Part 2:", part2)
}
