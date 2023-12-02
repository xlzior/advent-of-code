import util.FileUtils

object Solution {
  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))

    val games = lines.map(game =>
      """(\d+) (\w+)""".r
        .findAllMatchIn(game)
        .map(m => (m.group(1).toInt, m.group(2)))
        .toList
    )

    val bag = Map("red" -> 12, "green" -> 13, "blue" -> 14)

    val part1 = games.zipWithIndex
      .filter((game, _) => game.forall((count, colour) => count <= bag(colour)))
      .map((_, index) => index + 1)
      .sum

    println(s"Part 1: $part1")

    val part2 =
      games
        .map(
          _.foldLeft(Map[String, Int]().withDefault(_ => 0))((acc, curr) => {
            val (count, colour) = curr
            acc.updated(colour, count.max(acc(colour)))
          }).values.product
        )
        .sum

    println(s"Part 2: $part2")
  }
}
