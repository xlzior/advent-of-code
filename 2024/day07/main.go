package main

import (
	"fmt"
	"math"
	"strconv"
	"strings"

	"github.com/xlzior/aoc2024/utils"
)

func parseInput(lines []string) [][]int {
	parsed := make([][]int, 0, len(lines))
	for _, line := range lines {
		parsed = append(parsed, parseLine(line))
	}
	return parsed
}

func parseLine(line string) []int {
	rawNums := strings.Split(strings.Replace(line, ":", "", 1), " ")
	nums := make([]int, len(rawNums))
	for i, num := range rawNums {
		n, _ := strconv.Atoi(num)
		nums[i] = n
	}
	return nums
}

func add(a, b int) int {
	return a + b
}

func mul(a, b int) int {
	return a * b
}

func concat(a, b int) int {
	n := int(math.Pow10(len(fmt.Sprint(b))))
	return a*n + b
}

type Operation func(int, int) int

func evaluate(curr int, nums []int, target int, fns []Operation) bool {
	if curr > target {
		return false
	}

	if len(nums) == 0 {
		return curr == target
	}

	for _, fn := range fns {
		if evaluate(fn(curr, nums[0]), nums[1:], target, fns) {
			return true
		}
	}
	return false
}

func main() {
	lines := parseInput(utils.ReadLines())

	part1 := 0
	part2 := 0
	for _, line := range lines {
		target := line[0]
		first := line[1]
		rest := line[2:]
		if evaluate(first, rest, target, []Operation{add, mul}) {
			part1 += target
		}
		if evaluate(first, rest, target, []Operation{add, mul, concat}) {
			part2 += target
		}
	}
	fmt.Println("Part 1:", part1)
	fmt.Println("Part 2:", part2)
}
