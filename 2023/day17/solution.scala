import util._

val up = Pair[Int](-1, 0)
val down = Pair[Int](1, 0)
val left = Pair[Int](0, -1)
val right = Pair[Int](0, 1)

class CrucibleState(
    val grid: Grid[Int],
    val min: Int,
    val max: Int,
    val pos: Pair[Int],
    val dirs: List[Pair[Int]]
) extends State {
  val nextDirs = Map[Pair[Int], List[Pair[Int]]](
    up -> List(left, right),
    down -> List(left, right),
    left -> List(up, down),
    right -> List(up, down)
  )

  def isGoal: Boolean = pos == Pair[Int](grid.h - 1, grid.w - 1)

  def neighbours: List[(Int, CrucibleState)] =
    dirs
      .flatMap(dir => (min to max).map(i => (dir, i)))
      .flatMap((dir, i) => {
        val next = pos + dir * i
        if (grid.contains(next)) {
          val weight = (1 to i).map(j => grid.get(pos + dir * j).get).sum
          Some((next, weight, dir))
        } else {
          None
        }
      })
      .map((next, weight, dir) => {
        (weight, CrucibleState(grid, min, max, next, nextDirs(dir)))
      })

  override def equals(x: Any): Boolean = x match {
    case other: CrucibleState =>
      this.pos == other.pos && this.dirs == other.dirs
    case _ => false
  }
  override def hashCode(): Int = (pos, dirs).hashCode()
}

object Day extends Solution {
  def crucible(grid: Grid[Int], min: Int, max: Int): Int = {
    CrucibleState(
      grid,
      min,
      max,
      Pair[Int](0, 0),
      List(right, down)
    ).shortestPath
  }

  def part1(grid: Grid[Int]): Int = crucible(grid, 1, 3)
  def part2(grid: Grid[Int]): Int = crucible(grid, 4, 10)

  def solve(lines: List[String]): List[Int] = {
    val grid = Grid(lines.map(_.toCharArray().map(_.asDigit)).toArray)
    List(part1(grid), part2(grid))
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
