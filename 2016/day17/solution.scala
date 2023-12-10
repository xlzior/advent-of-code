import java.security.MessageDigest

import scala.collection.mutable.Queue
import scala.collection.mutable.Set

import util.FileUtils
import util.Pair
import scala.compiletime.ops.long

def md5(s: String): String = {
  MessageDigest
    .getInstance("MD5")
    .digest(s.getBytes)
    .map("%02X".format(_))
    .mkString
}

class State(val path: String, val location: Pair) {
  val deltas = Map(
    "U" -> Pair(0, -1),
    "D" -> Pair(0, 1),
    "L" -> Pair(-1, 0),
    "R" -> Pair(1, 0)
  )

  def isGoal: Boolean = location == Pair(3, 3)

  def steps: Iterable[String] =
    List("U", "D", "L", "R")
      .zip(md5(path).take(4))
      .filter((_, c) => "BCDEF".contains(c)) // door open
      .map(_._1)
      .filter(d => { // within grid
        val nextLocation = location + deltas(d)
        Pair(0, 0) <= nextLocation && nextLocation <= Pair(3, 3)
      })

  def go(direction: String): State =
    State(path + direction, location + deltas(direction))

  def neighbours: Iterable[State] = steps.map(go)
}

object Solution {
  def explore(start: String): (String, Int) = {
    val state = State(start, Pair(0, 0))
    val explored = Set[State](state)
    val queue = Queue[State](state)

    var shortestPath = ""
    var longestPath = ""

    while (queue.nonEmpty) {
      val current = queue.dequeue()

      if (current.isGoal) {
        val currentPath = current.path.replace(start, "")
        if (shortestPath.isEmpty) shortestPath = currentPath
        longestPath = currentPath
      } else {
        current.neighbours.map(neighbour => {
          if (!explored.contains(neighbour)) {
            explored.add(neighbour)
            queue.enqueue(neighbour)
          }
        })
      }
    }

    return (shortestPath, longestPath.length)
  }

  def main(args: Array[String]): Unit = {
    val puzzle: String = FileUtils.read(args(0))(0)

    assert(explore("ihgpwlah") == ("DDRRRD", 370))
    assert(explore("kglvqrro") == ("DDUDRLRRUDRD", 492))
    assert(explore("ulqzkmiv") == ("DRURDRUDDLLDLUURRDULRLDUUDDDRR", 830))

    val (shortestPath, longestPath) = explore(puzzle)
    println(s"Part 1: ${shortestPath}")
    println(s"Part 2: ${longestPath}")
  }
}
