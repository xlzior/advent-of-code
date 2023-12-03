package util

class Pair(val x: Int, val y: Int) {
  def +(other: Pair): Pair = new Pair(x + other.x, y + other.y)
  def *(factor: Int): Pair = new Pair(x * factor, y * factor)
  def %(other: Pair): Pair = new Pair(x % other.x, y % other.y)
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
