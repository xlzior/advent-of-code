package main

import (
	"fmt"
	"strings"

	"github.com/xlzior/aoc2024/utils"
)

func checkSafety(report []int) bool {
	if report[0] == report[1] {
		return false
	}
	isIncreasing := report[0] < report[1]
	for i := 0; i < len(report)-1; i++ {
		diff := report[i+1] - report[i]
		if isIncreasing && (diff < 1 || diff > 3) {
			return false
		} else if !isIncreasing && (diff < -3 || diff > -1) {
			return false
		}
	}
	return true
}

func main() {
	lines := utils.ReadLines()
	reports := make([][]int, len(lines))

	for r, line := range lines {
		splitted := strings.Split(line, " ")
		levels := make([]int, len(splitted))
		reports[r] = levels
		for c, n := range splitted {
			level := utils.MustParseInt(n)
			reports[r][c] = level
		}
	}

	part1 := 0
	for _, report := range reports {
		if checkSafety(report) {
			part1++
		}
	}
	fmt.Println("Part 1:", part1)

	part2 := 0
	for _, report := range reports {
		isSafe := false
		for i := range report {
			dampened := make([]int, 0, len(report)-1)
			dampened = append(dampened, report[0:i]...)
			dampened = append(dampened, report[i+1:]...)
			if checkSafety(dampened) {
				isSafe = true
				break
			}
		}
		if isSafe {
			part2++
		}
	}
	fmt.Println("Part 2:", part2)
}
