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

    if (Pair(0, 0) <= p && p < Pair(w, h)) grid(p.y)(p.x)
    else '.'
  }

  def getStart(grid: List[String]): (Pair, Char) = {
    val start = grid.zipWithIndex
      .flatMap((line, y) => {
        val x = line.indexOf("S")
        if (x >= 0) List(Pair(x, y)) else List.empty
      })(0)

    val surroundings =
      List(north, east, south, west).map(_ + start).map(p => get(grid, p))

    val pipe = surroundings match {
      case List(_, '-', '|', _) => 'F'
      case List(_, 'J', '|', _) => 'F'
      case List('7', '7', _, _) => 'L'
      case List(_, _, '|', 'F') => '7'
      case List(_, '7', 'J', _) => 'F'
      case _                    => '.'
    }

    (start, pipe)
  }

  def findLoop(grid: List[String]): Map[Pair, Char] = {
    val (start, pipe) = getStart(grid)
    var ghosts = next(pipe).values.map(dir => (start, dir))
    var pipes = Map[Pair, Char](start -> pipe)

    while (pipes.size == 1 || ghosts.map(_._1).toSet.size > 1) {
      ghosts = ghosts.map((pos, dir) => {
        val pipe = get(grid, pos + dir)
        pipes(pos + dir) = pipe
        (pos + dir, next(pipe)(dir))
      })
    }

    pipes
  }

  val eastWest = Set('-', 'F', 'L')
  val northSouth = Set('|', 'F', '7')

  def expand(pipes: Map[Pair, Char]): Map[Pair, Char] = {
    val expanded = pipes.map((coords, pipe) => (coords * 2, pipe)).toMap

    Map[Pair, Char](
      expanded
        .foldLeft(expanded)((acc, curr) => {
          val (coords, pipe) = curr
          var result = acc
          if (eastWest.contains(pipe))
            result = result.updated(coords + east, '-')
          if (northSouth.contains(pipe))
            result = result.updated(coords + south, '|')
          result
        })
        .map((coords, pipe) => (coords + Pair(1, 1), pipe))
        .toSeq: _*
    )
  }

  def floodfill(pipes: Map[Pair, Char], start: Pair, size: Pair): Set[Pair] = {
    val outside = Set[Pair](start)
    val queue = Queue[Pair](start)

    while (queue.nonEmpty) {
      val curr = queue.dequeue()

      deltas
        .map(delta => delta + curr)
        .filter(next => Pair(0, 0) <= next && next < size)
        .foreach(next => {
          if (!pipes.contains(next) && !outside.contains(next)) {
            queue.enqueue(next)
            outside.add(next)
          }
        })
    }

    outside
  }

  def gridCoordinates(start: Pair, end: Pair): IndexedSeq[Pair] =
    (start.x until end.x).flatMap(x =>
      (start.y until end.y).map(y => Pair(x, y))
    )

  def preExpansion(p: Pair): Boolean = p.y % 2 == 1 && p.x % 2 == 1

  def main(args: Array[String]): Unit = {
    val grid: List[String] = FileUtils.readFileContents(args(0))
    val H = 2 * grid.length + 1
    val W = 2 * grid(0).length + 1

    val pipes = findLoop(grid)
    val expanded = expand(pipes)
    val all = gridCoordinates(Pair(0, 0), Pair(W, H)).toSet
    val outside = floodfill(expanded, Pair(0, 0), Pair(W, H))
    val inside = (all -- outside -- expanded.keySet).filter(preExpansion)

    println(s"Part 1: ${pipes.size / 2}")
    println(s"Part 2: ${inside.size}")
  }
}
