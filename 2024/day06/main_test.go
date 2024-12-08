package main

import (
	"os"
	"strings"
	"testing"

	"github.com/xlzior/aoc2024/utils"
)

func Benchmark(b *testing.B) {
	data, _ := os.ReadFile("puzzle.in")
	grid := utils.Grid{Grid: strings.Split(string(data), "\n")}

	for i := 0; i < b.N; i++ {
		Solve(grid)
	}
}
