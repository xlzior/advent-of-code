package main

import (
	"fmt"
	"strings"

	"github.com/xlzior/aoc2024/utils"
)

func findUniqueCharacters(s string) map[rune]bool {
	unique := make(map[rune]bool)
	for _, r := range s {
		unique[r] = true
	}
	return unique
}

func gcd(a, b int) int {
	for b != 0 {
		a, b = b, a%b
	}
	return a
}

func main() {
	lines := utils.ReadLines()
	grid := utils.Grid{Grid: lines}

	part1 := make(map[utils.Pair]bool)
	part2 := make(map[utils.Pair]bool)
	allChars := findUniqueCharacters(strings.Join(lines, ""))
	for r := range allChars {
		if r == '.' {
			continue
		}
		antennas := grid.FindAllList(r)
		for _, a := range antennas {
			for _, b := range antennas {
				if a == b {
					continue
				}
				var ab utils.Pair
				var node utils.Pair

				// Part 1
				ab = b.Minus(a)
				node = a.Minus(ab)
				if grid.Contains(node) {
					part1[node] = true
				}
				node = b.Plus(ab)
				if grid.Contains(node) {
					part1[node] = true
				}

				// Part 2
				ab = ab.Divide(gcd(ab.R, ab.C))
				node = a
				for grid.Contains(node) {
					part2[node] = true
					node = node.Minus(ab)
				}
				node = b
				for grid.Contains(node) {
					part2[node] = true
					node = node.Plus(ab)
				}
			}
		}
	}

	fmt.Println("Part 1:", len(part1))
	fmt.Println("Part 2:", len(part2))
}
