package util

class Pair(val x: Int, val y: Int) {
  val r = x
  val c = y
  def +(other: Pair): Pair = Pair(x + other.x, y + other.y)
  def -(other: Pair): Pair = Pair(x - other.x, y - other.y)
  def unary_- = Pair(-x, -y)
  def *(factor: Int): Pair = Pair(x * factor, y * factor)
  def %(other: Pair): Pair = Pair(x % other.x, y % other.y)
  def <(other: Pair): Boolean = x < other.x && y < other.y
  def >(other: Pair): Boolean = x > other.x && y > other.y
  def <=(other: Pair): Boolean = x <= other.x && y <= other.y
  def >=(other: Pair): Boolean = x >= other.x && y >= other.y

  def abs: Int = Math.abs(x) + Math.abs(y)

  override def equals(x: Any): Boolean = {
    x match {
      case other: Pair => this.x == other.x && this.y == other.y
      case _           => false
    }
  }
  override def hashCode(): Int = {
    val prime = 31 // Pick a prime number
    var result = 1
    result = prime * result + x.hashCode()
    result = prime * result + y.hashCode()
    result
  }
  override def toString(): String = s"($x, $y)"
}
