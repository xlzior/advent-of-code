package main

import (
	"fmt"
	"os"
	"sort"

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

type cheat struct {
	start utils.Pair
	curr  utils.Pair
	time  int
}

func countCheats(grid utils.Grid, path map[utils.Pair]int, cheatTime, threshold int) map[[2]utils.Pair]int {
	cheats := make(map[[2]utils.Pair]int)
	todo := make([]cheat, 0)
	bestVisit := map[[2]utils.Pair]int{}

	// initialise todo; every cell on the path is a potential starting point
	for curr := range path {
		for _, dir := range utils.NSEW {
			next := curr.Plus(dir)
			ch := cheat{start: curr, curr: next, time: 1}
			todo = append(todo, ch)
			bestVisit[[2]utils.Pair{ch.start, ch.curr}] = ch.time
		}
	}
	for len(todo) > 0 {
		ch := todo[0]
		todo = todo[1:]

		// continue cheating
		if ch.time < cheatTime {
			for _, dir := range utils.NSEW {
				next := ch.curr.Plus(dir)
				ch := cheat{start: ch.start, curr: next, time: ch.time + 1}
				key := [2]utils.Pair{ch.start, ch.curr}
				lastVisitTime, exists := bestVisit[key]
				if grid.Contains(next) && (!exists || ch.time < lastVisitTime) {
					todo = append(todo, ch)
					bestVisit[key] = ch.time
				}
			}
		}
		// end the cheat
		if grid.GetCell(ch.curr) != '#' {
			savings := path[ch.start] - path[ch.curr] - ch.time
			key := [2]utils.Pair{ch.start, ch.curr}
			if savings >= threshold && savings > cheats[key] {
				cheats[key] = savings
			}
		}
	}
	return cheats
}

func summariseCheats(cheats map[[2]utils.Pair]int) {
	summary := make(map[int]int)
	keys := make([]int, 0)
	for ch := range cheats {
		if summary[cheats[ch]] == 0 {
			keys = append(keys, cheats[ch])
		}
		summary[cheats[ch]]++
	}
	sort.Ints(keys)
	for _, savings := range keys {
		fmt.Printf("There are %d cheats that save %d picoseconds.\n", summary[savings], savings)
	}
}

func main() {
	grid := utils.Grid{Grid: utils.ReadLines()}
	start := grid.FindAllList('S')[0]
	threshold1 := 1
	threshold2 := 50
	if os.Args[1] == "puzzle.in" {
		threshold1 = 100
		threshold2 = 100
	}

	path := findRegularPath(grid, start)
	part1 := countCheats(grid, path, 2, threshold1)
	// summariseCheats(part1)
	fmt.Println("Part 1:", len(part1))

	part2 := countCheats(grid, path, 20, threshold2)
	// summariseCheats(part2)
	fmt.Println("Part 2:", len(part2))
}
