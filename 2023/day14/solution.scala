import util.Solution
import util.FileUtils

object Day extends Solution {
  def bubble(column: String): String = {
    var prev = ""
    var bubbled = column
    while (prev != bubbled) {
      prev = bubbled
      bubbled = bubbled.replace(".O", "O.")
    }
    bubbled
  }

  def part1(lines: List[String]): Int =
    lines.transpose
      .map(column =>
        bubble(column.mkString).zipWithIndex
          .map((c, i) => if (c == 'O') column.length - i else 0)
          .sum
      )
      .sum

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
