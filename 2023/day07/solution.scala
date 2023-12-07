import util.FileUtils

class Hand(val cards: String, val counter: List[Int]) extends Ordered[Hand] {
  def this(cards: String) = {
    this(
      cards,
      cards.groupBy(identity).values.map(_.length).toList.sorted.reverse
    )
  }

  def typeValue: Int = {
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

  val cardValues =
    List('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')

  def cardValue = cards.map(c => 13 - cardValues.indexOf(c))

  def compare(that: Hand): Int = {
    val typeCompare = (this.typeValue - that.typeValue)
    val cardCompare = this.cardValue
      .zip(that.cardValue)
      .map((a, b) => a.compare(b))
      .toList
    (typeCompare :: cardCompare).dropWhile(_ == 0).headOption.getOrElse(0)
  }

  override def toString(): String = s"$typeValue $cards"
}

object Solution {
  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))

    val hands = lines.map(_.split(' ') match {
      case Array(cards, bid) => (Hand(cards), bid.toInt)
    })

    val part1 =
      hands.sorted.zipWithIndex.map((hand, b) => hand._2 * (b + 1)).sum

    println(s"Part 1: $part1")
  }
}
