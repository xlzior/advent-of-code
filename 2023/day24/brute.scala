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

  def parse(lines: List[String]): List[(Triple[Long], Triple[Long])] = {
    lines.map(line => {
      val Array(px, py, pz, vx, vy, vz) =
        """-?\d+""".r.findAllIn(line).toArray.map(_.toLong)
      val pos = Triple(px, py, pz)
      val vel = Triple(vx, vy, vz)
      (pos, vel)
    })

  }

  def countIntersections(
      hail: List[(Triple[Long], Triple[Long])],
      boundaryMin: Long,
      boundaryMax: Long
  ): Int = {
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

          val ix = pos1.x + s * vel1.x
          val iy = pos1.y + s * vel1.y
          (s, t, ix, iy)
        }
        case _ => {}
      }
      .count {
        case (s, t, ix, iy) => {
          s > 0 && t > 0 && boundaryMin <= ix && ix <= boundaryMax && boundaryMin <= iy && iy <= boundaryMax
        }
      }
  }

  def intersects(
      pos: Triple[Long],
      vel: Triple[Long],
      p: Triple[Long],
      v: Triple[Long]
  ): Boolean = {
    if (v.x == vel.x || v.y == vel.y || v.z == vel.z) {
      false
    } else {
      val tx = (pos.x - p.x).toDouble / (v.x - vel.x).toDouble
      val ty = (pos.y - p.y).toDouble / (v.y - vel.y).toDouble
      val tz = (pos.z - p.z).toDouble / (v.z - vel.z).toDouble
      val result = tx == ty && ty == tz && tx > 0
      // if (result) println(((pos.x - p.x), (v.x - vel.x), tx))

      result
    }
  }

  def part2(hails: List[(Triple[Long], Triple[Long])]): Long = {
    val (p, v) = hails(0)
    val scope = 300

    for (dx <- -scope to scope) {
      println(dx)
      for (dy <- -scope to scope) {
        for (dz <- -scope to scope) {
          val vel = Triple(dx.toLong, dy.toLong, dz.toLong)
          for (t <- 0 to scope) {
            // pos + vel * t = p + v * t
            // pos = p + v * t - vel * t
            val pos = p + v * t - vel * t
            val intersections = hails.tail.toStream
              .map((p2, v2) => intersects(pos, vel, p2, v2))
              .dropWhile(identity)

            if (intersections.headOption.isEmpty) {
              return pos.x + pos.y + pos.z
            }
          }
        }
      }
    }
    -1
  }

  def solve(lines: List[String]): List[Long] = {
    val hail = parse(lines)
    List(
      countIntersections(hail, 7, 27).toLong,
      countIntersections(hail, 200000000000000L, 400000000000000L).toLong,
      part2(hail)
    )
  }

  def main(args: Array[String]): Unit = {
    // assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    solution.zipWithIndex.foreach((soln, i) => {
      println(s"Part ${i + 1}: $soln")
    })
  }
}
