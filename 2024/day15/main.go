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

type box struct {
	left  utils.Pair
	right utils.Pair
}

var boxes = make(map[utils.Pair]box)
var walls = make(map[utils.Pair]bool)

func pushBoxes1(grid utils.Grid, box, dir utils.Pair) bool {
	next := box.Plus(dir)
	pushed := true

	if grid.GetCell(next) == '#' { // wall
		return false
	} else if grid.GetCell(next) == 'O' { // box
		pushed = pushBoxes1(grid, next, dir)
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
			pushed = pushBoxes1(grid, next, dirs[i])
		}
		if pushed {
			grid.SetCell(curr, '.')
			grid.SetCell(next, '@')
			curr = next
		}
	}
	return grid.FindAllSet('O')
}

func getNexts(b box, dir utils.Pair) []utils.Pair {
	nexts := []utils.Pair{}
	switch dir {
	case dirs['>']:
		nexts = append(nexts, b.right.Plus(dir))
	case dirs['<']:
		nexts = append(nexts, b.left.Plus(dir))
	case dirs['^']:
		fallthrough
	case dirs['v']:
		nexts = append(nexts, b.left.Plus(dir))
		nexts = append(nexts, b.right.Plus(dir))
	}
	return nexts
}

func canPushBoxes2(
	boxes map[utils.Pair]box,
	walls map[utils.Pair]bool,
	b box,
	dir utils.Pair,
) bool {
	nexts := getNexts(b, dir)
	pushed := true
	for _, next := range nexts {
		if walls[next] {
			return false
		} else if b2, isBox := boxes[next]; isBox {
			pushed = pushed && canPushBoxes2(boxes, walls, b2, dir)
		}
	}
	return pushed
}

func pushBoxes2(
	boxes map[utils.Pair]box,
	walls map[utils.Pair]bool,
	b box,
	dir utils.Pair,
) bool {
	if !canPushBoxes2(boxes, walls, b, dir) {
		return false
	}

	nexts := getNexts(b, dir)
	pushed := true
	for _, next := range nexts {
		if walls[next] {
			return false
		} else if b2, isBox := boxes[next]; isBox {
			pushed = pushed && pushBoxes2(boxes, walls, b2, dir)
		}
	}
	if pushed {
		delete(boxes, b.left)
		delete(boxes, b.right)
		b.left = b.left.Plus(dir)
		b.right = b.right.Plus(dir)
		boxes[b.left] = b
		boxes[b.right] = b
	}
	return pushed
}

func solvePart2(grid utils.Grid, instrs string) map[utils.Pair]box {
	for b := range grid.FindAllSet('[') {
		boxObj := box{left: b, right: b.Plus(dirs['>'])}
		boxes[b] = boxObj
		boxes[b.Plus(dirs['>'])] = boxObj
	}
	for b := range grid.FindAllSet(']') {
		boxObj := box{left: b.Plus(dirs['<']), right: b}
		boxes[b] = boxObj
		boxes[b.Plus(dirs['<'])] = boxObj
	}
	walls = grid.FindAllSet('#')
	curr := grid.FindAllList('@')[0]
	for _, i := range instrs {
		next := curr.Plus(dirs[i])
		pushed := true
		if walls[next] {
			continue
		} else if box, isBox := boxes[next]; isBox {
			pushed = pushBoxes2(boxes, walls, box, dirs[i])
		}
		if pushed {
			curr = next
		}
	}
	return boxes
}

func sumGpsCoordinates1(boxes map[utils.Pair]bool) int {
	sum := 0
	for box := range boxes {
		sum += 100*box.R + box.C
	}
	return sum
}

func sumGpsCoordinates2(boxes map[utils.Pair]box) int {
	seen := map[box]bool{}
	sum := 0
	for coords := range boxes {
		b := boxes[coords]
		if !seen[b] {
			sum += 100*b.left.R + b.left.C
		}
		seen[b] = true
	}
	return sum
}

func expandGrid(str string) string {
	str = strings.Replace(str, "#", "##", -1)
	str = strings.Replace(str, "O", "[]", -1)
	str = strings.Replace(str, ".", "..", -1)
	str = strings.Replace(str, "@", "@.", -1)
	return str
}

func main() {
	sections := utils.ReadSections()
	instrs := strings.Replace(sections[1], "\n", "", -1)

	grid1 := utils.Grid{Grid: strings.Split(sections[0], "\n")}
	boxes1 := solvePart1(grid1, instrs)
	fmt.Println("Part 1:", sumGpsCoordinates1(boxes1))

	grid2 := utils.Grid{Grid: strings.Split(expandGrid(sections[0]), "\n")}
	boxes2 := solvePart2(grid2, instrs)
	fmt.Println("Part 2:", sumGpsCoordinates2(boxes2))
}
