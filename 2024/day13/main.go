package main

import (
	"fmt"
	"regexp"

	"github.com/xlzior/aoc2024/utils"
)

func divmod(a, b int) (int, int) {
	return a / b, a % b
}

func solve(ax, bx, tx, ay, by, ty int) (int, int, bool) {
	b, bmod := divmod(ty*ax-tx*ay, by*ax-bx*ay)
	a, amod := divmod(tx-b*bx, ax)
	solveable := amod == 0 && bmod == 0
	return a, b, solveable
}

func extractNums(s string) (int, int) {
	numsRegex := regexp.MustCompile(`\d+`)
	input := numsRegex.FindAllString(s, 2)
	first := utils.MustParseInt(input[0])
	second := utils.MustParseInt(input[1])
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
