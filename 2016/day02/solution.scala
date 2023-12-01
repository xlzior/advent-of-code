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

abstract class Keypad() {
  var location: Pair
  val keypad: Array[Array[String]]

  var password: String = ""
  val deltas = Map(
    "U" -> new Pair(0, -1),
    "R" -> new Pair(1, 0),
    "D" -> new Pair(0, 1),
    "L" -> new Pair(-1, 0)
  )

  def isValid(location: Pair): Boolean

  def move(steps: String): Unit = {
    location = steps.foldLeft(location)((location, step) => {
      val newLocation = location + deltas(step.toString)
      if (isValid(newLocation)) newLocation else location
    })
    password = password + keypad(location.y)(location.x)
  }
}

class SquareKeypad() extends Keypad {
  var location: Pair = new Pair(1, 1)

  val keypad = Array(
    Array("1", "2", "3"),
    Array("4", "5", "6"),
    Array("7", "8", "9")
  )

  def isValid(location: Pair): Boolean = {
    location.x >= 0 && location.x <= 2 &&
    location.y >= 0 && location.y <= 2
  }
}

class DiamondKeypad() extends Keypad {
  var location: Pair = new Pair(0, 2)

  val keypad = Array(
    Array("0", "0", "1", "0", "0"),
    Array("0", "2", "3", "4", "0"),
    Array("5", "6", "7", "8", "9"),
    Array("0", "A", "B", "C", "0"),
    Array("0", "0", "D", "0", "0")
  )

  def isValid(location: Pair): Boolean = {
    location.x >= 0 && location.x <= 4 &&
    location.y >= 0 && location.y <= 4 &&
    keypad(location.y)(location.x) != "0"
  }
}

object Solution {
  def main(args: Array[String]): Unit = {
    val lines = FileUtils.readFileContents(args(0))

    val squareKeypad = new SquareKeypad()
    val diamondKeypad = new DiamondKeypad()

    lines.foreach(line => {
      squareKeypad.move(line)
      diamondKeypad.move(line)
    })

    println(s"Part 1: ${squareKeypad.password}")
    println(s"Part 2: ${diamondKeypad.password}")
  }
}
