import util.FileUtils

def toDiffs(list: List[Int]): List[Int] = {
  list.zip(list.drop(1)).map { case (a, b) => b - a }
}

def fromDiffs(diffs: Stream[Int], start: Int): Stream[Int] = {
  diffs.scanLeft(start)(_ + _)
}

object Solution {
  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))
    val histories = lines.map(_.split(" ").map(_.toInt).toList)

    val patterns = histories
      .map(history => {
        var left = List(history.head)
        var diff = toDiffs(history)
        while (diff(0) != diff(1) || diff(1) != diff(2)) {
          left = diff.head :: left
          diff = toDiffs(diff)
        }
        (left, diff(0), history.length)
      })

    val part1 = patterns
      .map((left, diff, length) => {
        left.foldLeft(Stream.continually(diff))(fromDiffs)(length)
      })
      .sum

    println(s"Part 1: $part1")

    val part2 = patterns
      .map((left, diff, length) =>
        left.foldLeft(diff)((acc, curr) => curr - acc)
      )
      .sum

    println(s"Part 2: $part2")
  }
}
