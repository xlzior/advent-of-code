package utils

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

func ReadLines() []string {
	filename := os.Args[1]
	data, _ := os.ReadFile(filename)
	lines := strings.Split(string(data), "\n")
	return lines
}

func MustParseInt(s string) int {
	n, err := strconv.Atoi(s)
	if err != nil {
		panic(fmt.Sprintf("failed to convert %s to int", s))
	}
	return n
}
