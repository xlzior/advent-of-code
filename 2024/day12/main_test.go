package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
	"testing"

	"github.com/xlzior/aoc2024/utils"
)

func TestAdd(t *testing.T) {
	for i := 1; i <= 5; i++ {
		input, _ := os.ReadFile(fmt.Sprint(i, ".in"))
		grid := utils.Grid{Grid: strings.Split(string(input), "\n")}
		p1, p2 := solve(grid)
		observed := []int{p1, p2}

		answers, _ := os.ReadFile(fmt.Sprint(i, ".out"))
		expected := strings.Split(string(answers), "\n")
		for j, p := range []int{1, 2} {
			e, err := strconv.Atoi(expected[j])
			o := observed[j]
			if err != nil {
				fmt.Printf("skipping test case %d part %d, received %s\n", i, p, expected[j])
			} else if o == e {
				fmt.Printf("test case %d part %d passed: %d\n", i, p, e)
			} else {
				t.Errorf("test case %d failed: expected %d, received %d", i, e, o)
			}
		}
	}
}
