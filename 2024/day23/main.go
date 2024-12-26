package main

import (
	"fmt"
	"sort"
	"strings"

	"github.com/xlzior/aoc2024/utils"
)

func getNodes(lines []string) map[string]bool {
	nodes := make(map[string]bool)
	for _, line := range lines {
		s := strings.Split(line, "-")
		a, b := s[0], s[1]
		nodes[a] = true
		nodes[b] = true
	}
	return nodes
}

func getEdgeList(lines []string) map[[2]string]bool {
	edges := make(map[[2]string]bool)
	for _, line := range lines {
		s := strings.Split(line, "-")
		a, b := s[0], s[1]
		edges[[2]string{a, b}] = true
		edges[[2]string{b, a}] = true
	}
	return edges
}

func getAdjacencyList(lines []string) map[string][]string {
	neighbours := make(map[string][]string)
	for _, line := range lines {
		s := strings.Split(line, "-")
		a, b := s[0], s[1]
		neighbours[a] = append(neighbours[a], b)
		neighbours[b] = append(neighbours[b], a)
	}
	return neighbours
}

func checkPrefix(nodes []string, prefix string) bool {
	for _, s := range nodes {
		if strings.HasPrefix(s, prefix) {
			return true
		}
	}
	return false
}

func part1(edges map[[2]string]bool, neighbours map[string][]string) int {
	result := 0
	for a := range neighbours {
		for _, b := range neighbours[a] {
			for _, c := range neighbours[a] {
				if edges[[2]string{b, c}] && checkPrefix([]string{a, b, c}, "t") {
					result++
				}
			}
		}
	}
	if result%6 != 0 {
		panic("count not divisible by 6")
	}
	return result / 6
}

func intersect[K comparable, V comparable](m map[K]V, n map[K]V) map[K]V {
	result := make(map[K]V)
	for k, v := range m {
		if n[k] == v {
			result[k] = v
		}
	}
	return result
}

func union[K comparable, V comparable](m map[K]V, n map[K]V) map[K]V {
	result := make(map[K]V)
	for k, v := range m {
		result[k] = v
	}
	for k, v := range n {
		result[k] = v
	}
	return result
}

func bronKerbosch(
	neighbours map[string][]string,
	r map[string]bool,
	p map[string]bool,
	x map[string]bool,
) map[string]bool {
	if len(p) == 0 && len(x) == 0 {
		return r
	}

	var largestClique map[string]bool
	for node := range p {
		v := map[string]bool{node: true}
		n := make(map[string]bool)
		for _, u := range neighbours[node] {
			n[u] = true
		}
		result := bronKerbosch(neighbours, union(r, v), intersect(p, n), intersect(x, n))
		if len(result) > len(largestClique) {
			largestClique = result
		}
		delete(p, node)
		x = union(x, v)
	}
	return largestClique
}

func mapToSlice(m map[string]bool) []string {
	slice := make([]string, 0, len(m))
	for k := range m {
		slice = append(slice, k)
	}
	return slice
}

func main() {
	lines := utils.ReadLines()
	nodes := getNodes(lines)
	edges := getEdgeList(lines)
	neighbours := getAdjacencyList(lines)
	fmt.Println("There are", len(neighbours), "nodes")
	fmt.Println("Part 1:", part1(edges, neighbours))

	part2 := mapToSlice(bronKerbosch(neighbours, map[string]bool{}, nodes, map[string]bool{}))
	sort.Strings(part2)
	fmt.Println("Part 2:", strings.Join(part2, ","))
}
