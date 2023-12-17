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
  val sameDirection = (pair: Pair) => List(pair)

  val next = Map(
    '.' -> Map[Pair, List[Pair]]().withDefault(sameDirection),
    '-' -> Map(up -> List(left, right), down -> List(left, right))
      .withDefault(sameDirection),
    '|' -> Map(left -> List(up, down), right -> List(up, down))
      .withDefault(sameDirection),
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
    )
  )

  def energise(lines: List[String])(pos: Pair, dir: Pair): Int = {
    val h = lines.length
    val w = lines(0).length()

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

  def part1(lines: List[String]): Int = {
    energise(lines)(Pair(0, 0), right)
  }

  def part2(lines: List[String]): Int = {
    val h = lines.length
    val w = lines(0).length()

    val rs =
      (0 until h).flatMap(r =>
        List((Pair(r, 0), right), (Pair(r, w - 1), left))
      )
    val cs =
      (0 until w).flatMap(c => List((Pair(0, c), down), (Pair(h - 1, c), up)))

    (rs ++ cs).map((pos, dir) => energise(lines)(pos, dir)).max
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
