import util.FileUtils

def count(string: String): List[Int] = {
  string.groupBy(identity).values.map(_.length).toList.sorted.reverse
}

def jokerify(string: String): String = {
  val nonJokers = string.filter(_ != 'J').groupBy(identity).mapValues(_.length)
  val mostCommon = if (nonJokers.nonEmpty) nonJokers.maxBy(_._2)._1 else '3'
  string.replaceAll("J", mostCommon.toString())
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

def cardValue(cardStrength: String)(cards: String) =
  cards.map(c => cardStrength.reverse.indexOf(c))

def value(counter: List[Int], cards: String, cardStrength: String) = {
  val cardValues = cardValue(cardStrength)(cards)
  (
    typeValue(counter),
    cardValues(0),
    cardValues(1),
    cardValues(2),
    cardValues(3),
    cardValues(4)
  )
}

def value1(cards: String) =
  value(count(cards), cards, "AKQJT98765432")

def value2(cards: String) =
  value(count(jokerify(cards)), cards, "AKQT98765432J")

def countWinnings(sortedHands: List[(String, Int)]) = {
  sortedHands.zipWithIndex.map((hand, b) => hand._2 * (b + 1)).sum
}

object Solution {
  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.read(args(0))
    val hands = lines.map(_.split(' ') match {
      case Array(cards, bid) => (cards, bid.toInt)
    })

    val part1 = countWinnings(hands.sortBy((cards, _) => value1(cards)))
    val part2 = countWinnings(hands.sortBy((cards, _) => value2(cards)))

    println(s"Part 1: $part1")
    println(s"Part 2: $part2")
  }
}
