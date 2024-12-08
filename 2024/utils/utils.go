package utils

import (
	"os"
	"strings"
)

func ReadLines() []string {
	filename := os.Args[1]
	data, _ := os.ReadFile(filename)
	lines := strings.Split(string(data), "\n")
	return lines
}
