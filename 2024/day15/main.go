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

func pushBoxes(boxes, walls map[utils.Pair]bool, box, dir utils.Pair) ([]utils.Pair, []utils.Pair, bool) {
	next := box.Plus(dir)
	boxesPushed := []utils.Pair{box}
	newBoxes := []utils.Pair{next}
	blocked := false

	if walls[next] {
		return boxesPushed, newBoxes, true
	}
	if boxes[next] {
		old, new, b := pushBoxes(boxes, walls, next, dir)
		boxesPushed = append(boxesPushed, old...)
		newBoxes = append(newBoxes, new...)
		blocked = blocked || b
	}
	return boxesPushed, newBoxes, blocked
}

func printGrid(curr utils.Pair, grid utils.Grid, walls, boxes map[utils.Pair]bool) {
	for i := 0; i < grid.NumRows(); i++ {
		for j := 0; j < grid.NumCols(); j++ {
			p := utils.Pair{R: i, C: j}
			if p == curr {
				fmt.Print("@")
			} else if walls[p] {
				fmt.Print("#")
			} else if boxes[p] {
				fmt.Print("O")
			} else {
				fmt.Print(" ")
			}
		}
		fmt.Println()
	}
}

func solvePart1(grid utils.Grid, instrs string) map[utils.Pair]bool {
	curr := grid.FindAllList('@')[0]
	walls := grid.FindAllSet('#')
	boxes := grid.FindAllSet('O')
	for _, i := range instrs {
		next := curr.Plus(dirs[i])
		if walls[next] {
			continue
		} else if boxes[next] {
			boxesPushed, newBoxes, blocked := pushBoxes(boxes, walls, next, dirs[i])
			if blocked {
				continue
			} else {
				for _, box := range boxesPushed {
					delete(boxes, box)
				}
				for _, box := range newBoxes {
					boxes[box] = true
				}
				curr = next
			}
		} else { // empty space
			curr = next
		}
	}
	return boxes
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
