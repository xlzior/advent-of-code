import scala.collection.mutable.Queue
import scala.collection.mutable.Set

import util._

object Day extends Solution {
  val deltas = List(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))

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

  def part2(lines: List[String], distance: Long): Long = {
    val grid = Grid(lines.map(_.toCharArray()).toArray)
    val r = grid.h / 2

    val coords = List(0, r, 2 * r)
    val List(List(nw, n, ne), List(w, start, e), List(sw, s, se)) =
      coords.map(r => coords.map(c => Pair(r, c)))
    val edges = List(n, e, s, w)
    val corners = List(nw, ne, se, sw)

    val (_, odd, even) = explore(grid, start, r)
    val fromEdge = edges.map(edge => explore(grid, edge, 2 * r)._1)
    val fromEdges = (fromEdge :+ fromEdge(0)).sliding(2).map {
      case List(a, b) => a.union(b)
    }
    val fromCorner = corners.map(corner => explore(grid, corner, r - 1)._1)

    val N = (distance - r) / grid.h
    val T = fromEdge.map(_.size.toLong).sum
    val A = fromEdges.map(_.size.toLong).sum
    val B = fromCorner.map(_.size.toLong).sum

    // https://www.reddit.com/r/adventofcode/comments/18o4y0m/2023_day_21_part_2_algebraic_solution_using_only/
    List(
      (N - 1) * (N - 1) * odd,
      N * N * even,
      (N - 1) * A,
      N * B,
      T
    ).sum
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
