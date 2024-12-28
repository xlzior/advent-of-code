package main

import (
	"container/heap"
	"fmt"

	"github.com/xlzior/aoc2024/utils"
)

type posdir struct {
	pos utils.Pair
	dir utils.Pair
}

func (pd posdir) turnRight() posdir {
	return posdir{pd.pos, pd.dir.TurnRight()}
}

func (pd posdir) turnLeft() posdir {
	return posdir{pd.pos, pd.dir.TurnLeft()}
}

func (pd posdir) moveForward() posdir {
	return posdir{pd.pos.Plus(pd.dir), pd.dir}
}

type Item struct {
	PosDir  posdir
	Visited []utils.Pair
	Score   int
	Index   int
}

type PriorityQueue []*Item

func (pq PriorityQueue) Len() int { return len(pq) }

func (pq PriorityQueue) Less(i, j int) bool {
	return pq[i].Score < pq[j].Score
}

func (pq PriorityQueue) Swap(i, j int) {
	pq[i], pq[j] = pq[j], pq[i]
	pq[i].Index = i
	pq[j].Index = j
}

func (pq *PriorityQueue) Push(x any) {
	item := x.(*Item)
	item.Index = len(*pq)
	*pq = append(*pq, item)
}

func (pq *PriorityQueue) Pop() any {
	old := *pq
	n := len(old)
	item := old[n-1]
	item.Index = -1 // For safety
	*pq = old[0 : n-1]
	return item
}

var east = utils.Pair{R: 0, C: 1}

func dijkstra(grid utils.Grid) (int, int) {
	start := grid.FindAllList('S')[0]
	end := grid.FindAllList('E')[0]
	dist := map[posdir]int{{start, east}: 0}
	pq := make(PriorityQueue, 0)
	heap.Init(&pq)
	heap.Push(&pq, &Item{PosDir: posdir{start, east}, Score: 0, Visited: []utils.Pair{start}})
	bestScore := -1
	goodSeats := map[utils.Pair]bool{start: true, end: true}

	for pq.Len() > 0 {
		curr := heap.Pop(&pq).(*Item)
		if curr.PosDir.pos == end {
			if bestScore < 0 {
				bestScore = curr.Score
			}
			if curr.Score == bestScore {
				for _, pos := range curr.Visited {
					goodSeats[pos] = true
				}
			}
		}

		for _, next := range []Item{
			{PosDir: curr.PosDir.moveForward(), Score: curr.Score + 1},
			{PosDir: curr.PosDir.turnLeft(), Score: curr.Score + 1000},
			{PosDir: curr.PosDir.turnRight(), Score: curr.Score + 1000},
		} {
			if grid.GetCell(next.PosDir.pos) == '#' {
				continue
			}
			_, exists := dist[next.PosDir]
			if !exists || next.Score <= dist[next.PosDir] {
				dist[next.PosDir] = next.Score
				v := make([]utils.Pair, len(curr.Visited))
				copy(v, curr.Visited)
				v = append(v, next.PosDir.pos)
				heap.Push(&pq, &Item{PosDir: next.PosDir, Score: next.Score, Visited: v})
			}
		}
	}
	return bestScore, len(goodSeats)
}

func main() {
	grid := utils.Grid{Grid: utils.ReadLines()}
	part1, part2 := dijkstra(grid)
	fmt.Println("Part 1:", part1)
	fmt.Println("Part 2:", part2)
}
