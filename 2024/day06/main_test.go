package main

import (
	"os"
	"strings"
	"testing"
)

func Benchmark(b *testing.B) {
	data, _ := os.ReadFile("puzzle.in")
	grid := strings.Split(string(data), "\n")

	for i := 0; i < b.N; i++ {
		Solve(grid)
	}
}
