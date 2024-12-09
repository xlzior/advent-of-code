package main

import (
	"fmt"
	"strconv"

	"github.com/xlzior/aoc2024/utils"
)

func solvePart1(line string) int {
	part1 := 0
	i := 0
	left := 0
	right := len(line) - 1
	r, _ := strconv.Atoi(string(line[right]))
	for left < right {
		// consume left block
		l, _ := strconv.Atoi(string(line[left]))
		for l > 0 {
			part1 += left / 2 * i
			i++
			l--
		}
		left++

		// consume right block until left free space gone
		l, _ = strconv.Atoi(string(line[left]))
		for l > 0 {
			part1 += right / 2 * i
			i++
			l--
			r--
			if r == 0 {
				// 1 for the right block, then skip the right free space
				right -= 2
				if right < left {
					// ran out of right blocks, excess left free space not used
					break
				}
				r, _ = strconv.Atoi(string(line[right]))
			}
		}
		left++
	}
	for r > 0 {
		part1 += right / 2 * i
		i++
		r--
	}
	return part1
}

func main() {
	line := utils.ReadLines()[0]

	part1 := solvePart1(line)
	fmt.Println("Part 1:", part1)
}
