import scala.collection.mutable.Set
import scala.collection.mutable.Queue
import util.FileUtils
import util.Pair

object Solution {
  def part1(files: List[(Pair, Int, Int)]) = {
    files
      .map((a, used, _) =>
        if (used == 0) 0
        else files.count((b, _, avail) => a != b && used <= avail)
      )
      .sum
  }

  val deltas = List(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))

  def part2(files: List[(Pair, Int, Int)]): Int = {
    val maxX = files.map(_._1.x).max
    val maxY = files.map(_._1.y).max
    val goal = Pair(maxX, 0)
    val walls = files.filter(_._2 > 100).map(_._1).toSet
    val empty = files.filter(_._2 == 0).head._1
    val initialState = (empty, goal)

    val explored = Set[(Pair, Pair)]((empty, goal))
    val queue = Queue[(Int, Pair, Pair)]((0, empty, goal))
    val target = Pair(0, 0)

    while (queue.nonEmpty) {
      val (numSteps, empty, goal) = queue.dequeue()

      if (goal == target) return numSteps

      deltas.map(delta => {
        val newEmpty = empty + delta
        val newGoal = if (newEmpty == goal) empty else goal
        if (
          Pair(0, 0) <= newEmpty && newEmpty <= Pair(maxX, maxY) &&
          !walls.contains(newEmpty) &&
          !explored.contains((newEmpty, newGoal))
        ) {
          explored.add((newEmpty, newGoal))
          queue.enqueue((numSteps + 1, newEmpty, newGoal))
        }
      })
    }
    -1
  }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.read(args(0))

    val files = lines
      .drop(2)
      .flatMap(line =>
        """(\d+)""".r.findAllIn(line).map(_.toInt).toList match {
          case List(x, y, size, used, avail, usedPercent) =>
            List((Pair(x, y), used, avail))
          case _ => List.empty
        }
      )

    println(s"Part 1: ${part1(files)}")
    println(s"Part 2: ${part2(files)}")
  }
}
