package main

import (
	"fmt"
	"math"
	"os"
	"regexp"

	"github.com/xlzior/aoc2024/utils"
)

func Destructure(slice []int, vars ...*int) {
	for i := 0; i < len(vars) && i < len(slice); i++ {
		*vars[i] = slice[i]
	}
}

func extractNums(s string, n int) []int {
	numsRegex := regexp.MustCompile(`-?\d+`)
	input := numsRegex.FindAllString(s, n)
	nums := make([]int, n)
	for i := range nums {
		nums[i] = utils.MustParseInt(input[i])
	}
	return nums
}

func solvePart1(robots [][2]utils.Pair, size utils.Pair) int {
	quadrants := make(map[utils.Pair][]utils.Pair)

	for _, robot := range robots {
		p := robot[0]
		v := robot[1]
		f := p.Plus(v.Times(100)).Mod(size).Plus(size).Mod(size)
		q := f.Times(2).Plus(utils.Pair{R: 1, C: 1}).Minus(size)
		if q.R != 0 && q.C != 0 {
			q.R = q.R / int(math.Abs(float64(q.R)))
			q.C = q.C / int(math.Abs(float64(q.C)))
			quadrants[q] = append(quadrants[q], f)
		}
	}
	part1 := 1
	for q := range quadrants {
		part1 *= len(quadrants[q])
	}
	return part1
}

func main() {
	lines := utils.ReadLines()
	size := utils.Pair{R: 7, C: 11}
	filename := os.Args[1]
	if filename == "puzzle.in" {
		size = utils.Pair{R: 103, C: 101}
	}

	robots := make([][2]utils.Pair, 0)
	for _, line := range lines {
		var px, py, vx, vy int
		Destructure(extractNums(line, 4), &px, &py, &vx, &vy)
		p := utils.Pair{R: py, C: px}
		v := utils.Pair{R: vy, C: vx}
		robots = append(robots, [2]utils.Pair{p, v})
	}
	part1 := solvePart1(robots, size)
	fmt.Println("Part 1:", part1)
}
