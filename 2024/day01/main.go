package main

import (
	"fmt"
	"sort"
	"strings"

	"github.com/xlzior/aoc2024/utils"
)

func main() {
	lines := utils.ReadLines()
	left := make([]int, len(lines))
	right := make([]int, len(lines))

	for i, v := range lines {
		nums := strings.Split(v, "   ")
		left[i] = utils.MustParseInt(nums[0])
		right[i] = utils.MustParseInt(nums[1])
	}

	sort.Ints(left)
	sort.Ints(right)

	part1 := 0
	for i := range left {
		if left[i] > right[i] {
			part1 += left[i] - right[i]
		} else {
			part1 += right[i] - left[i]
		}
	}
	fmt.Println("Part 1:", part1)

	part2 := 0
	rightCounts := make(map[int]int)
	for _, r := range right {
		rightCounts[r]++
	}
	for _, l := range left {
		part2 += l * rightCounts[l]
	}
	fmt.Println("Part 2:", part2)
}
