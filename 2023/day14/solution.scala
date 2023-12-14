import util.Solution
import util.FileUtils
import util.Pair

object Day extends Solution {
  def rollLeft(column: String): String = {
    val updated = column.replace(".O", "O.")
    if (updated == column) column else rollLeft(updated)
  }

  def rotateAntiClockwise(grid: List[String]): List[String] =
    grid.map(_.reverse).transpose.map(_.mkString)

  def rotateClockwise(grid: List[String]): List[String] =
    grid.transpose.map(_.mkString.reverse)

  def calculateLoad(grid: List[String]): Int =
    grid
      .map(column =>
        column.zipWithIndex
          .map((c, i) => if (c == 'O') column.length - i else 0)
          .sum
      )
      .sum

  def part1(grid: List[String]): Int = {
    val rolled = grid.map(rollLeft)
    calculateLoad(rolled)
  }

  def part2(grid: List[String]): Int = {
    val n = 1_000_000_000
    var target = n
    var rolled = grid
    var seen = Map[List[String], Int](grid -> 0)

    var i = 0
    while (i < target) {
      i += 1
      for (_ <- 1 to 4) {
        rolled = rotateClockwise(rolled.map(rollLeft))
      }
      if (seen.contains(rolled)) {
        val cycle = i - seen(rolled)
        val offset = (n - i) % cycle
        target = i + offset
      }
      seen = seen.updated(rolled, i)
    }
    calculateLoad(rolled)
  }

  def solve(lines: List[String]): List[Int] = {
    val grid = rotateAntiClockwise(lines)
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
