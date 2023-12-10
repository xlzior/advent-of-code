import util.FileUtils
import scala.collection.immutable.Stream.Empty

/*
  4 2 2 1 0 0 <- determines how many subsequent cards

  1 1 1 1 1 1 <- determines what to +
+ 0 1 1 1 1 0
= 1 2 2 2 2 1

+ 0 0 1 1 0 0
= 1 2 4 4 2 1
= 1 2 4 8 6 1
= 1 2 4 8 14 1
 */

object Solution {
  def parse(lines: List[String]): List[Int] =
    lines
      .map(
        _.split(':')(1)
          .split('|')
          .map(_.trim.split("\\s+").map(_.toInt).toSet)
      )
      .map(card => card(0).intersect(card(1)).size)

  def getCount(cards: Stream[Int], counts: Stream[Int]): Stream[Int] =
    (cards, counts) match {
      case (Empty, Empty) => counts
      case (card #:: cardsTail, count #:: countsTail) =>
        count #:: getCount(
          cardsTail,
          countsTail
            .zip(Stream.fill(card)(count) ++ Stream.continually(0))
            .map((a, b) => a + b)
        )
      case _ => counts
    }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.read(args(0))
    val cards = parse(lines)

    val part1 = cards.map(n => if (n == 0) 0 else math.pow(2, n - 1)).sum.toInt

    println(s"Part 1: $part1")

    val part2 =
      cards.zipWithIndex
        .foldLeft(Array.fill(cards.length)(1))((acc, curr) => {
          val (n, i) = curr
          (i + 1 to n + i).foreach(j => acc(j) += acc(i))
          acc
        })
        .sum

    println(s"Part 2: $part2")

    val part2stream = getCount(cards.toStream, Stream.fill(cards.length)(1)).sum

    println(s"Part 2: $part2stream")
  }
}
