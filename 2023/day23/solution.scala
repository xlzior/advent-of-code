import scala.collection.mutable
import util._

object Day extends Solution {
  val up = Pair(-1, 0)
  val down = Pair(1, 0)
  val left = Pair(0, -1)
  val right = Pair(0, 1)

  val neighbours = Map(
    '.' -> List(up, down, left, right),
    '^' -> List(up),
    'v' -> List(down),
    '<' -> List(left),
    '>' -> List(right)
  )

  def part1(grid: Grid[Char]): Int = {
    val start = Pair(0, 1)
    val end = Pair(grid.h - 1, grid.w - 2)

    val queue = mutable.Queue((0, Set[Pair[Int]](), start))
    var maxSteps = 0

    while (queue.nonEmpty) {
      val (numSteps, path, curr) = queue.dequeue()

      if (curr == end) maxSteps = numSteps
      else {
        neighbours(grid.get(curr).get)
          .map(_ + curr)
          .filter(next =>
            !path.contains(next) &&
              grid.get(next).map(_ != '#').getOrElse(false)
          )
          .foreach(next => {
            queue.enqueue((numSteps + 1, path + curr, next))
          })
      }
    }

    maxSteps
  }

  def part2(grid: Grid[Char]): Int = {
    val checkpoints = grid.iterator
      .filter(curr => {
        val cell = grid.get(curr).getOrElse('#')
        val neighbours = List(up, down, left, right)
          .map(dir => grid.get(curr + dir).getOrElse('#'))
          .count(_ != '#')

        cell != '#' && neighbours != 2
      })
      .toSet

    val graph = mutable.Map[Pair[Int], mutable.Map[Pair[Int], Int]]()

    checkpoints.foreach(checkpoint => {
      val queue = mutable.Queue((0, checkpoint))
      val visited = mutable.Set(checkpoint)

      while (queue.nonEmpty) {
        val (numSteps, curr) = queue.dequeue()

        if (checkpoint != curr && checkpoints.contains(curr)) {
          if (!graph.contains(checkpoint))
            graph(checkpoint) = mutable.Map[Pair[Int], Int]()
          if (!graph.contains(curr))
            graph(curr) = mutable.Map[Pair[Int], Int]()

          graph(checkpoint)(curr) = numSteps
          graph(curr)(checkpoint) = numSteps
        } else {
          List(up, down, left, right).foreach(dir => {
            val next = curr + dir
            if (
              grid.get(next).getOrElse('#') != '#' &&
              !visited.contains(next)
            ) {
              queue.enqueue((numSteps + 1, next))
              visited.add(next)
            }
          })
        }
      }
    })

    val start = Pair(0, 1)
    val end = Pair(grid.h - 1, grid.w - 2)
    val queue = mutable.Queue((0, Set[Pair[Int]](), start))
    val prune =
      mutable.Map[Set[Pair[Int]], Int]().withDefault(_ => 0)
    var maxSteps = 0

    while (queue.nonEmpty) {
      val (numSteps, path, curr) = queue.dequeue()

      if (curr == end) maxSteps = maxSteps.max(numSteps)
      else {
        graph(curr)
          .filter((next, weight) =>
            !path.contains(next) &&
              numSteps + weight > prune(path + curr)
          )
          .foreach((next, weight) => {
            queue.enqueue((numSteps + weight, path + curr, next))
            prune(path + curr) = numSteps + weight
          })
      }
    }

    maxSteps
  }

  def solve(lines: List[String]): List[Int] = {
    val grid = Grid(lines.map(_.toCharArray()).toArray)
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
