package main

import (
	"fmt"
	"image"
	"image/color"
	"image/png"
	"os"
	"path/filepath"
	"strings"

	"github.com/xlzior/aoc2024/utils"
)

func generateFileName(i int) string {
	return "snapshots/" + fmt.Sprintf("%05d", i) + ".png"
}

func saveAsPng(positions map[utils.Pair]int, i int, size utils.Pair) {
	height := size.R
	width := size.C
	img := image.NewRGBA(image.Rect(0, 0, width, height))

	for y := 0; y < height; y++ {
		for x := 0; x < width; x++ {
			if positions[utils.Pair{R: y, C: x}] > 0 {
				img.Set(x, y, color.RGBA{R: 27, G: 120, B: 25, A: 255})
			} else {
				img.Set(x, y, color.RGBA{R: 255, G: 255, B: 255, A: 255})
			}
		}
	}

	file, _ := os.Create(generateFileName(i))
	defer file.Close()
	png.Encode(file, img)
}

func fileExists(file string) bool {
	_, err := os.Stat(file)
	return !os.IsNotExist(err)
}

func findSmallestFile(folder string) string {
	files, _ := os.ReadDir(folder)

	var smallestFile string
	var smallestSize int64 = -1

	for _, file := range files {
		if file.IsDir() {
			continue
		}

		filePath := filepath.Join(folder, file.Name())
		info, _ := os.Stat(filePath)

		if smallestSize == -1 || info.Size() < smallestSize {
			smallestFile = file.Name()
			smallestSize = info.Size()
		}
	}
	return smallestFile
}

func runSimulation(robots [][2]utils.Pair, size utils.Pair, start, until, step int) {
	for i := start; i < until; i += step {
		positions := simulate(robots, i, size)
		saveAsPng(positions, i, size)
	}
}

func findChristmasTree(robots [][2]utils.Pair, size utils.Pair) int {
	end := 10_000
	if !fileExists(generateFileName(end - 1)) {
		runSimulation(robots, size, 1, 10_000, 1)
	}
	smallestFile := findSmallestFile("snapshots")
	n := utils.MustParseInt(strings.Replace(smallestFile, ".png", "", 1))
	return n
}
