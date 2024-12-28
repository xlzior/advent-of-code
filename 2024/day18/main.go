package main

import (
	"fmt"
	"os"
	"strings"

	"github.com/xlzior/aoc2024/utils"
)

var nsew = []utils.Pair{
	{R: 0, C: 1},
	{R: 0, C: -1},
	{R: 1, C: 0},
	{R: -1, C: 0},
}

type node struct {
	coord utils.Pair
	steps int
}

func isWithinBounds(p, topLeft, bottomRight utils.Pair) bool {
	return p.R >= topLeft.R && p.R <= bottomRight.R && p.C >= topLeft.C && p.C <= bottomRight.C
}

func bfs(obstacles map[utils.Pair]bool, start utils.Pair, end utils.Pair) int {
	visited := make(map[utils.Pair]bool)
	todo := []node{{coord: start, steps: 0}}
	for len(todo) > 0 {
		curr := todo[0]
		todo = todo[1:]
		if curr.coord == end {
			return curr.steps
		}
		for _, dir := range nsew {
			next := curr.coord.Plus(dir)
			if isWithinBounds(next, start, end) && !visited[next] && !obstacles[next] {
				visited[next] = true
				todo = append(todo, node{coord: next, steps: curr.steps + 1})
			}
		}
	}
	return -1
}

func toSet[A comparable](list []A) map[A]bool {
	set := make(map[A]bool)
	for _, l := range list {
		set[l] = true
	}
	return set
}

func binarySearch(
	obstacles []utils.Pair,
	start, end utils.Pair,
	low, high int,
) utils.Pair {
	var mid int
	for low < high {
		mid = (low + high) / 2
		steps := bfs(toSet(obstacles[:mid]), start, end)
		if steps > 0 { // path is not blocked, need more obstacles
			low = mid + 1
		} else { // path is blocked, need less obstacles
			high = mid
		}
	}
	return obstacles[high-1]
}

func main() {
	lines := utils.ReadLines()
	filename := os.Args[1]
	n := 12
	start := utils.Pair{R: 0, C: 0}
	end := utils.Pair{R: 6, C: 6}
	if filename == "puzzle.in" {
		n = 1024
		end = utils.Pair{R: 70, C: 70}
	}

	obstacles := make([]utils.Pair, len(lines))
	for i, line := range lines {
		splitted := strings.Split(line, ",")
		x := utils.MustParseInt(splitted[0])
		y := utils.MustParseInt(splitted[1])
		obstacles[i] = utils.Pair{R: y, C: x}
	}

	shortestPath := bfs(toSet(obstacles[:n]), start, end)
	fmt.Println("Part 1:", shortestPath)

	blockingObstacle := binarySearch(obstacles, start, end, n, len(obstacles))
	fmt.Printf("Part 2: %d,%d\n", blockingObstacle.C, blockingObstacle.R)
}
