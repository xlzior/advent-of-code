import scala.collection.mutable.PriorityQueue
import scala.collection.mutable.Map
import scala.collection.mutable.Set

import util.Solution
import util.FileUtils
import util.Grid
import util.Pair

object Day extends Solution {
  val dirs = List(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))

  implicit val ordering: Ordering[(Int, Pair, List[Pair])] = Ordering.by(-_._1)

  def part1(grid: Grid[Int]): Int = {
    val start = Pair(0, 0)
    val end = Pair(grid.h - 1, grid.w - 1)
    val explored = Set[(Pair, List[Pair])]((start, List.empty[Pair]))
    val pq =
      PriorityQueue[(Int, Pair, List[Pair])]((0, start, List.empty[Pair]))

    while (pq.nonEmpty) {
      val (heatLoss, u, path) = pq.dequeue()

      if (u == end) {
        return heatLoss
      }

      dirs
        .filter(dir => {
          val goingBackwards = path.headOption.map(_ == -dir).getOrElse(false)
          val fourthConsecutive =
            path.length >= 3 && path.take(3).forall(_ == dir)
          !goingBackwards && !fourthConsecutive
        })
        .foreach(dir => {
          val v = u + dir

          grid
            .get(v)
            .map(hl => {
              if (!explored.contains((v, (dir :: path).take(3)))) {
                explored.add((v, (dir :: path).take(3)))
                pq.addOne((heatLoss + hl, v, dir :: path))
              }
            })
        })
    }

    -1
  }

  def part2(lines: List[String]): Int = {
    -1
  }

  def solve(lines: List[String]): List[Int] = {
    val grid = Grid(lines.map(_.toCharArray().map(_.asDigit)).toArray)
    List(part1(grid), part2(lines))
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
