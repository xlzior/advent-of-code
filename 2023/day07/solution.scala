import util.FileUtils

def toCounts(string: String): List[Int] = {
  string.groupBy(identity).values.map(_.length).toList.sorted.reverse
}

def jokerify(string: String): String = {
  val nonJokers = string.filter(_ != 'J').groupBy(identity).mapValues(_.length)
  val mostCommon = if (nonJokers.nonEmpty) nonJokers.maxBy(_._2)._1 else '3'
  string.replaceAll("J", mostCommon.toString())
}

class Hand(val cards: String, val counter: List[Int], val counter2: List[Int]) {
  def this(cards: String) = {
    this(cards, toCounts(cards), toCounts(jokerify(cards)))
  }

  def typeValue(counter: List[Int]): Int = {
    counter match {
      case List(5)             => 7
      case List(4, 1)          => 6
      case List(3, 2)          => 5
      case List(3, 1, 1)       => 4
      case List(2, 2, 1)       => 3
      case List(2, 1, 1, 1)    => 2
      case List(1, 1, 1, 1, 1) => 1
      case _                   => 0
    }
  }

  val cardValues = "AKQJT98765432".reverse
  val cardValues2 = "AKQT98765432J".reverse

  def cardValue = cards.map(c => cardValues.indexOf(c))
  def cardValue2 = cards.map(c => cardValues2.indexOf(c))

  def value = (
    typeValue(counter),
    cardValue(0),
    cardValue(1),
    cardValue(2),
    cardValue(3),
    cardValue(4)
  )

  def value2 = (
    typeValue(counter2),
    cardValue2(0),
    cardValue2(1),
    cardValue2(2),
    cardValue2(3),
    cardValue2(4)
  )
}

def countWinnings(sortedHands: List[(Hand, Int)]) = {
  sortedHands.zipWithIndex.map((hand, b) => hand._2 * (b + 1)).sum
}

object Solution {
  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))

    val hands = lines.map(_.split(' ') match {
      case Array(cards, bid) => (Hand(cards), bid.toInt)
    })

    val part1 = countWinnings(hands.sortBy((hand, _) => hand.value))
    val part2 = countWinnings(hands.sortBy((hand, _) => hand.value2))

    println(s"Part 1: $part1")
    println(s"Part 2: $part2")
  }
}
