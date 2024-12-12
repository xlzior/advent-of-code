package main

import (
	"fmt"

	"github.com/xlzior/aoc2024/utils"
)

func main() {
	lines := utils.ReadLines()
	grid := utils.Grid{Grid: lines}
	nsew := []utils.Pair{{0, 1}, {0, -1}, {1, 0}, {-1, 0}}

	todo := make(map[utils.Pair]bool)
	br := grid.GetBottomRight()
	for i := 0; i < br.R; i++ {
		for j := 0; j < br.C; j++ {
			todo[utils.Pair{R: i, C: j}] = true
		}
	}

	part1 := 0
	for len(todo) > 0 {
		for p := range todo {
			plant := grid.GetCell(p)
			area := 0
			perimeter := 0

			visited := make(map[utils.Pair]bool)
			neighbours := []utils.Pair{p}
			for len(neighbours) > 0 {
				curr := neighbours[0]
				neighbours = neighbours[1:]
				if grid.GetCell(curr) == plant && !visited[curr] {
					visited[curr] = true
					delete(todo, curr)
					area++
					for _, dir := range nsew {
						next := curr.Plus(dir)
						if !visited[next] {
							neighbours = append(neighbours, next)
						}
					}
				}
				if grid.GetCell(curr) != plant {
					perimeter++
				}
			}
			fmt.Println(string(plant), area, perimeter)
			part1 += area * perimeter
			break
		}
	}
	fmt.Println("Part 1:", part1)
}
