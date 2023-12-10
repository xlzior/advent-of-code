import scala.collection.mutable.Set
import scala.collection.mutable.Queue
import scala.collection.mutable.Map

import util.FileUtils
import util.Pair

object Solution {
  val north = Pair(0, -1)
  val south = Pair(0, 1)
  val east = Pair(1, 0)
  val west = Pair(-1, 0)

  val deltas = List(north, south, east, west)

  val bdc =
    Map(
      '|' -> '│',
      '-' -> '─',
      'L' -> '└',
      'J' -> '┘',
      '7' -> '┐',
      'F' -> '┌',
      '.' -> '.'
    )

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

    val h = lines.length
    val w = lines(0).length

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
      case List(_, _, '|', 'F') => '7'
      case List(_, '7', 'J', _) => 'F'
    }

    val startDirections = next(startPipe).values.toList
    var left = start
    var right = start
    var leftDir = startDirections(0)
    var rightDir = startDirections(1)
    var numSteps = 0
    val pipes = Map[Pair, Char](start -> startPipe)

    while (numSteps == 0 || left != right) {
      left = left + leftDir
      leftDir = next(get(lines, left))(leftDir)
      pipes(left) = get(lines, left)

      right = right + rightDir
      rightDir = next(get(lines, right))(rightDir)
      pipes(right) = get(lines, right)

      numSteps += 1
    }

    println(s"Part 1: $numSteps")

    val withoutJunk = lines.zipWithIndex.map((line, y) =>
      line.zipWithIndex
        .map((char, x) => if (pipes.contains(Pair(x, y))) char else '.')
        .mkString
    )

    val eastWest = Set('-', 'F', 'L')
    val northSouth = Set('|', 'F', '7')

    val expanded = pipes.map((coords, pipe) => (coords * 2, pipe)).toMap
    val connected = expanded.foldLeft(expanded)((acc, curr) => {
      val (coords, pipe) = curr
      var result = acc
      if (eastWest.contains(pipe)) result = result.updated(coords + east, '-')
      if (northSouth.contains(pipe))
        result = result.updated(coords + south, '|')
      result
    })
    val padded =
      connected.map((coords, pipe) => (coords + Pair(1, 1), pipe)).toMap

    // println((expanded.size, connected.size, padded.size))

    val display = bdc.foldLeft(withoutJunk.mkString("\n"))((acc, chars) =>
      acc.replace(chars._1, chars._2)
    )

    // println(display)

    // (0 to 2 * h + 1).foreach(y => {
    //   (0 to 2 * w + 1).foreach(x => {
    //     print(bdc(padded.getOrElse(Pair(x, y), '.')))
    //   })
    //   println()
    // })

    val outside = Set[Pair](Pair(0, 0))
    val queue = Queue[Pair](Pair(0, 0))
    while (queue.nonEmpty) {
      val curr = queue.dequeue()

      deltas
        .map(delta => delta + curr)
        .filter(next => Pair(0, 0) <= next && next < Pair(2 * w + 1, 2 * h + 1))
        .foreach(next => {
          if (padded.getOrElse(next, '.') == '.' && !outside.contains(next)) {
            queue.enqueue(next)
            outside.add(next)
          }
        })
    }

    val all = (0 until 2 * h + 1)
      .flatMap(y => (0 until 2 * w + 1).map(x => Pair(x, y)))
      .toSet
    val inside = all -- outside.toSet -- padded.keySet

    // println(inside.size)

    val insideFiltered = inside.filter(p => p.y % 2 == 1 && p.x % 2 == 1)

    println(insideFiltered.size)
  }
}
