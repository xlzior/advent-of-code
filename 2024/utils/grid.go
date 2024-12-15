package utils

import "fmt"

type Grid struct {
	Grid []string
}

func (g *Grid) GetBottomRight() Pair {
	return Pair{g.NumRows(), g.NumCols()}
}

func (g *Grid) NumRows() int {
	return len(g.Grid)
}

func (g *Grid) NumCols() int {
	return len(g.Grid[0])
}

func (g *Grid) Contains(p Pair) bool {
	br := g.GetBottomRight()
	return p.R >= 0 && p.R < br.R && p.C >= 0 && p.C < br.C
}

func (g *Grid) GetCell(p Pair) rune {
	if !g.Contains(p) {
		return '\u0000'
	}
	return rune(g.Grid[p.R][p.C])
}

func (g *Grid) SetCell(p Pair, r rune) {
	if g.Contains(p) {
		row := []rune(g.Grid[p.R])
		row[p.C] = r
		g.Grid[p.R] = string(row)
	}
}

func (g *Grid) Print() {
	for _, row := range g.Grid {
		fmt.Println(row)
	}
}

func (g *Grid) FindAllList(char rune) []Pair {
	results := make([]Pair, 0)
	br := g.GetBottomRight()
	for r := 0; r < br.R; r++ {
		for c := 0; c < br.C; c++ {
			if g.GetCell(Pair{r, c}) == char {
				results = append(results, Pair{r, c})
			}
		}
	}
	return results
}

func (g *Grid) FindAllSet(char rune) map[Pair]bool {
	results := make(map[Pair]bool, 0)
	br := g.GetBottomRight()
	for r := 0; r < br.R; r++ {
		for c := 0; c < br.C; c++ {
			if g.GetCell(Pair{r, c}) == char {
				results[Pair{r, c}] = true
			}
		}
	}
	return results
}
