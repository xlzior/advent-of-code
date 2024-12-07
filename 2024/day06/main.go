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

type posdir struct {
	position  pair
	direction pair
}

func (p1 *pair) Add(p2 pair) pair {
	return pair{p1.r + p2.r, p1.c + p2.c}
}

func (p *pair) TurnRight() pair {
	return pair{p.c, -p.r}
}

func isWithinBounds(grid []string, p pair) bool {
	return p.r >= 0 && p.r < len(grid) && p.c >= 0 && p.c < len(grid[0])
}

func getCell(grid []string, p pair) string {
	if !isWithinBounds(grid, p) {
		return ""
	}
	return string(grid[p.r][p.c])
}

func findCells(grid []string, char string) []pair {
	results := make([]pair, 0)
	for r := 0; r < len(grid); r++ {
		for c := 0; c < len(grid[0]); c++ {
			if getCell(grid, pair{r, c}) == char {
				results = append(results, pair{r, c})
			}
		}
	}
	return results
}

func moveGuard(grid []string, curr pair, dir pair) (pair, pair) {
	next := curr.Add(dir)
	if getCell(grid, next) == "#" {
		return moveGuard(grid, curr, dir.TurnRight())
	} else {
		return next, dir
	}
}

func replaceAtIndex(s string, k int, replacement rune) string {
	if k < 0 || k >= len(s) {
		return s
	}

	runes := []rune(s)
	runes[k] = replacement

	return string(runes)
}

func isLoop(grid []string, start pair) bool {
	curr := start
	dir := pair{-1, 0} // up

	visited := make(map[posdir]bool)

	for isWithinBounds(grid, curr) {
		curr, dir = moveGuard(grid, curr, dir)
		if visited[posdir{curr, dir}] {
			return true
		}
		visited[posdir{curr, dir}] = true
	}
	return false
}

func main() {
	filename := os.Args[1]
	data, _ := os.ReadFile(filename)
	grid := strings.Split(string(data), "\n")

	visited := make(map[pair]bool)
	dir := pair{-1, 0} // up

	start := findCells(grid, "^")[0]
	curr := start

	for isWithinBounds(grid, curr) {
		visited[curr] = true
		curr, dir = moveGuard(grid, curr, dir)
	}
	fmt.Println("Part 1:", len(visited))

	loopCandidates := make(map[pair]bool)
	for candidate := range visited {
		if candidate == start {
			continue
		}
		newGrid := make([]string, len(grid))
		copy(newGrid, grid)
		newGrid[candidate.r] = replaceAtIndex(newGrid[candidate.r], candidate.c, '#')
		if isLoop(newGrid, start) {
			loopCandidates[candidate] = true
		}
	}
	fmt.Println("Part 2:", len(loopCandidates))
}
