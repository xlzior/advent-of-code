import util.FileUtils

object Solution {
  def main(args: Array[String]): Unit = {
    val letters = FileUtils
      .readFileContents(args(0))
      .transpose
      .map(
        _.groupBy(identity)
          .mapValues(_.length)
          .toList
      )

    val part1 = letters.flatMap(_.sortBy(-_._2).take(1).map(_._1)).mkString
    println(s"Part 1: $part1")

    val part2 = letters.flatMap(_.sortBy(_._2).take(1).map(_._1)).mkString
    println(s"Part 2: $part2")
  }
}
