import util.Solution
import util.FileUtils
import util.Pair

object Day extends Solution {
  def emptyLines(lines: List[String]): List[Int] =
    lines.zipWithIndex.filter((line, _) => line.forall(_ == '.')).map(_._2)

  def getTotalDistance(lines: List[String], n: Int): Long = {
    val emptyRows = emptyLines(lines)
    val emptyCols = emptyLines(lines.transpose.map(_.mkString))

    val galaxies = lines.zipWithIndex.flatMap((row, r) =>
      row.zipWithIndex.collect { case ('#', c) => Pair(r, c) }
    )

    galaxies
      .combinations(2)
      .map {
        case List(a, b) => {
          val basic = (a - b).abs.toLong
          val (minX, minY) = (a.x.min(b.x), a.y.min(b.y))
          val (maxX, maxY) = (a.x.max(b.x), a.y.max(b.y))
          val extraRows = emptyRows.count(x => minX < x && x < maxX).toLong
          val extraCols = emptyCols.count(y => minY < y && y < maxY).toLong
          basic + extraRows * (n - 1) + extraCols * (n - 1)
        }
        case _ => 0
      }
      .sum
  }

  def solve(lines: List[String]): List[Long] = {
    List(
      getTotalDistance(lines, 2),
      getTotalDistance(lines, 10),
      getTotalDistance(lines, 100),
      getTotalDistance(lines, 1_000_000)
    )
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
