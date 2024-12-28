package main

import (
	"fmt"
	"math"
	"strconv"
	"strings"

	"github.com/xlzior/aoc2024/utils"
)

func parseInput() (int, int, int, []int) {
	lines := utils.ReadLines()
	a := utils.MustParseInt(strings.Split(lines[0], ": ")[1])
	b := utils.MustParseInt(strings.Split(lines[1], ": ")[1])
	c := utils.MustParseInt(strings.Split(lines[2], ": ")[1])
	prog := strings.Split(strings.Replace(lines[4], "Program: ", "", 1), ",")
	program := make([]int, len(prog))
	for i, p := range prog {
		program[i] = utils.MustParseInt(p)
	}
	return a, b, c, program
}

var a, b, c, instr int
var program []int

func combo(n int) int {
	if n <= 3 {
		return n
	} else if n == 4 {
		return a
	} else if n == 5 {
		return b
	} else if n == 6 {
		return c
	}
	return -1
}

func execute() (int, bool, bool) {
	op := program[instr]
	v := program[instr+1]
	switch op {
	case 0:
		a = a / int(math.Pow(2, float64(combo(v))))
		instr += 2
	case 1:
		b = b ^ v
		instr += 2
	case 2:
		b = combo(v) % 8
		instr += 2
	case 3:
		if a != 0 {
			instr = v
		} else {
			return 0, false, true
		}
	case 4:
		b = b ^ c
		instr += 2
	case 5:
		instr += 2
		out := combo(v) % 8
		return out, true, false
	case 6:
		b = a / int(math.Pow(2, float64(combo(v))))
		instr += 2
	case 7:
		c = a / int(math.Pow(2, float64(combo(v))))
		instr += 2
	}
	return 0, false, false
}

func runGenericProgram(ia, ib, ic, in int) string {
	a, b, c, instr = ia, ib, ic, in
	outputs := make([]string, 0)
	done := false
	for !done {
		v, isOutput, d := execute()
		done = d
		if isOutput {
			outputs = append(outputs, fmt.Sprint(v))
		}
	}
	return strings.Join(outputs, ",")
}

func hash(a int) int {
	result := 0
	for a != 0 {
		b = (a & 7) ^ 2
		c = a >> b
		result = result<<3 | (b^c^7)&7
		a = a >> 3
	}
	return result
}

func assertMatched(current, target int, n int) bool {
	return (current^target)&((1<<(n*3))-1) == 0
}

func pickLock(target int) int {
	hairpin := 0
	for i := 0; i < len(toOctal(target)); i++ {
		next := 0
		matched := false
		for !matched {
			h := hash(hairpin + next)
			if assertMatched(h, target, i+1) {
				hairpin += next
				matched = true
			} else {
				next += int(math.Pow(8, float64(15-i)))
			}
		}
	}
	return hairpin
}

func toOctal(n int) string {
	return strconv.FormatInt(int64(n), 8)
}

func main() {
	ia, ib, ic, ip := parseInput()
	program = ip
	part1 := runGenericProgram(ia, ib, ic, 0)
	fmt.Println("Part 1:", part1)
	fmt.Println("Verify:", toOctal(hash(ia)))

	target := 0
	for _, p := range program {
		target = target*8 + p
	}

	candidate := pickLock(target)
	fmt.Println("Part 2:", candidate)
	fmt.Println("Verify:", toOctal(hash(candidate)))
	fmt.Println("Verify:", program)
}
