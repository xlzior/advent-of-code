package main

import (
	"fmt"
	"strings"

	"github.com/xlzior/aoc2024/utils"
)

func parseInput() ([][5]int, [][5]int) {
	sections := utils.ReadSections()
	locks := make([][5]int, 0)
	keys := make([][5]int, 0)

	for _, s := range sections {
		sec := strings.Split(s, "\n")
		lock := [5]int{-1, -1, -1, -1, -1}
		key := [5]int{-1, -1, -1, -1, -1}
		for r, row := range sec {
			for c, cell := range row {
				if cell == '#' {
					lock[c] = r
					if key[c] < 0 {
						key[c] = 6 - r
					}
				}
			}
		}
		if sec[0] == "#####" {
			locks = append(locks, lock)
		} else {
			keys = append(keys, key)
		}
	}
	return locks, keys
}

func main() {
	locks, keys := parseInput()
	part1 := 0
	for _, lock := range locks {
		for _, key := range keys {
			fit := true
			for i := 0; i < 5; i++ {
				if lock[i]+key[i] > 5 {
					fit = false
				}
			}
			if fit {
				part1++
			}
		}
	}
	fmt.Println("Part 1:", part1)
}
