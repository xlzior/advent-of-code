package main

import (
	"fmt"
	"os"
	"sort"
	"strconv"
	"strings"
)

type pair struct {
	r int
	c int
}

func parseRules(data []string) map[pair]bool {
	parsed := make(map[pair]bool, len(data))
	for _, rule := range data {
		pages := strings.Split(rule, "|")
		a, _ := strconv.Atoi(pages[0])
		b, _ := strconv.Atoi(pages[1])
		parsed[pair{a, b}] = true
	}
	return parsed
}

func parseUpdates(data []string) [][]int {
	updates := make([][]int, len(data))
	for i, rawUpdate := range data {
		pages := strings.Split(rawUpdate, ",")
		update := make([]int, len(pages))
		for j, page := range pages {
			p, _ := strconv.Atoi(page)
			update[j] = p
		}
		updates[i] = update
	}
	return updates
}

func isCorrect(rules map[pair]bool, update []int) bool {
	for i := 0; i < len(update); i++ {
		for j := i + 1; j < len(update); j++ {
			if rules[pair{update[j], update[i]}] {
				return false
			}
		}
	}
	return true
}

func reorder(rules map[pair]bool, update []int) []int {
	sorted := make([]int, len(update))
	copy(sorted, update)
	sort.Slice(sorted, func(i, j int) bool {
		return rules[pair{sorted[i], sorted[j]}]
	})
	return sorted
}

func main() {
	filename := os.Args[1]
	data, _ := os.ReadFile(filename)
	sections := strings.Split(string(data), "\n\n")
	rules := parseRules(strings.Split(sections[0], "\n"))
	updates := parseUpdates(strings.Split(sections[1], "\n"))

	part1 := 0
	part2 := 0
	for _, update := range updates {
		i := len(update) / 2
		if isCorrect(rules, update) {
			part1 += update[i]
		} else {
			reorderedUpdate := reorder(rules, update)
			part2 += reorderedUpdate[i]
		}
	}
	fmt.Println("Part 1:", part1)
	fmt.Println("Part 2:", part2)
}
