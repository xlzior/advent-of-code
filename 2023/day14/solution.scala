import util.Solution
import util.FileUtils
import util.Pair

object Day extends Solution {
  var n = 1_000_000_000
  var dimensions = Pair(0, 0)
  var cubes = Set[Pair]()
  val directions = List(Pair(1, 0), Pair(0, -1), Pair(-1, 0), Pair(0, 1))

  def roll(spheres: List[Pair], direction: Pair): List[Pair] = {
    var newSpheres = List[Pair]()

    spheres
      .sortBy(p => -(p.x * direction.x + p.y * direction.y))
      .foreach(sphere => {
        var pos = sphere
        var nextPos = pos + direction
        while (
          Pair(1, 1) <= nextPos && nextPos <= dimensions &&
          !cubes.contains(nextPos) &&
          !newSpheres.contains(nextPos)
        ) {
          pos = nextPos
          nextPos = pos + direction
        }
        newSpheres = pos :: newSpheres
      })

    newSpheres
  }

  def calculateLoad(spheres: Iterable[Pair]): Int =
    spheres.map(p => p.x).sum

  def part1(spheres: List[Pair]): Int = {
    val rolledSpheres = roll(spheres, directions(0))
    calculateLoad(rolledSpheres)
  }

  def cycle(spheres: List[Pair]): List[Pair] = {
    var rolledSpheres = spheres
    for (direction <- directions) {
      rolledSpheres = roll(rolledSpheres, direction)
    }
    rolledSpheres
  }

  def part2(spheres: List[Pair]): Int = {
    var seen = Map[List[Pair], Int](spheres -> 0)
    var rolledSpheres = spheres
    for (i <- 1 to n) {
      if (i % 1000_000 == 0) println(s"${i / n * 100}%")
      for (direction <- directions) {
        rolledSpheres = roll(rolledSpheres, direction)
      }
      if (seen.contains(rolledSpheres)) {
        println(
          s"state $i has been seen before in state ${seen(rolledSpheres)}"
        )
        val cycle = i - seen(rolledSpheres)
        val offset = (n - i) % cycle
        println((cycle, offset))
        return 0
      }
      seen = seen.updated(rolledSpheres, i)
    }
    calculateLoad(rolledSpheres)
  }

  def getCoordinates(lines: List[String], symbol: Char): Iterable[Pair] = {
    val h = lines.length
    lines.zipWithIndex.flatMap((row, r) =>
      row.zipWithIndex.collect {
        case (char, c) if char == symbol => Pair(h - r, c + 1)
      }
    )
  }

  def solve(lines: List[String]): List[Int] = {
    dimensions = Pair(lines.length, lines(0).length)
    cubes = getCoordinates(lines, '#').toSet
    val spheres = getCoordinates(lines, 'O').toList

    List(part1(spheres), part2(spheres))
  }

  def main(args: Array[String]): Unit = {
    // assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
