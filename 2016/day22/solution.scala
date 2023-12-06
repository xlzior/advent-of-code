import util.FileUtils

object Solution {
  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))

    val files = lines
      .drop(2)
      .map(line =>
        """(\d+)""".r.findAllIn(line).map(_.toInt).toList match {
          case List(x, y, size, used, avail, usedPercent) =>
            (x, y, size, used, avail, usedPercent)
        }
      )

    val used = files.map(_._4)
    val avails = files.map(_._5)

    val part1 = used.zipWithIndex
      .map((used, u) =>
        if (used == 0) {
          0
        } else {
          avails.zipWithIndex.count((avail, a) => u != a && used <= avail)
        }
      )
      .sum

    println(s"Part 1: $part1")
  }
}
