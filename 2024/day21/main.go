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

func complexity(code string, sequence string) int {
	fmt.Println(code, len(sequence), utils.MustParseInt(code[:len(code)-1]), sequence)
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

func getShortest(strs []string) string {
	shortest := strs[0]
	for _, s := range strs {
		if len(s) < len(shortest) {
			shortest = s
		}
	}
	return shortest
}

func decompileSegment(pad map[string]utils.Pair, start, end string, n int) string {
	delta := pad[end].Minus(pad[start])
	perms := getPermutations(delta)

	candidates := []string{}
	for _, perm := range perms {
		if isIllegal(start, pad, perm) {
			continue
		}
		if n == 0 {
			candidates = append(candidates, perm)
		} else {
			candidates = append(candidates, decompileSequence(dirpad, perm, n-1))
		}
	}
	if len(candidates) == 1 {
		return candidates[0]
	} else {
		return getShortest(candidates)
	}
}

func decompileSequence(pad map[string]utils.Pair, code string, n int) string {
	result := []string{}
	code = "A" + code
	for i := 0; i < len(code)-1; i++ {
		curr := string(code[i])
		next := string(code[i+1])
		seq := decompileSegment(pad, curr, next, n)
		result = append(result, seq)
	}
	return strings.Join(result, "")
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
	for _, code := range lines {
		seq := decompileSequence(numpad, code, 2)
		part1 += complexity(code, seq)
	}
	fmt.Println("Part 1:", part1)
}
