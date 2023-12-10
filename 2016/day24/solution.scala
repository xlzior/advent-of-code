import scala.collection.mutable.Queue
import scala.collection.mutable.Map
import scala.collection.mutable.Set as MutableSet

import util.FileUtils
import util.Pair

object Solution {
  val deltas = List(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))

  def bfs(grid: List[String], start: Pair, pois: Set[Pair]): Map[Pair, Int] = {
    val h = grid.length
    val w = grid(0).length
    val explored = MutableSet[Pair](start)
    val queue = Queue[(Int, Pair)]((0, start))
    val shortestPaths = Map[Pair, Int]()

    while (queue.nonEmpty) {
      val (numSteps, curr) = queue.dequeue()

      if (pois.contains(curr) && !shortestPaths.contains(curr)) {
        shortestPaths(curr) = numSteps
      }

      if (shortestPaths.size == pois.size) {
        return shortestPaths
      }

      deltas
        .map(delta => curr + delta)
        .filter(next =>
          Pair(0, 0) <= next && next < Pair(w, h) && grid(next.y)(next.x) != '#'
        )
        .foreach(next => {
          if (!explored.contains(next)) {
            explored.add(next)
            queue.enqueue((numSteps + 1, next))
          }
        })
    }

    shortestPaths
  }

  def main(args: Array[String]): Unit = {
    val grid: List[String] = FileUtils.read(args(0))

    val pois = grid.zipWithIndex
      .flatMap((line, y) =>
        """\d""".r
          .findAllMatchIn(line)
          .map(m => (m.group(0), Pair(m.start, y)))
          .toList
      )
      .toSet
    val start = pois.find(_._1 == "0").get._2
    val poiCoordinates = pois.map(_._2)

    val apsp =
      poiCoordinates.map(poi => (poi, bfs(grid, poi, poiCoordinates))).toMap
    val excludingStart = poiCoordinates - start

    val part1 = excludingStart.toList.permutations
      .map(visitingOrder =>
        (start :: visitingOrder)
          .sliding(2)
          .map { case List(a, b) => apsp(a)(b) }
          .sum
      )
      .min

    val part2 = excludingStart.toList.permutations
      .map(visitingOrder =>
        (start :: (visitingOrder :+ start))
          .sliding(2)
          .map { case List(a, b) => apsp(a)(b) }
          .sum
      )
      .min

    println(s"Part 1: $part1")
    println(s"Part 2: $part2")
  }
}
