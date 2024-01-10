import util._

object Day extends Solution {
  def solveEquations(
      a: Long,
      b: Long,
      c: Long,
      d: Long,
      e: Long,
      f: Long
  ): (Double, Double) = {
    // ax + by = c
    // dx + ey = f
    val y = (c * d - a * f).toDouble / (b * d - a * e)
    val x = (c - b * y) / a.toDouble
    (x, y)
  }

  def countIntersections(
      lines: List[String],
      boundaryMin: Long,
      boundaryMax: Long
  ): Int = {
    val hail = lines.map(line => {
      val Array(px, py, pz, vx, vy, vz) =
        """-?\d+""".r.findAllIn(line).toArray.map(_.toLong)
      val pos = Pair(px, py)
      val vel = Pair(vx, vy)
      (pos, vel)
    })

    hail
      .combinations(2)
      .map {
        case List((pos1, vel1), (pos2, vel2)) => {
          val (s, t) = solveEquations(
            vel1.x,
            -vel2.x,
            pos2.x - pos1.x,
            vel1.y,
            -vel2.y,
            pos2.y - pos1.y
          )

          // println(s"Hailstone A: $pos1 @ $vel1")
          // println(s"Hailstone B: $pos2 @ $vel2")
          // println(s"s=$s, t=$t")
          val ix = pos1.x + s * vel1.x
          val iy = pos1.y + s * vel1.y
          // println(s"$ix, $iy")
          (s, t, ix, iy)
        }
        case _ => {
          println("weh")
        }
      }
      .count {
        case (s, t, ix, iy) => {
          s > 0 && t > 0 && boundaryMin <= ix && ix <= boundaryMax && boundaryMin <= iy && iy <= boundaryMax
        }
      }
  }

  def part2(lines: List[String]): Int = {
    -1
  }

  def solve(lines: List[String]): List[Int] = {
    List(
      countIntersections(lines, 7, 27),
      countIntersections(lines, 200000000000000L, 400000000000000L)
    )
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
