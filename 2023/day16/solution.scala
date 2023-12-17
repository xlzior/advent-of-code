import scala.collection.mutable.Set
import scala.collection.mutable.Queue

import util.Solution
import util.FileUtils
import util.Pair
import util.Grid

object Day extends Solution {
  type Beam = (Pair, Pair)
  val up = Pair(-1, 0)
  val down = Pair(1, 0)
  val left = Pair(0, -1)
  val right = Pair(0, 1)

  val nextDirs = Map(
    '.' -> Map[Pair, List[Pair]]().withDefault(p => List(p)),
    '/' -> Map[Pair, List[Pair]]().withDefault(p => List(Pair(-p.c, -p.r))),
    '\\' -> Map[Pair, List[Pair]]().withDefault(p => List(Pair(p.c, p.r))),
    '-' -> Map(up -> List(left, right), down -> List(left, right))
      .withDefault(p => List(p)),
    '|' -> Map(left -> List(up, down), right -> List(up, down))
      .withDefault(p => List(p))
  )

  def energise(grid: Grid[Char])(start: Beam): Int = {
    val explored = Set[Beam](start)
    val queue = Queue[Beam](start)

    while (queue.nonEmpty) {
      val (pos, dir) = queue.dequeue()

      grid
        .get(pos)
        .map(obj =>
          nextDirs(obj)(dir).foreach(dir => {
            val next = (pos + dir, dir)
            if (grid.isValid(next._1) && !explored.contains(next)) {
              explored.add(next)
              queue.enqueue(next)
            }
          })
        )
    }

    explored.map(_._1).toSet.size
  }

  def part1(grid: Grid[Char]): Int = {
    energise(grid)((Pair(0, 0), right))
  }

  def part2(grid: Grid[Char]): Int = {
    val rs = (0 until grid.h).flatMap(r =>
      List((Pair(r, 0), right), (Pair(r, grid.w - 1), left))
    )
    val cs = (0 until grid.w).flatMap(c =>
      List((Pair(0, c), down), (Pair(grid.h - 1, c), up))
    )

    (rs ++ cs).map(energise(grid)).max
  }

  def solve(lines: List[String]): List[Int] = {
    val grid = Grid(lines.map(_.toCharArray()).toArray)
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
