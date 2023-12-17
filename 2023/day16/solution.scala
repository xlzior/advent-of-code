import scala.collection.mutable.Set
import scala.collection.mutable.Queue

import util.Solution
import util.FileUtils
import util.Pair

object Day extends Solution {
  val up = Pair(-1, 0)
  val down = Pair(1, 0)
  val left = Pair(0, -1)
  val right = Pair(0, 1)

  val next = Map(
    '.' -> Map(
      up -> List(up),
      left -> List(left),
      down -> List(down),
      right -> List(right)
    ),
    '/' -> Map(
      up -> List(right),
      right -> List(up),
      down -> List(left),
      left -> List(down)
    ),
    '\\' -> Map(
      up -> List(left),
      left -> List(up),
      down -> List(right),
      right -> List(down)
    ),
    '-' -> Map(
      right -> List(right),
      left -> List(left),
      up -> List(left, right),
      down -> List(left, right)
    ),
    '|' -> Map(
      up -> List(up),
      down -> List(down),
      left -> List(up, down),
      right -> List(up, down)
    )
  )

  def part1(lines: List[String]): Int = {
    val h = lines.length
    val w = lines(0).length()

    val pos = Pair(0, 0)
    val dir = Pair(0, 1)
    val start = (pos, dir)
    val explored = Set[(Pair, Pair)](start)
    val queue = Queue[(Pair, Pair)](start)

    while (queue.nonEmpty) {
      val (pos, dir) = queue.dequeue()

      if (Pair(0, 0) <= pos && pos < Pair(h, w)) {
        explored.add((pos, dir))
        val obj = lines(pos.r)(pos.c)
        val dirs = next(obj)(dir)
        queue.enqueueAll(
          dirs
            .map(dir => (pos + dir, dir))
            .filter(next => !explored.contains(next))
        )
      }
    }

    explored.map(_._1).toSet.size
  }

  def part2(lines: List[String]): Int = {
    -1
  }

  def solve(lines: List[String]): List[Int] = {
    List(part1(lines), part2(lines))
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
