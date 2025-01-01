package main

import (
	"fmt"
	"slices"
	"strings"

	"github.com/xlzior/aoc2024/utils"
)

var nums = []string{"789", "456", "123", " 0A"}
var dirs = []string{" ^A", "<v>"}
var numpad = make(map[string]utils.Pair)
var dirpad = make(map[string]utils.Pair)

func complexity(code string, length int) int {
	return length * utils.MustParseInt(code[:len(code)-1])
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

func isIllegal(start string, pad map[string]utils.Pair, sequence string) bool {
	illegal := pad[" "]
	curr := pad[start]
	for _, s := range sequence {
		if curr == illegal {
			return true
		}
		curr = curr.Plus(dirmap[s])
	}
	return curr == illegal
}

func getPermutations(delta utils.Pair) []string {
	segments := make([]string, 0)
	for _, u := range []unit{unitUp, unitRight, unitDown, unitLeft} {
		if u.condition(delta) {
			segments = append(segments, u.generator(delta))
		}
	}
	seq := strings.Join(segments, "") + "A"
	if len(segments) <= 1 {
		return []string{seq}
	} else if len(segments) == 2 {
		slices.Reverse(segments)
		return []string{seq, strings.Join(segments, "") + "A"}
	} else {
		panic("expected only 1 or 2 segments")
	}
}

type key struct {
	padlen int
	start  string
	end    string
	n      int
}

var cache = make(map[key]int)

func decompileSegment(pad map[string]utils.Pair, start, end string, n int) int {
	k := key{len(pad), start, end, n}
	if value, exists := cache[k]; exists {
		return value
	}

	delta := pad[end].Minus(pad[start])
	perms := getPermutations(delta)

	result := -1
	for _, perm := range perms {
		if isIllegal(start, pad, perm) {
			continue
		}
		length := 0
		if n == 0 {
			length = len(perm)
		} else {
			length = decompileSequence(dirpad, perm, n-1)
		}
		if result == -1 || length < result {
			result = length
		}
	}
	cache[k] = result
	return result
}

func decompileSequence(pad map[string]utils.Pair, code string, n int) int {
	result := 0
	code = "A" + code
	for i := 0; i < len(code)-1; i++ {
		curr := string(code[i])
		next := string(code[i+1])
		length := decompileSegment(pad, curr, next, n)
		result += length
	}
	return result
}

func main() {
	lines := utils.ReadLines()
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

	part1 := 0
	part2 := 0
	for _, code := range lines {
		part1 += complexity(code, decompileSequence(numpad, code, 2))
		part2 += complexity(code, decompileSequence(numpad, code, 25))
	}
	fmt.Println("Part 1:", part1)
	fmt.Println("Part 2:", part2)
}
