import util.Solution
import util.FileUtils
import util.Pair

object Day extends Solution {
  def part1(lines: List[String]): Int = {
    val w = lines(0).length
    val emptyRow = ".".repeat(w) + "\n"
    val rowsExpanded = lines
      .mkString("\n")
      .replace(emptyRow, emptyRow.repeat(2))
      .split("\n")
      .toList

    val h = rowsExpanded.length
    val emptyCol = ".".repeat(h) + "\n"
    val expanded = rowsExpanded.transpose
      .map(_.mkString)
      .mkString("\n")
      .replace(emptyCol, emptyCol.repeat(2))
      .split("\n")
      .toList
      .transpose
      .map(_.mkString)

    val galaxies = expanded.zipWithIndex.flatMap((row, r) =>
      row.zipWithIndex.flatMap((char, c) => {
        if (char == '#') List(Pair(r, c))
        else List.empty
      })
    )

    galaxies.combinations(2).map { case List(a, b) => (a - b).abs }.sum
  }

  def part2(lines: List[String]): Int = {
    -1
  }

  def solve(lines: List[String]): (Int, Int) = {
    (part1(lines), part2(lines))
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val (part1, part2) = solve(lines)
    println(s"Part 1: $part1")
    println(s"Part 2: $part2")
  }
}
