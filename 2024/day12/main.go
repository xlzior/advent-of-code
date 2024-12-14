package main

import (
	"fmt"

	"github.com/xlzior/aoc2024/utils"
)

var nsew = []utils.Pair{{0, 1}, {0, -1}, {1, 0}, {-1, 0}}

func getAllCells(grid utils.Grid) map[utils.Pair]bool {
	todo := make(map[utils.Pair]bool)
	br := grid.GetBottomRight()
	for i := 0; i < br.R; i++ {
		for j := 0; j < br.C; j++ {
			todo[utils.Pair{R: i, C: j}] = true
		}
	}
	return todo
}

func head(m map[utils.Pair]bool) utils.Pair {
	for p := range m {
		return p
	}
	panic("map is empty")
}

func countConnectedComponents(nodes map[utils.Pair]bool) int {
	count := 0
	for len(nodes) > 0 {
		comp := []utils.Pair{head(nodes)}
		for len(comp) > 0 {
			curr := comp[0]
			comp = comp[1:]
			delete(nodes, curr)
			for _, dir := range nsew {
				next := curr.Plus(dir)
				if nodes[next] {
					comp = append(comp, next)
				}
			}
		}
		count++
	}
	return count
}

func solve(grid utils.Grid) (int, int) {
	todo := getAllCells(grid)

	part1 := 0
	part2 := 0
	for len(todo) > 0 {
		p := head(todo)
		plant := grid.GetCell(p)
		plot := make(map[utils.Pair]bool)
		perimeter := make(map[utils.Pair]map[utils.Pair]bool)

		neighbours := []utils.Pair{p}
		for len(neighbours) > 0 {
			curr := neighbours[0]
			neighbours = neighbours[1:]
			if grid.GetCell(curr) == plant && !plot[curr] {
				plot[curr] = true
				delete(todo, curr)
				for _, dir := range nsew {
					next := curr.Plus(dir)
					if grid.GetCell(next) != plant {
						if perimeter[dir] == nil {
							perimeter[dir] = make(map[utils.Pair]bool)
						}
						perimeter[dir][next] = true
					} else if !plot[next] {
						neighbours = append(neighbours, next)
					}
				}
			}
		}
		area := len(plot)
		fences := 0
		sides := 0
		for dir := range perimeter {
			fences += len(perimeter[dir])
			sides += countConnectedComponents(perimeter[dir])
		}
		part1 += area * fences
		part2 += area * sides
	}
	return part1, part2
}

func main() {
	lines := utils.ReadLines()
	grid := utils.Grid{Grid: lines}
	part1, part2 := solve(grid)
	fmt.Println("Part 1:", part1)
	fmt.Println("Part 2:", part2)
}
