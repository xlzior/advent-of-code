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

func isWithinBounds(bottomRight pair, p pair) bool {
	return p.r >= 0 && p.r < bottomRight.r && p.c >= 0 && p.c < bottomRight.c
}

func getCell(grid []string, p pair) string {
	if !isWithinBounds(pair{len(grid), len(grid[0])}, p) {
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

func moveGuard(obstacles map[pair]bool, curr pair, dir pair) (pair, pair) {
	next := curr.Add(dir)
	if obstacles[next] {
		return moveGuard(obstacles, curr, dir.TurnRight())
	} else {
		return next, dir
	}
}

func isLoop(obstacles map[pair]bool, bottomRight pair, curr pair, dir pair) bool {
	visited := make(map[posdir]bool)

	for isWithinBounds(bottomRight, curr) {
		if visited[posdir{curr, dir}] {
			return true
		}
		next, nextDir := moveGuard(obstacles, curr, dir)
		if dir != nextDir {
			visited[posdir{curr, dir}] = true
		}
		curr, dir = next, nextDir
	}
	return false
}

func Solve(grid []string) {
	start := findCells(grid, "^")[0]
	obstacles := make(map[pair]bool)
	for _, obst := range findCells(grid, "#") {
		obstacles[obst] = true
	}
	bottomRight := pair{len(grid), len(grid[0])}

	visited := make(map[pair]bool)
	loopCandidates := make(map[pair]bool)

	curr := start
	dir := pair{-1, 0} // up

	for isWithinBounds(bottomRight, curr) {
		visited[curr] = true
		next, nextDir := moveGuard(obstacles, curr, dir)

		if next != start && !visited[next] {
			obstacles[next] = true
			if isLoop(obstacles, bottomRight, curr, dir) {
				loopCandidates[next] = true
			}
			obstacles[next] = false
		}

		curr, dir = next, nextDir
	}
	fmt.Println("Part 1:", len(visited))
	fmt.Println("Part 2:", len(loopCandidates))
}

func main() {
	filename := os.Args[1]
	data, _ := os.ReadFile(filename)
	grid := strings.Split(string(data), "\n")
	Solve(grid)
}
