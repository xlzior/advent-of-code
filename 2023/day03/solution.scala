import util.FileUtils
import util.Pair
import util.Timer

object Solution {
  val symbolPattern = """[^\d\s.]""".r
  val numberPattern = """(\d+)""".r
  val gearPattern = """[*]""".r
  val deltas = (-1 to 1).flatMap(x => (-1 to 1).map(y => Pair(x, y)))

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))

    val timer = Timer()
    timer.checkpoint()

    val symbols = lines.zipWithIndex
      .flatMap((line, y) =>
        symbolPattern.findAllMatchIn(line).map(m => Pair(m.start, y))
      )
      .toSet

    val numbers = lines.zipWithIndex.flatMap((line, y) =>
      numberPattern
        .findAllMatchIn(line)
        .map(m =>
          (m.group(1).toInt, (m.start to m.end - 1).map(x => Pair(x, y)))
        )
    )

    val part1 = numbers
      .filter((n, coords) => {
        coords
          .flatMap(c => deltas.map(d => c + d))
          .toSet
          .intersect(symbols)
          .nonEmpty
      })
      .map(_._1)
      .sum

    println(s"Part 1: $part1")

    timer.checkpointPrint()
    timer.checkpoint()

    val gears = lines.zipWithIndex.flatMap((line, y) =>
      gearPattern.findAllMatchIn(line).map(m => Pair(m.start, y))
    )

    val part2 = gears
      .map(g => {
        val partNumbers = numbers
          .filter((n, coords) => {
            deltas.map(d => g + d).toSet.intersect(coords.toSet).nonEmpty
          })

        if (partNumbers.length == 2) partNumbers.map(_._1).product else 0
      })
      .sum

    println(s"Part 2: $part2")

    timer.checkpointPrint()
  }
}
