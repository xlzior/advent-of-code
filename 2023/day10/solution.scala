import scala.collection.mutable.Set
import scala.collection.mutable.Queue

import util.FileUtils
import util.Pair

object Solution {
  val north = Pair(0, -1)
  val south = Pair(0, 1)
  val east = Pair(1, 0)
  val west = Pair(-1, 0)

  val next = Map(
    '|' -> Map(north -> north, south -> south),
    '-' -> Map(east -> east, west -> west),
    'L' -> Map(south -> east, west -> north),
    'J' -> Map(south -> west, east -> north),
    '7' -> Map(east -> south, north -> west),
    'F' -> Map(west -> south, north -> east)
  )

  def get(grid: List[String], p: Pair): Char = {
    val h = grid.length
    val w = grid(0).length

    if (Pair(0, 0) <= p && p < Pair(w, h)) {
      grid(p.y)(p.x)
    } else {
      '.'
    }
  }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))
    val start =
      lines.zipWithIndex
        .flatMap((line, y) => {
          val x = line.indexOf("S")
          if (x >= 0) List(Pair(x, y)) else List.empty
        })(0)

    val surroundings =
      List(north, east, south, west).map(_ + start).map(p => get(lines, p))

    val startPipe = surroundings match {
      case List(_, '-', '|', _) => 'F'
      case List(_, 'J', '|', _) => 'F'
      case List('7', '7', _, _) => 'L'
    }

    val startDirections = next(startPipe).values.toList
    var left = start
    var right = start
    var leftDir = startDirections(0)
    var rightDir = startDirections(1)
    var numSteps = 0

    while (numSteps == 0 || left != right) {
      left = left + leftDir
      leftDir = next(get(lines, left))(leftDir)
      right = right + rightDir
      rightDir = next(get(lines, right))(rightDir)
      numSteps += 1
    }

    println(s"Part 1: $numSteps")
  }
}
