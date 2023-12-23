import scala.collection.mutable.Queue
import scala.collection.mutable.Set

import util._

object Day extends Solution {
  val deltas = List(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))

  def countPlots(garden: Grid[Char], distance: Int): Int = {
    val start = garden.find('S').get
    val targets = Set[Pair[Int]]()

    val queue = Queue[(Int, Pair[Int])]((0, start))
    val explored = Set[(Int, Pair[Int])]((0, start))

    while (queue.nonEmpty) {
      val (numSteps, curr) = queue.dequeue()

      if (numSteps == distance) {
        targets.add(curr)
      } else {
        deltas.foreach(d =>
          garden
            .get(curr + d)
            .map(char => {
              val neighbour = (numSteps + 1, curr + d)
              if (char != '#' && !explored.contains(neighbour)) {
                queue.enqueue(neighbour)
                explored.add(neighbour)
              }
            })
        )
      }
    }

    targets.size
  }

  def solve(lines: List[String]): List[Int] = {
    val grid = Grid(lines.map(_.toCharArray()).toArray)
    List(
      countPlots(grid, 6),
      countPlots(grid, 64)
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
