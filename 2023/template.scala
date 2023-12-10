import util.Solution
import util.FileUtils

object Day extends Solution {
  def part1(lines: List[String]): Int = {
    -1
  }

  def part2(lines: List[String]): Int = {
    -1
  }

  def solve(lines: List[String]): (Int, Int) = {
    (part1(lines), part2(lines))
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read("puzzle.in")
    val (part1, part2) = solve(lines)
    println(s"Part 1: $part1")
    println(s"Part 2: $part2")
  }
}
