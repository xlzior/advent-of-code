import util.Solution
import util.FileUtils

object Day extends Solution {
  def countSmudges(left: String, right: String): Int = {
    left.reverse.zip(right).count((a, b) => a != b)
  }

  def findVertical(smudges: Int)(pattern: Array[String]): Int =
    pattern
      .map(line =>
        (1 until line.length).map(i =>
          countSmudges(line.slice(0, i), line.slice(i, line.length))
        )
      )
      .toList
      .transpose
      .map(_.sum)
      .indexOf(smudges) + 1

  def findHorizontal(smudges: Int)(pattern: Array[String]): Int =
    findVertical(smudges)(
      pattern.map(_.split("")).transpose.map(_.mkString)
    )

  def summariseMirrors(patterns: Array[Array[String]], smudges: Int): Int = {
    val horizontal = patterns.map(findHorizontal(smudges)).filter(_ > 0).sum
    val vertical = patterns.map(findVertical(smudges)).filter(_ > 0).sum
    horizontal * 100 + vertical
  }

  def solve(lines: List[String]): List[Int] = {
    val patterns = lines.mkString("\n").split("\n\n").map(_.split("\n"))
    List(summariseMirrors(patterns, 0), summariseMirrors(patterns, 1))
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
