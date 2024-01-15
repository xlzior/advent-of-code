import scala.collection.mutable.Queue
import scala.collection.mutable.Set

import util._

object Day extends Solution {
  val deltas = List(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))

  def countPlots(garden: Grid[Char], start: Pair[Int], distance: Int): Int = {
    explore(garden, start, distance)._1.size
  }

  def part1(lines: List[String]): List[Int] = {
    val garden = Grid(lines.map(_.toCharArray()).toArray)
    val start = garden.find('S').get

    List(
      countPlots(garden, start, 6),
      countPlots(garden, start, 64)
    )
  }

  def solve(lines: List[String]): List[Int] = part1(lines)

  def explore(
      garden: Grid[Char],
      start: Pair[Int],
      distance: Int
  ): (Set[Pair[Int]], Int, Int) = {
    val targets = Set[Pair[Int]]()
    val same = Set[Pair[Int]]()
    val diff = Set[Pair[Int]]()

    val queue = Queue[(Int, Pair[Int])]((0, start))
    val explored = Set[Pair[Int]](start)

    while (queue.nonEmpty) {
      val (numSteps, curr) = queue.dequeue()

      if (numSteps % 2 == distance % 2) {
        same.add(curr)
        if (numSteps <= distance) targets.add(curr)
      } else {
        diff.add(curr)
      }

      deltas.foreach(d =>
        garden
          .get(curr + d)
          .map(char => {
            val neighbour = curr + d
            if (char != '#' && !explored.contains(neighbour)) {
              queue.enqueue((numSteps + 1, neighbour))
              explored.add(neighbour)
            }
          })
      )
    }

    (targets, same.size, diff.size)
  }

  def part2(lines: List[String], distance: Long): Long = {
    val grid = Grid(lines.map(_.toCharArray()).toArray)

    val w = grid.h
    assert(w % 2 == 1)
    val r = w / 2

    val start = grid.find('S').get
    val northFace = Pair(0, r)
    val southFace = Pair(2 * r, r)
    val westFace = Pair(r, 0)
    val eastFace = Pair(r, 2 * r)
    val northwestCorner = Pair(0, 0)
    val northeastCorner = Pair(0, 2 * r)
    val southwestCorner = Pair(2 * r, 0)
    val southeastCorner = Pair(2 * r, 2 * r)

    val (_, sameParityFull, diffParityFull) = explore(grid, start, r)
    val northTarget = explore(grid, northFace, 2 * r)._1
    val southTarget = explore(grid, southFace, 2 * r)._1
    val westTarget = explore(grid, westFace, 2 * r)._1
    val eastTarget = explore(grid, eastFace, 2 * r)._1
    val northwestTarget = explore(grid, northwestCorner, r - 1)._1
    val northeastTarget = explore(grid, northeastCorner, r - 1)._1
    val southwestTarget = explore(grid, southwestCorner, r - 1)._1
    val southeastTarget = explore(grid, southeastCorner, r - 1)._1

    val nw = northTarget.union(westTarget)
    val ne = northTarget.union(eastTarget)
    val sw = southTarget.union(westTarget)
    val se = southTarget.union(eastTarget)

    val n = (distance - r) / w

    // full exploration
    var same = (n - 1) * (n - 1)
    var diff = n * n
    val fullExploration = same * sameParityFull + diff * diffParityFull

    // partial exploration
    val straight =
      List(northTarget, southTarget, westTarget, eastTarget)
        .map(_.size.toLong)
        .sum

    val corners =
      List(northwestTarget, northeastTarget, southwestTarget, southeastTarget)
        .map(_.size.toLong)
        .sum * n

    val diagonal =
      List(nw, ne, sw, se)
        .map(_.size.toLong * (n - 1))
        .sum

    fullExploration + diagonal + straight + corners
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = part1(lines)
    println(s"Part 1a: ${solution.head}")
    println(s"Part 1b: ${solution.last}")

    if (args(0) == "2") {
      // https://www.reddit.com/r/adventofcode/comments/18o1071/2023_day_21_a_better_example_input_mild_part_2/
      println(s"Part 2")
      val steps = List(8, 25, 42, 59, 76, 1180148)
      val answers = List(68, 576, 1576, 3068, 5052, 1185525742508L)
      steps
        .zip(answers)
        .map((n, ans) => {
          val obs = part2(lines, n)
          println(s"- $n: $obs ${if (obs == ans) "✅" else "❌"}")
        })
    }

    if (args(0) == "puzzle") {
      println(s"Part 2: ${part2(lines, 26501365)}")
    }
  }
}
