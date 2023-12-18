import scala.collection.mutable.PriorityQueue
import scala.collection.mutable.Map
import scala.collection.mutable.Set

import util.Solution
import util.FileUtils
import util.Grid
import util.Pair

val up = Pair(-1, 0)
val down = Pair(1, 0)
val left = Pair(0, -1)
val right = Pair(0, 1)

class State(
    val grid: Grid[Int],
    val min: Int,
    val max: Int,
    val pos: Pair,
    val dirs: List[Pair]
) {
  val nextDirs = Map[Pair, List[Pair]](
    up -> List(left, right),
    down -> List(left, right),
    left -> List(up, down),
    right -> List(up, down)
  )

  def isGoal: Boolean = pos == Pair(grid.h - 1, grid.w - 1)

  def neighbours: List[(Int, State)] =
    dirs
      .flatMap(dir =>
        (min to max).flatMap(i => {
          val next = pos + dir * i
          if (grid.contains(next)) {
            val weight = (1 to i).map(j => grid.get(pos + dir * j).get).sum
            Some((next, weight, dir))
          } else {
            None
          }
        })
      )
      .map((next, weight, dir) => {
        (weight, State(grid, min, max, next, nextDirs(dir)))
      })

  override def equals(x: Any): Boolean = x match {
    case other: State => this.pos == other.pos && this.dirs == other.dirs
    case _            => false
  }
  override def hashCode(): Int = (pos, dirs).hashCode()

  implicit val ordering: Ordering[(Int, State)] = Ordering.by(-_._1)

  def shortestPath: Int = {
    val dist = Map(this -> 0)
    val pq = PriorityQueue((0, this))

    while (pq.nonEmpty) {
      val (cost, state) = pq.dequeue()

      if (state.isGoal)
        return cost

      state.neighbours.foreach((weight, nextState) => {
        val alt = cost + weight

        if (alt < dist.getOrElse(nextState, Int.MaxValue)) {
          dist(nextState) = alt
          pq.addOne((alt, nextState))
        }
      })
    }

    -1
  }
}

object Day extends Solution {
  def crucible(grid: Grid[Int], min: Int, max: Int): Int = {
    State(grid, min, max, Pair(0, 0), List(right, down)).shortestPath
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
