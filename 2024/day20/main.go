package main

import (
	"fmt"
	"os"

	"github.com/xlzior/aoc2024/utils"
)

func findRegularPath(grid utils.Grid, start utils.Pair) map[utils.Pair]int {
	pathLength := len(grid.FindAllList('.')) + 1
	path := map[utils.Pair]int{start: pathLength}
	todo := []utils.Pair{start}
	i := 0
	for len(todo) > 0 {
		curr := todo[0]
		todo = todo[1:]
		path[curr] = pathLength - i
		i++
		for _, dir := range utils.NSEW {
			next := curr.Plus(dir)
			_, visited := path[next]
			if !visited && grid.GetCell(next) != '#' {
				todo = append(todo, next)
			}
		}
	}
	return path
}

func cheat(grid utils.Grid, path map[utils.Pair]int, threshold int) int {
	count := 0
	for curr := range path {
		for _, dir := range utils.NSEW {
			mid := curr.Plus(dir)
			dst := curr.Plus(dir.Times(2))
			if grid.GetCell(mid) == '#' && grid.GetCell(dst) != '#' && grid.Contains(dst) {
				savings := path[curr] - path[dst] - 2
				if savings >= threshold {
					count++
				}
			}
		}
	}
	return count
}

func main() {
	grid := utils.Grid{Grid: utils.ReadLines()}
	start := grid.FindAllList('S')[0]
	threshold := 0
	if os.Args[1] == "puzzle.in" {
		threshold = 100
	}

	path := findRegularPath(grid, start)
	part1 := cheat(grid, path, threshold)
	fmt.Println("Part 1:", part1)
}
