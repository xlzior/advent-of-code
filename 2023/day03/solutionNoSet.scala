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
    val h = lines.length
    val w = lines(0).length

    val timer = Timer()
    timer.checkpoint()

    var gears = Map[Pair, List[Int]]().withDefault(_ => List.empty)

    val part1 = lines.zipWithIndex
      .flatMap((line, y) =>
        numberPattern
          .findAllMatchIn(line)
          .map(m =>
            (m.group(1).toInt, (m.start to m.end - 1).map(x => Pair(x, y)))
          )
      )
      .filter((n, coords) => {
        coords
          .flatMap(c => deltas.map(d => c + d))
          .filter(p => Pair(0, 0) <= p && p < Pair(w, h))
          .exists(p => {
            if (lines(p.y)(p.x) == '*') {
              gears = gears.updated(p, n :: gears(p))
            }
            symbolPattern.matches(lines(p.y)(p.x).toString())
          })
      })
      .map(_._1)
      .sum

    println(s"Part 1: $part1")

    timer.checkpointPrint()
    timer.checkpoint()

    val part2 =
      gears.map((_, parts) => if (parts.length == 2) parts.product else 0).sum

    println(s"Part 2: $part2")

    timer.checkpointPrint()
  }
}
