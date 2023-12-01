import util.FileUtils

val mapping = Map(
  "1" -> 1,
  "2" -> 2,
  "3" -> 3,
  "4" -> 4,
  "5" -> 5,
  "6" -> 6,
  "7" -> 7,
  "8" -> 8,
  "9" -> 9,
  "one" -> 1,
  "two" -> 2,
  "three" -> 3,
  "four" -> 4,
  "five" -> 5,
  "six" -> 6,
  "seven" -> 7,
  "eight" -> 8,
  "nine" -> 9
)

object Solution {
  def part1(lines: List[String]): Int =
    lines
      .map(_.filter(_.isDigit))
      .map(s => (s.take(1) + s.takeRight(1)).toInt)
      .sum

  def part2(lines: List[String]): Int =
    lines
      .map(line => {
        val numbers = mapping.keys
          .flatMap(key =>
            List(
              (line.indexOf(key), mapping(key)),
              (line.lastIndexOf(key), mapping(key))
            )
          )
          .filter(_._1 != -1)

        val first = numbers.minBy(_._1)._2
        val last = numbers.maxBy(_._1)._2
        first * 10 + last
      })
      .sum

  def main(args: Array[String]): Unit = {
    val lines = FileUtils.readFileContents(args(0))

    args(1) match {
      case "1" => println(s"Part 1: ${part1(lines)}")
      case "2" => println(s"Part 2: ${part2(lines)}")
      case "3" => {
        println(s"Part 1: ${part1(lines)}")
        println(s"Part 2: ${part2(lines)}")
      }
    }
  }
}
