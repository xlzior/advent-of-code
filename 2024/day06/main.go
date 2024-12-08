package main

import (
	"fmt"

	"github.com/xlzior/aoc2024/utils"
)

type posdir struct {
	position  utils.Pair
	direction utils.Pair
}

func moveGuard(obstacles map[utils.Pair]bool, curr utils.Pair, dir utils.Pair) (utils.Pair, utils.Pair) {
	next := curr.Plus(dir)
	if obstacles[next] {
		return moveGuard(obstacles, curr, dir.TurnRight())
	} else {
		return next, dir
	}
}

func isLoop(grid utils.Grid, obstacles map[utils.Pair]bool, curr utils.Pair, dir utils.Pair) bool {
	visited := make(map[posdir]bool)

	for grid.Contains(curr) {
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

func Solve(grid utils.Grid) {
	start := grid.FindAll('^')[0]
	obstacles := make(map[utils.Pair]bool)
	for _, obst := range grid.FindAll('#') {
		obstacles[obst] = true
	}

	visited := make(map[utils.Pair]bool)
	loopCandidates := make(map[utils.Pair]bool)

	curr := start
	dir := utils.Pair{R: -1, C: 0} // up

	for grid.Contains(curr) {
		visited[curr] = true
		next, nextDir := moveGuard(obstacles, curr, dir)

		if next != start && !visited[next] {
			obstacles[next] = true
			if isLoop(grid, obstacles, curr, dir) {
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
	lines := utils.ReadLines()
	grid := utils.Grid{Grid: lines}
	Solve(grid)
}
