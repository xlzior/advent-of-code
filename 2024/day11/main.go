package main

import (
	"fmt"
	"strconv"
	"strings"

	"github.com/xlzior/aoc2024/utils"
)

var cache map[[2]int]int

func countDescendants(stone int, generations int) int {
	cachedResult := cache[[2]int{stone, generations}]
	if cachedResult > 0 {
		return cachedResult
	}

	var result int
	if generations == 0 {
		result = 1
	} else if stone == 0 {
		result = countDescendants(1, generations-1)
	} else if len(fmt.Sprint(stone))%2 == 0 {
		str := fmt.Sprint(stone)
		l := len(str)
		a, _ := strconv.Atoi(str[:l/2])
		b, _ := strconv.Atoi(str[l/2:])
		result = countDescendants(a, generations-1) + countDescendants(b, generations-1)
	} else {
		result = countDescendants(stone*2024, generations-1)
	}
	cache[[2]int{stone, generations}] = result
	return result
}

func main() {
	line := utils.ReadLines()[0]

	part1 := 0
	part2 := 0
	cache = make(map[[2]int]int)
	for _, num := range strings.Split(line, " ") {
		n, _ := strconv.Atoi(num)
		part1 += countDescendants(n, 25)
		part2 += countDescendants(n, 75)
	}
	fmt.Println("Part 1:", part1)
	fmt.Println("Part 2:", part2)
}
