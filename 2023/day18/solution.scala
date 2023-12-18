import scala.collection.mutable.Set
import scala.collection.mutable.Queue

import util._

object Day extends Solution {
  val instruction = """([UDLR]) (\d+) \(#([a-f0-9]{6})\)""".r

  val dirs = Map(
    "U" -> Pair(-1, 0),
    "D" -> Pair(1, 0),
    "L" -> Pair(0, -1),
    "R" -> Pair(0, 1)
  )

  def floodfill(
      border: Set[Pair],
      start: Pair,
      topLeft: Pair,
      bottomRight: Pair
  ): Set[Pair] = {
    val outside = Set[Pair](start)
    val queue = Queue[Pair](start)

    while (queue.nonEmpty) {
      val curr = queue.dequeue()

      dirs.values
        .map(dir => dir + curr)
        .filter(next => topLeft <= next && next <= bottomRight)
        .foreach(next => {
          if (!border.contains(next) && !outside.contains(next)) {
            queue.enqueue(next)
            outside.add(next)
          }
        })
    }

    outside
  }

  def part1(instructions: List[(Pair, Int, String)]): Int = {
    var curr = Pair(0, 0)
    val border = Set[Pair](curr)

    instructions.foreach((dir, count, colour) => {
      (1 to count).foreach(i => border.add(curr + dir * i))
      curr = curr + dir * count
    })
    val topLeft = Pair(border.map(_.r).min - 1, border.map(_.c).min - 1)
    val bottomRight = Pair(border.map(_.r).max + 1, border.map(_.c).max + 1)

    val outside = floodfill(border, topLeft, topLeft, bottomRight)
    val area = (bottomRight.r - topLeft.r + 1) * (bottomRight.c - topLeft.c + 1)

    area - outside.size

    // println(outside.size)
    // (topLeft.r to bottomRight.r).foreach(r => {
    //   (topLeft.c to bottomRight.c).foreach(c => {
    //     print(
    //       if (border.contains(Pair(r, c))) "#"
    //       else if (outside contains Pair(r, c)) "."
    //       else "~"
    //     )
    //   })
    //   println()
    // })

    // border.size
  }

  def part2(instructions: List[(Pair, Int, String)]): Int = {
    -1
  }

  def solve(lines: List[String]): List[Int] = {
    val instructions = lines.map(_ match {
      case instruction(udlr, num, colour) => {
        val dir = dirs(udlr)
        val count = num.toInt
        (dir, count, colour)
      }
    })

    List(part1(instructions), part2(instructions))
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
