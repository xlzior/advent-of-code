package main

import (
	"fmt"
	"sort"
	"strconv"
	"strings"

	"github.com/xlzior/aoc2024/utils"
)

type connection struct {
	a  string
	op string
	b  string
}

var connections = make(map[string]connection)
var cache = make(map[string]int)

func get(name string) int {
	if value, exists := cache[name]; exists {
		return value
	}
	conn := connections[name]
	a := get(conn.a)
	b := get(conn.b)
	switch conn.op {
	case "AND":
		return a & b
	case "OR":
		return a | b
	case "XOR":
		return a ^ b
	}
	return 0
}

func main() {
	sections := utils.ReadSections()
	inits := strings.Split(sections[0], "\n")
	conns := strings.Split(sections[1], "\n")
	zs := make([]string, 0)

	for _, init := range inits {
		splitted := strings.Split(init, ": ")
		name := splitted[0]
		value := utils.MustParseInt(splitted[1])
		cache[name] = value
	}

	for _, conn := range conns {
		splitted := strings.Split(conn, " -> ")
		name := splitted[1]
		splitted = strings.Split(splitted[0], " ")
		a, op, b := splitted[0], splitted[1], splitted[2]
		connections[name] = connection{a, op, b}
		if strings.HasPrefix(name, "z") {
			zs = append(zs, name)
		}
	}
	sort.Strings(zs)
	bits := make([]string, 0)
	for i := len(zs) - 1; i >= 0; i-- {
		bits = append(bits, fmt.Sprint(get(zs[i])))
	}
	binary := strings.Join(bits, "")
	part1, _ := strconv.ParseInt(binary, 2, 64)
	fmt.Println("Part 1:", part1)
}
