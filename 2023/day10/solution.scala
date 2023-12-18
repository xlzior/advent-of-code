import scala.collection.mutable.Set
import scala.collection.mutable.Queue
import scala.collection.mutable.Map

import util.FileUtils
import util._

object PrintUtils {
  val bdc =
    Map('|' -> '│', '-' -> '─', 'L' -> '└', 'J' -> '┘', '7' -> '┐', 'F' -> '┌')
      .withDefault(c => c)

  def printPipes(pipes: Map[Pair[Int], Char], dimensions: Pair[Int]) = {
    (0 until dimensions.y).foreach(y => {
      (0 until dimensions.x).foreach(x => {
        print(bdc(pipes.getOrElse(Pair[Int](x, y), '.')))
      })
      println()
    })
    println()
  }

  def printMap(
      pipes: Map[Pair[Int], Char],
      dimensions: Pair[Int],
      outside: Set[Pair[Int]]
  ) = {
    (0 until dimensions.y).foreach(y => {
      (0 until dimensions.x).foreach(x => {
        val xy = Pair[Int](x, y)
        print(bdc(pipes.getOrElse(xy, if (outside.contains(xy)) 'O' else 'I')))
      })
      println()
    })

    println()
  }
}

object Day10 extends Solution {
  val north = Pair[Int](0, -1)
  val south = Pair[Int](0, 1)
  val east = Pair[Int](1, 0)
  val west = Pair[Int](-1, 0)

  val deltas = List(north, south, east, west)

  val next = Map(
    '|' -> Map(north -> north, south -> south),
    '-' -> Map(east -> east, west -> west),
    'L' -> Map(south -> east, west -> north),
    'J' -> Map(south -> west, east -> north),
    '7' -> Map(east -> south, north -> west),
    'F' -> Map(west -> south, north -> east)
  )

  def get(grid: List[String], p: Pair[Int]): Char = {
    val h = grid.length
    val w = grid(0).length

    if (Pair[Int](0, 0) <= p && p < Pair[Int](w, h)) grid(p.y)(p.x)
    else '.'
  }

  def getStart(grid: List[String]): (Pair[Int], Char) = {
    val start = grid.zipWithIndex
      .flatMap((line, y) => {
        val x = line.indexOf("S")
        if (x >= 0) List(Pair[Int](x, y)) else List.empty
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

  def findLoop(grid: List[String]): Map[Pair[Int], Char] = {
    val (start, pipe) = getStart(grid)
    var ghosts = next(pipe).values.map(dir => (start, dir))
    var pipes = Map[Pair[Int], Char](start -> pipe)

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

  def expand(pipes: Map[Pair[Int], Char]): Map[Pair[Int], Char] = {
    val expanded = pipes.map((coords, pipe) => (coords * 2, pipe)).toMap

    Map[Pair[Int], Char](
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
        .map((coords, pipe) => (coords + Pair[Int](1, 1), pipe))
        .toSeq: _*
    )
  }

  def floodfill(
      pipes: Map[Pair[Int], Char],
      start: Pair[Int],
      size: Pair[Int]
  ): Set[Pair[Int]] = {
    val outside = Set[Pair[Int]](start)
    val queue = Queue[Pair[Int]](start)

    while (queue.nonEmpty) {
      val curr = queue.dequeue()

      deltas
        .map(delta => delta + curr)
        .filter(next => Pair[Int](0, 0) <= next && next < size)
        .foreach(next => {
          if (!pipes.contains(next) && !outside.contains(next)) {
            queue.enqueue(next)
            outside.add(next)
          }
        })
    }

    outside
  }

  def gridCoordinates(start: Pair[Int], end: Pair[Int]): IndexedSeq[Pair[Int]] =
    (start.x until end.x).flatMap(x =>
      (start.y until end.y).map(y => Pair[Int](x, y))
    )

  def preExpansion(p: Pair[Int]): Boolean = p.y % 2 == 1 && p.x % 2 == 1

  def solve(grid: List[String]): List[Int] = {
    val h = grid.length
    val w = grid(0).length
    val H = 2 * h + 1
    val W = 2 * w + 1

    val pipes = findLoop(grid)
    val expanded = expand(pipes)
    val all = gridCoordinates(Pair[Int](0, 0), Pair[Int](W, H)).toSet
    val outside = floodfill(expanded, Pair[Int](0, 0), Pair[Int](W, H))
    val inside = (all -- outside -- expanded.keySet).filter(preExpansion)

    // PrintUtils.printPipes(pipes, Pair[Int](w, h))
    // PrintUtils.printPipes(expanded, Pair[Int](W, H))
    // PrintUtils.printMap(expanded, Pair[Int](W, H), outside)

    List(pipes.size / 2, inside.size)
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
