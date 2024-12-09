package main

import (
	"fmt"
	"strconv"

	"github.com/xlzior/aoc2024/utils"
)

func SolvePart1(line string) int {
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

func parseInput(line string) ([][3]int, [][2]int) {
	blocks := make([][3]int, 0) // {index, id, length}
	spaces := make([][2]int, 0) // {index, length}
	j := 0
	for i := 0; i < len(line); i += 2 {
		blockSize, _ := strconv.Atoi(string(line[i]))
		blocks = append(blocks, [3]int{j, i / 2, blockSize})
		j += blockSize

		if i+1 < len(line) {
			spaceSize, _ := strconv.Atoi(string(line[i+1]))
			spaces = append(spaces, [2]int{j, spaceSize})
			j += spaceSize
		}
	}
	return blocks, spaces
}

func findFirstFreeSpace(spaces [][2]int, length int) int {
	for i, s := range spaces {
		if length <= s[1] {
			return i
		}
	}
	return -1
}

func SolvePart2(line string) int {
	blocks, spaces := parseInput(line)
	for i := len(blocks) - 1; i >= 0; i-- {
		s := findFirstFreeSpace(spaces, blocks[i][2])
		if s >= 0 && spaces[s][0] < blocks[i][0] {
			spaces[s][1] -= blocks[i][2] // decrement space left
			blocks[i][0] = spaces[s][0]  // set block start index
			spaces[s][0] += blocks[i][2] // increment space start index
		}
	}
	part2 := 0
	for _, block := range blocks {
		i, id, length := block[0], block[1], block[2]
		for j := 0; j < length; j++ {
			part2 += id * (i + j)
		}
	}
	return part2
}

func main() {
	line := utils.ReadLines()[0]

	part1 := SolvePart1(line)
	fmt.Println("Part 1:", part1)

	part2 := SolvePart2(line)
	fmt.Println("Part 2:", part2)
}
