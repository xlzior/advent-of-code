package main

import (
	"fmt"
	"strings"

	"github.com/xlzior/aoc2024/utils"
)

var nums = []string{"789", "456", "123", " 0A"}
var dirs = []string{" ^A", "<v>"}
var numpad = make(map[string]utils.Pair)
var dirpad = make(map[string]utils.Pair)

func complexity(code string, sequence string) int {
	fmt.Println(len(sequence), utils.MustParseInt(code[:len(code)-1]))
	return len(sequence) * utils.MustParseInt(code[:len(code)-1])
}

type unit struct {
	condition func(utils.Pair) bool
	generator func(utils.Pair) string
}

var unitUp = unit{
	condition: func(p utils.Pair) bool { return p.R < 0 },
	generator: func(p utils.Pair) string { return strings.Repeat("^", -p.R) },
}
var unitDown = unit{
	condition: func(p utils.Pair) bool { return p.R > 0 },
	generator: func(p utils.Pair) string { return strings.Repeat("v", p.R) },
}
var unitLeft = unit{
	condition: func(p utils.Pair) bool { return p.C < 0 },
	generator: func(p utils.Pair) string { return strings.Repeat("<", -p.C) },
}
var unitRight = unit{
	condition: func(p utils.Pair) bool { return p.C > 0 },
	generator: func(p utils.Pair) string { return strings.Repeat(">", p.C) },
}
var numpadUnits = []unit{unitUp, unitRight, unitDown, unitLeft}
var dirpadUnits = []unit{unitRight, unitDown, unitLeft, unitUp}

func expandSingle(delta utils.Pair, units []unit) string {
	result := make([]string, 0)
	for _, u := range units {
		if u.condition(delta) {
			result = append(result, u.generator(delta))
		}
	}
	return strings.Join(result, "")
}

func expandSequence(pad map[string]utils.Pair, sequence string) string {
	result := make([]string, 0)
	sequence = "A" + sequence
	var units []unit
	if len(pad) == len(numpad) {
		units = numpadUnits
	} else {
		units = dirpadUnits
	}
	for i := 0; i < len(sequence)-1; i++ {
		c := pad[string(sequence[i])]
		n := pad[string(sequence[i+1])]
		delta := n.Minus(c)

		result = append(result, expandSingle(delta, units), "A")
	}
	return strings.Join(result, "")
}

var dirmap = map[rune]utils.Pair{
	'>': {R: 0, C: 1},
	'v': {R: 1, C: 0},
	'<': {R: 0, C: -1},
	'^': {R: -1, C: 0},
}

func contract(pad []string, sequence string, start utils.Pair) string {
	result := make([]string, 0)
	curr := start
	for _, r := range sequence {
		if r == 'A' {
			result = append(result, string(pad[curr.R][curr.C]))
		} else {
			d := dirmap[r]
			curr = curr.Plus(d)
		}
	}
	return strings.Join(result, "")
}

func getButtonSequence(code string) string {
	first := expandSequence(numpad, code)
	fmt.Println(first)
	second := expandSequence(dirpad, first)
	fmt.Println(second)
	third := expandSequence(dirpad, second)
	fmt.Println(third)
	return third
}

func main() {
	lines := utils.ReadLines()
	part1 := 0
	for r, row := range nums {
		for c, cell := range row {
			numpad[string(cell)] = utils.Pair{R: r, C: c}
		}
	}
	for r, row := range dirs {
		for c, cell := range row {
			dirpad[string(cell)] = utils.Pair{R: r, C: c}
		}
	}

	for _, code := range lines {
		seq := getButtonSequence(code)
		part1 += complexity(code, seq)
	}
	fmt.Println("Part 1:", part1)
}
