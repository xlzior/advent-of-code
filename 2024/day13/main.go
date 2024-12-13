package main

import (
	"fmt"
	"regexp"
	"strconv"

	"github.com/xlzior/aoc2024/utils"
)

func solve(ax, bx, tx, ay, by, ty int) (int, int, bool) {
	numer := ty*ax - tx*ay
	denom := by*ax - bx*ay
	if numer%denom != 0 {
		return 0, 0, false
	}
	b := numer / denom

	numer = tx - b*bx
	denom = ax
	if numer%denom != 0 {
		return 0, 0, false
	}
	a := numer / denom

	return a, b, true
}

func extractNums(s string) (int, int) {
	numsRegex := regexp.MustCompile(`\d+`)
	input := numsRegex.FindAllString(s, 2)
	first, _ := strconv.Atoi(input[0])
	second, _ := strconv.Atoi(input[1])
	return first, second
}

func main() {
	lines := utils.ReadLines()

	part1 := 0
	part2 := 0
	offset := 10000000000000
	for i := 0; i < len(lines)+1; i += 4 {
		ax, ay := extractNums(lines[i])
		bx, by := extractNums(lines[i+1])
		tx, ty := extractNums(lines[i+2])

		a, b, solveable := solve(ax, bx, tx, ay, by, ty)
		if solveable {
			part1 += 3*a + b
		}
		a, b, solveable = solve(ax, bx, tx+offset, ay, by, ty+offset)
		if solveable {
			part2 += 3*a + b
		}
	}
	fmt.Println("Part 1:", part1)
	fmt.Println("Part 2:", part2)
}
