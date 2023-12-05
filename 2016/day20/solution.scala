import util.FileUtils

object Solution {
  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))
    val ranges =
      lines.map(_.split("-").map(_.toLong)).map { case Array(a, b) => (a, b) }

    val whitelistStart = ranges
      .map((_, high) => high + 1)
      .filter(c => !ranges.exists((a, b) => a <= c && c <= b))

    println(s"Part 1: ${whitelistStart.min}")

    val whitelistLengths = whitelistStart
      .filter(x => x < "4294967295".toLong)
      .map(start =>
        ranges
          .map((low, high) => low)
          .filter(low => low > start)
          .min - start
      )

    println(s"Part 2: ${whitelistLengths.sum}")
  }
}
