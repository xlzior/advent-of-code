import util.FileUtils

object Solution {
  def isPossibleTriangle(a: Int, b: Int, c: Int): Boolean = {
    a + b > c && a + c > b && b + c > a
  }

  def main(args: Array[String]): Unit = {
    val lines = FileUtils
      .readFileContents(args(0))
      .map(_.trim().split("\\s+").map(_.toInt))

    val part1 = lines
      .count { case Array(a: Int, b: Int, c: Int) =>
        isPossibleTriangle(a, b, c)
      }

    println(s"Part 1: $part1")

    val part2 = lines
      .grouped(3)
      .flatMap(_.transpose)
      .count {
        case List(a: Int, b: Int, c: Int) =>
          isPossibleTriangle(a, b, c)
        case _ => false
      }

    println(s"Part 2: $part2")
  }
}
