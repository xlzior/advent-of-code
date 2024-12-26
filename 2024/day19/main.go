package main

import (
	"fmt"
	"strings"

	"github.com/xlzior/aoc2024/utils"
)

var cache = make(map[string]int)

func countWays(target string, patterns []string) int {
	if value, exists := cache[target]; exists {
		return value
	}
	if len(target) == 0 {
		cache[target] = 1
		return 1
	}
	ways := 0
	for _, p := range patterns {
		if strings.HasPrefix(target, p) {
			ways += countWays(target[len(p):], patterns)
		}
	}
	cache[target] = ways
	return ways
}

func main() {
	sections := utils.ReadSections()
	patterns := strings.Split(sections[0], ", ")
	targets := strings.Split(sections[1], "\n")
	part1 := 0
	part2 := 0
	for _, target := range targets {
		ways := countWays(target, patterns)
		if ways > 0 {
			part1++
		}
		part2 += ways
	}
	fmt.Println("Part 1:", part1)
	fmt.Println("Part 2:", part2)
}
