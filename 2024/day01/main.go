package main

import (
	"fmt"
	"os"
	"sort"
	"strconv"
	"strings"
)

func main() {
	filename := os.Args[1]
	data, _ := os.ReadFile(filename)
	lines := strings.Split(string(data), "\n")
	left := make([]int, 0)
	right := make([]int, 0)

	for _, v := range lines {
		nums := strings.Split(v, "   ")
		num1, _ := strconv.Atoi(nums[0])
		num2, _ := strconv.Atoi(nums[1])
		left = append(left, num1)
		right = append(right, num2)
	}

	sort.Sort(sort.IntSlice(left))
	sort.Sort(sort.IntSlice(right))

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
	for _, l := range left {
		count := 0
		for _, r := range right {
			if l == r {
				count++
			}
		}
		part2 += count * l
	}
	fmt.Println("Part 2:", part2)
}
