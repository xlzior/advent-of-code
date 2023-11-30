import util.FileUtils

// 2022 Day 1

object Day1 {
  def main(args: Array[String]): Unit = {
    val lines = FileUtils.readFileContents(args(0))

    val groups = lines.mkString(",").split(",,")
    val groupTotals = groups.map(group => group.split(",").map(_.toLong).sum)
    println(s"Part 1: ${groupTotals.max}")

    val top3 = groupTotals.sorted.reverse.take(3)
    println(s"Part 2: ${top3.sum}")
  }
}
