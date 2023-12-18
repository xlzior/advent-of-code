import util._

object Day extends Solution {
  val instruction = """([UDLR]) (\d+) \(#([a-f0-9]{6})\)""".r

  val dirMap = Map(
    "U" -> Pair[Long](-1, 0),
    "D" -> Pair[Long](1, 0),
    "L" -> Pair[Long](0, -1),
    "R" -> Pair[Long](0, 1)
  )
  val dirString = "RDLU"

  def findArea(instructions: List[(String, Long)]): Long = {
    val (border, points, _) = instructions
      .foldLeft((0L, List[Pair[Long]](), Pair[Long](0, 0))) {
        case ((border, points, pos), (dir, count)) => {
          val next = pos + dirMap(dir) * count
          (border + count, next :: points, next)
        }
      }

    val xs = points.map(_.x)
    val ys = points.map(_.y)
    val s1 = xs.zip(ys.drop(1) :+ ys.head).map((x, y) => x * y).sum
    val s2 = ys.zip(xs.drop(1) :+ xs.head).map((x, y) => x * y).sum

    (s1 - s2).abs / 2 + border / 2 + 1
  }

  def part1(lines: List[String]): Long = {
    val instructions = lines.map(_ match {
      case instruction(udlr, count, _) => (udlr, count.toLong)
    })

    findArea(instructions)
  }

  def part2(lines: List[String]): Long = {
    val instructions = lines.map(_ match {
      case instruction(_, _, colour) =>
        (
          dirString(colour.last.asDigit).toString(),
          Integer.parseInt(colour.slice(0, 5), 16).toLong,
        )
    })

    findArea(instructions)
  }

  def solve(lines: List[String]): List[Long] = {
    List(part1(lines), part2(lines))
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
