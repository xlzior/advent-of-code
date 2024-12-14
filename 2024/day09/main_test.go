package main

import (
	"os"
	"strings"
	"testing"
)

func Benchmark(b *testing.B) {
	// test cases from: https://www.reddit.com/r/adventofcode/comments/1haauty/2024_day_9_part_2_bonus_test_case_that_might_make/
	// u/Standard_Bar8402
	data, _ := os.ReadFile("4.in")
	line := strings.Split(string(data), "\n")[0]
	ints := stringToInts(line)

	for i := 0; i < b.N; i++ {
		solvePart2(ints)
	}
}
