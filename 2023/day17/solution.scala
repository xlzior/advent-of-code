import scala.collection.mutable.PriorityQueue
import scala.collection.mutable.Map
import scala.collection.mutable.Set

import util.Solution
import util.FileUtils
import util.Grid
import util.Pair

object Day extends Solution {
  val up = Pair(-1, 0)
  val down = Pair(1, 0)
  val left = Pair(0, -1)
  val right = Pair(0, 1)

  val nextDirs = Map[Pair, List[Pair]](
    up -> List(left, right),
    down -> List(left, right),
    left -> List(up, down),
    right -> List(up, down)
  )

  implicit val ordering: Ordering[(Int, Pair, List[Pair])] = Ordering.by(-_._1)

  def crucible(grid: Grid[Int], min: Int, max: Int): Int = {
    val start = Pair(0, 0)
    val end = Pair(grid.h - 1, grid.w - 1)
    val dist = Map((start, List(right, down)) -> 0)
    val pq = PriorityQueue((0, start, List(right, down)))

    while (pq.nonEmpty) {
      val (heatLoss, curr, outDirs) = pq.dequeue()

      if (curr == end)
        return heatLoss

      outDirs.foreach(dir => {
        (min to max).foreach(i => {
          val next = curr + dir * i

          if (grid.contains(next)) {
            val hl = (1 to i).map(j => grid.get(curr + dir * j).get).sum
            val alt = heatLoss + hl

            if (alt < dist.getOrElse((next, nextDirs(dir)), Int.MaxValue)) {
              dist((next, nextDirs(dir))) = alt
              pq.addOne((heatLoss + hl, next, nextDirs(dir)))
            }
          }
        })
      })
    }

    -1
  }

  def part1(grid: Grid[Int]): Int = crucible(grid, 1, 3)
  def part2(grid: Grid[Int]): Int = crucible(grid, 4, 10)

  def solve(lines: List[String]): List[Int] = {
    val grid = Grid(lines.map(_.toCharArray().map(_.asDigit)).toArray)
    List(part1(grid), part2(grid))
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
