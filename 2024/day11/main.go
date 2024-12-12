package main

import (
	"fmt"
	"strconv"
	"strings"

	"github.com/xlzior/aoc2024/utils"
)

func countDescendants(stone int, generations int) int {
	cache := make(map[[2]int]int)
	var helper func(int, int) int

	helper = func(s int, g int) int {
		cachedResult := cache[[2]int{s, g}]
		if cachedResult > 0 {
			return cachedResult
		}

		var result int
		if g == 0 {
			result = 1
		} else if s == 0 {
			result = helper(1, g-1)
		} else if len(fmt.Sprint(s))%2 == 0 {
			str := fmt.Sprint(s)
			l := len(str)
			a, _ := strconv.Atoi(str[:l/2])
			b, _ := strconv.Atoi(str[l/2:])
			result = helper(a, g-1) + helper(b, g-1)
		} else {
			result = helper(s*2024, g-1)
		}
		cache[[2]int{s, g}] = result
		return result
	}

	return helper(stone, generations)
}

func main() {
	line := utils.ReadLines()[0]

	part1 := 0
	part2 := 0
	for _, num := range strings.Split(line, " ") {
		n, _ := strconv.Atoi(num)
		part1 += countDescendants(n, 25)
		part2 += countDescendants(n, 75)
	}
	fmt.Println("Part 1:", part1)
	fmt.Println("Part 2:", part2)
}
