package main

import (
	"fmt"

	"github.com/xlzior/aoc2024/utils"
)

func mix(a, b int) int {
	return a ^ b
}

func prune(a int) int {
	return a % 16777216
}

func evolve(n int) int {
	a := prune(mix(n, n*64))
	b := prune(mix(a, a/32))
	c := prune(mix(b, b*2048))
	return c
}

func computeSecrets(s, n int) []int {
	secrets := make([]int, 0, n)
	secrets = append(secrets, s)
	for i := 0; i < n; i++ {
		s = evolve(s)
		secrets = append(secrets, s)
	}
	return secrets
}

func part1(secrets [][]int) int {
	result := 0
	for _, monkey := range secrets {
		result += monkey[2000]
	}
	return result
}

func part2(secrets [][]int) int {
	bananas := make(map[[4]int]int)

	for _, monkey := range secrets {
		sequencesSeen := make(map[[4]int]bool)
		for i := 0; i < 2000-3; i++ {
			a, b, c, d, e := monkey[i]%10, monkey[i+1]%10, monkey[i+2]%10, monkey[i+3]%10, monkey[i+4]%10
			w, x, y, z := b-a, c-b, d-c, e-d
			key := [4]int{w, x, y, z}
			if !sequencesSeen[key] {
				bananas[key] += e
				sequencesSeen[key] = true
			}
		}
	}

	max := 0
	for key := range bananas {
		if bananas[key] >= max {
			max = bananas[key]
		}
	}
	return max
}

func main() {
	lines := utils.ReadLines()
	secrets := make([][]int, 0, len(lines))
	for _, line := range lines {
		n := utils.MustParseInt(line)
		secrets = append(secrets, computeSecrets(n, 2000))
	}

	fmt.Println("Part 1:", part1(secrets))
	fmt.Println("Part 2:", part2(secrets))
}
