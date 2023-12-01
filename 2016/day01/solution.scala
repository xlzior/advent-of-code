import util.FileUtils

class Pair(val x: Int, val y: Int) {
  def +(other: Pair): Pair = new Pair(x + other.x, y + other.y)
  def *(factor: Int): Pair = new Pair(x * factor, y * factor)
  def abs: Int = Math.abs(x) + Math.abs(y)
  override def equals(x: Any): Boolean = {
    x match {
      case other: Pair => this.x == other.x && this.y == other.y
      case _           => false
    }
  }
  override def hashCode: Int = x.hashCode + y.hashCode
}

object Solution {
  val deltas =
    Array(new Pair(0, 1), new Pair(1, 0), new Pair(0, -1), new Pair(-1, 0))

  def main(args: Array[String]): Unit = {
    val lines = FileUtils.readFileContents(args(0))

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
