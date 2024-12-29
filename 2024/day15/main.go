package main

import (
	"fmt"
	"strings"

	"github.com/xlzior/aoc2024/utils"
)

var dirs = map[rune]utils.Pair{
	'>': {R: 0, C: 1},
	'<': {R: 0, C: -1},
	'^': {R: -1, C: 0},
	'v': {R: 1, C: 0},
}

func canPushBoxes(grid utils.Grid, box, dir utils.Pair) bool {
	next := box.Plus(dir)
	if grid.GetCell(next) == '#' { // wall
		return false
	} else if grid.GetCell(next) == 'O' { // box
		return canPushBoxes(grid, next, dir)
	} else { // empty space
		return true
	}
}

func pushBoxes(grid utils.Grid, box, dir utils.Pair) bool {
	next := box.Plus(dir)
	pushed := true

	if grid.GetCell(next) == '#' { // wall
		return false
	} else if grid.GetCell(next) == 'O' { // box
		pushed = pushBoxes(grid, next, dir)
	}
	if pushed {
		grid.SetCell(next, 'O')
		grid.SetCell(box, '.')
	}
	return pushed
}

func solvePart1(grid utils.Grid, instrs string) map[utils.Pair]bool {
	curr := grid.FindAllList('@')[0]
	for _, i := range instrs {
		next := curr.Plus(dirs[i])
		pushed := true
		if grid.GetCell(next) == '#' {
			continue
		} else if grid.GetCell(next) == 'O' {
			pushed = pushBoxes(grid, next, dirs[i])
		}
		if pushed {
			grid.SetCell(curr, '.')
			grid.SetCell(next, '@')
			curr = next
		}
	}
	return grid.FindAllSet('O')
}

func sumGpsCoordinates(boxes map[utils.Pair]bool) int {
	sum := 0
	for box := range boxes {
		sum += 100*box.R + box.C
	}
	return sum
}

func main() {
	sections := utils.ReadSections()
	grid := utils.Grid{Grid: strings.Split(sections[0], "\n")}
	instrs := strings.Replace(sections[1], "\n", "", -1)
	boxes1 := solvePart1(grid, instrs)
	fmt.Println("Part 1:", sumGpsCoordinates(boxes1))
}
