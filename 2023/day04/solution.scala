import util.FileUtils

object Solution {
  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))

    val cards =
      lines
        .map(
          _.split(':')(1)
            .split('|')
            .map(_.trim.split("\\s+").map(_.toInt).toSet)
        )
        .map(card => card(0).intersect(card(1)).size)

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
  }
}

/*

4 2 2 1 0 0 <- determines how many of the next card

1 1 1 1 1 1 <- determines how many to +
1 2 2 2 2 1
1 2 4 4 2 1
1 2 4 8 6 1
1 2 4 8 14 1
 */
