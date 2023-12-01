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
  def part1(lines: List[String]): Int = {
    val keypad = Array(
      Array(1, 2, 3),
      Array(4, 5, 6),
      Array(7, 8, 9)
    )

    val deltas = Map(
      "U" -> new Pair(0, -1),
      "R" -> new Pair(1, 0),
      "D" -> new Pair(0, 1),
      "L" -> new Pair(-1, 0)
    )

    lines
      .foldLeft(Pair(1, 1), 0)((acc, line) => {
        val (location, password) = acc
        val newLocation = line.foldLeft(location)((currentLocation, c) => {
          val newLocation = currentLocation + deltas(c.toString)
          if (
            newLocation.x < 0 || newLocation.x > 2 ||
            newLocation.y < 0 || newLocation.y > 2
          ) {
            currentLocation
          } else {
            newLocation
          }
        })
        (newLocation, password * 10 + keypad(newLocation.y)(newLocation.x))
      })
      ._2
  }

  def part2(lines: List[String]): String = {
    val keypad = Array(
      Array("0", "0", "1", "0", "0"),
      Array("0", "2", "3", "4", "0"),
      Array("5", "6", "7", "8", "9"),
      Array("0", "A", "B", "C", "0"),
      Array("0", "0", "D", "0", "0")
    )

    val deltas = Map(
      "U" -> new Pair(0, -1),
      "R" -> new Pair(1, 0),
      "D" -> new Pair(0, 1),
      "L" -> new Pair(-1, 0)
    )

    lines
      .foldLeft(Pair(0, 2), "")((acc, line) => {
        val (location, password) = acc
        val newLocation = line.foldLeft(location)((currentLocation, c) => {
          val newLocation = currentLocation + deltas(c.toString)
          if (
            newLocation.x < 0 || newLocation.x > 4 ||
            newLocation.y < 0 || newLocation.y > 4 ||
            keypad(newLocation.y)(newLocation.x) == "0"
          ) {
            currentLocation
          } else {
            newLocation
          }
        })
        (newLocation, password + keypad(newLocation.y)(newLocation.x).toString)
      })
      ._2
  }

  def main(args: Array[String]): Unit = {
    val lines = FileUtils.readFileContents(args(0))
    println(s"Part 1: ${part1(lines)}")
    println(s"Part 1: ${part2(lines)}")
  }
}
