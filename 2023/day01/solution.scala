import util.FileUtils

val digits = Map(
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

  def getDigits(line: String): List[(Int, Int)] = {
    val digitsOnly = line.filter(_.isDigit)

    if (digitsOnly.isEmpty) {
      return List()
    }

    val firstDigitIndex = line.indexOf(digitsOnly.head)
    val firstDigit = (firstDigitIndex, line(firstDigitIndex).asDigit)
    val lastDigitIndex = line.lastIndexOf(digitsOnly.last)
    val lastDigit = (lastDigitIndex, line(lastDigitIndex).asDigit)
    List(firstDigit, lastDigit)
  }

  def getWords(line: String): List[(Int, Int)] = {
    val spelledOut = digits.keys
      .flatMap(key =>
        List((line.indexOf(key), key), (line.lastIndexOf(key), key))
      )
      .filter(_._1 != -1)

    if (spelledOut.isEmpty) {
      return List()
    }

    val (index1, word1) = spelledOut.minBy(_._1)
    val (index2, word2) = spelledOut.maxBy(_._1)

    List((index1, digits(word1)), (index2, digits(word2)))
  }

  def part2(lines: List[String]): Int =
    lines
      .map(line => {
        val numbers = getDigits(line) ++ getWords(line)

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
