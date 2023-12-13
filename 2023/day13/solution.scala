import util.Solution
import util.FileUtils

object Day extends Solution {
  def isReflection(left: String, right: String): Boolean = {
    left.reverse.zip(right).forall((a, b) => a == b)
  }

  def findVerticalMirror(pattern: Array[String]): Int = {
    val result = pattern
      .map(line =>
        (1 until line.length)
          .filter(i =>
            isReflection(line.slice(0, i), line.slice(i, line.length))
          )
          .toSet
      )
      .foldLeft(Set.from(1 until pattern(0).length))((acc, curr) => acc & curr)

    result.toList match {
      case List(item) => return item
      case Nil        => return -1
    }
  }

  def findHorizontalMirror(pattern: Array[String]): Int = {
    findVerticalMirror(pattern.map(_.split("")).transpose.map(_.mkString))
  }

  def part1(patterns: Array[Array[String]]): Int = {
    val horizontal = patterns.map(findHorizontalMirror).filter(_ > 0).sum
    val vertical = patterns.map(findVerticalMirror).filter(_ > 0).sum
    horizontal * 100 + vertical
  }

  def part2(patterns: Array[Array[String]]): Int = {
    -1
  }

  def solve(lines: List[String]): List[Int] = {
    val patterns = lines.mkString("\n").split("\n\n").map(_.split("\n"))
    List(part1(patterns), part2(patterns))
  }

  def main(args: Array[String]): Unit = {
    // assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
