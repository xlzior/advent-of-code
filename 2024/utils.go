package utils

import "fmt"

type pair struct {
	r int
	c int
}

func combinations(list []pair, n int) [][]pair {
	results := make([][]pair, 0)
	if len(list) < n {
		return results
	}

	if n == 0 {
		results = append(results, []pair{})
		return results
	}

	// combinations with the first item
	for _, combi := range combinations(list[1:], n-1) {
		var newCombi = []pair{list[0]}
		newCombi = append(newCombi, combi...)
		results = append(results, newCombi)
	}

	// combinations without the first item
	results = append(results, combinations(list[1:], n)...)

	fmt.Println("finding", n, "combinations of", list, "=", results)
	return results
}
