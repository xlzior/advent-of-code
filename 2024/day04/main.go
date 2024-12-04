package main

import (
	"fmt"
	"os"
	"strings"
)

type pair struct {
	r int
	c int
}

func (p1 *pair) Add(p2 pair) pair {
	return pair{p1.r + p2.r, p1.c + p2.c}
}

func (p1 *pair) Times(n int) pair {
	return pair{p1.r * n, p1.c * n}
}

func getCell(grid []string, coords pair) string {
	if coords.r < 0 || coords.r >= len(grid) ||
		coords.c < 0 || coords.c >= len(grid[0]) {
		return ""
	}

	return string(grid[coords.r][coords.c])
}

func containsXMAS(grid []string, start pair, dir pair) bool {
	return getCell(grid, start.Add(dir.Times(0))) == "X" &&
		getCell(grid, start.Add(dir.Times(1))) == "M" &&
		getCell(grid, start.Add(dir.Times(2))) == "A" &&
		getCell(grid, start.Add(dir.Times(3))) == "S"
}

func containsCrossMAS(grid []string, start pair, dir pair) bool {
	dir2 := pair{-dir.c, dir.r}
	return getCell(grid, start) == "A" &&
		getCell(grid, start.Add(dir.Times(1))) == "M" &&
		getCell(grid, start.Add(dir.Times(-1))) == "S" &&
		getCell(grid, start.Add(dir2.Times(1))) == "M" &&
		getCell(grid, start.Add(dir2.Times(-1))) == "S"
}

func main() {
	filename := os.Args[1]
	data, _ := os.ReadFile(filename)
	grid := strings.Split(string(data), "\n")

	var nsew = []pair{{0, 1}, {0, -1}, {1, 0}, {-1, 0}}
	var diagonals = []pair{{-1, 1}, {-1, -1}, {1, 1}, {1, -1}}
	var allDirections = []pair{}
	allDirections = append(allDirections, nsew...)
	allDirections = append(allDirections, diagonals...)

	part1 := 0
	part2 := 0
	for i := 0; i < len(grid); i++ {
		for j := 0; j < len(grid[0]); j++ {
			for _, dir := range allDirections {
				if containsXMAS(grid, pair{i, j}, dir) {
					part1++
				}
			}
			for _, dir := range diagonals {
				if containsCrossMAS(grid, pair{i, j}, dir) {
					part2++
				}
			}
		}
	}
	fmt.Println("Part 1:", part1)
	fmt.Println("Part 2:", part2)
}
