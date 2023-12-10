import util.FileUtils
import util.Pair

object Solution {
  val deltas =
    Array(new Pair(0, 1), new Pair(1, 0), new Pair(0, -1), new Pair(-1, 0))

  def main(args: Array[String]): Unit = {
    val lines = FileUtils.read(args(0))

    var part2Done = false

    val instructions = lines(0)
      .split(", ")
      .map(s =>
        s match {
          case s if s.startsWith("L") => (-1, s.substring(1).toInt)
          case s if s.startsWith("R") => (1, s.substring(1).toInt)
        }
      )
      .foldLeft((new Pair(0, 0), 0, Set()))((acc, instruction) => {
        val (turn, distance) = instruction
        val (location, facing, visited) = acc

        val newFacing = (facing + turn + 4) % 4
        val newVisited = (1 to distance).foldLeft(visited)((acc, i) => {
          val newLocation = location + deltas(newFacing) * i
          if (visited.contains(newLocation) && !part2Done) {
            println(s"Part 2: ${newLocation.abs}")
            part2Done = true
          }
          acc + newLocation
        })

        (location + deltas(newFacing) * distance, newFacing, newVisited)
      })

    println(s"Part 1: ${instructions._1.abs}")
  }
}
