package main

import (
	"fmt"
	"os"
	"strings"
)

type pair struct {
	r int
	c int
}

func (p1 *pair) Plus(p2 pair) pair {
	return pair{p1.r + p2.r, p1.c + p2.c}
}

func (p1 *pair) Minus(p2 pair) pair {
	return pair{p1.r - p2.r, p1.c - p2.c}
}

func (p *pair) Times(n int) pair {
	return pair{p.r * n, p.c * n}
}

func (p *pair) Divide(n int) pair {
	return pair{p.r / n, p.c / n}
}

func isWithinBounds(bottomRight pair, p pair) bool {
	return p.r >= 0 && p.r < bottomRight.r && p.c >= 0 && p.c < bottomRight.c
}

func getCell(grid []string, p pair) rune {
	if !isWithinBounds(pair{len(grid), len(grid[0])}, p) {
		return '\u0000'
	}
	return rune(grid[p.r][p.c])
}

func findCells(grid []string, char rune) []pair {
	results := make([]pair, 0)
	for r := 0; r < len(grid); r++ {
		for c := 0; c < len(grid[0]); c++ {
			if getCell(grid, pair{r, c}) == char {
				results = append(results, pair{r, c})
			}
		}
	}
	return results
}

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
	filename := os.Args[1]
	data, _ := os.ReadFile(filename)
	grid := strings.Split(string(data), "\n")
	bottomRight := pair{len(grid), len(grid[0])}

	part1 := make(map[pair]bool)
	part2 := make(map[pair]bool)
	allChars := findUniqueCharacters(strings.Join(grid, ""))
	for r := range allChars {
		if r == '.' {
			continue
		}
		antennas := findCells(grid, r)
		for _, a := range antennas {
			for _, b := range antennas {
				if a == b {
					continue
				}
				var ab pair
				var node pair

				// Part 1
				ab = b.Minus(a)
				node = a.Minus(ab)
				if isWithinBounds(bottomRight, node) {
					part1[node] = true
				}
				node = b.Plus(ab)
				if isWithinBounds(bottomRight, node) {
					part1[node] = true
				}

				// Part 2
				ab = ab.Divide(gcd(ab.r, ab.c))
				node = a
				for isWithinBounds(bottomRight, node) {
					part2[node] = true
					node = node.Minus(ab)
				}
				node = b
				for isWithinBounds(bottomRight, node) {
					part2[node] = true
					node = node.Plus(ab)
				}
			}
		}
	}

	fmt.Println("Part 1:", len(part1))
	fmt.Println("Part 2:", len(part2))
}
