package main

import (
	"fmt"
	"os"
	"regexp"
	"strconv"
	"strings"
)

func main() {
	filename := os.Args[1]
	data, _ := os.ReadFile(filename)
	s := string(data)
	mulRegex := regexp.MustCompile(`mul\((?P<a>\d+),(?P<b>\d+)\)`)
	instrRegex := regexp.MustCompile(`mul\((?P<a>\d+),(?P<b>\d+)\)|do\(\)|don't\(\)`)

	part1 := 0
	for _, match := range mulRegex.FindAllStringSubmatch(s, -1) {
		a, _ := strconv.Atoi(match[mulRegex.SubexpIndex("a")])
		b, _ := strconv.Atoi(match[mulRegex.SubexpIndex("b")])
		part1 += a * b
	}
	fmt.Println("Part 1:", part1)

	part2 := 0
	isOn := true
	for _, match := range instrRegex.FindAllStringSubmatch(s, -1) {
		if match[0] == "don't()" {
			isOn = false
		} else if match[0] == "do()" {
			isOn = true
		} else if strings.HasPrefix(match[0], "mul") && isOn {
			a, _ := strconv.Atoi(match[mulRegex.SubexpIndex("a")])
			b, _ := strconv.Atoi(match[mulRegex.SubexpIndex("b")])
			part2 += a * b
		}
	}
	fmt.Println("Part 2:", part2)
}
