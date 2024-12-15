package main

import (
	"fmt"
	"strings"

	"github.com/xlzior/aoc2024/utils"
)

func main() {
	sections := utils.ReadSections()
	grid := utils.Grid{Grid: strings.Split(sections[0], "\n")}
	instrs := strings.Replace(sections[1], "\n", "", -1)

	dirs := map[rune]utils.Pair{
		'>': {R: 0, C: 1},
		'<': {R: 0, C: -1},
		'^': {R: -1, C: 0},
		'v': {R: 1, C: 0},
	}

	curr := grid.FindAll('@')[0]
	grid.SetCell(curr, '.')
	for _, i := range instrs {
		next := curr.Plus(dirs[i])
		switch grid.GetCell(next) {
		case '#':
			continue
		case '.':
			curr = next
		case 'O':
			nextEmpty := next.Plus(dirs[i])
			for grid.GetCell(nextEmpty) == 'O' {
				nextEmpty = nextEmpty.Plus(dirs[i])
			}
			if grid.GetCell(nextEmpty) == '#' {
				continue
			} else { // empty space
				grid.SetCell(nextEmpty, 'O')
				grid.SetCell(next, '.')
				curr = next
			}
		}
	}

	part1 := 0
	for _, box := range grid.FindAll('O') {
		part1 += 100*box.R + box.C
	}
	fmt.Println("Part 1:", part1)
}
