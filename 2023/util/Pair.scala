package util
class Pair[A](val x: A, val y: A)(implicit numeric: Numeric[A]) {
  val r = x
  val c = y

  def +(other: Pair[A]): Pair[A] =
    Pair(numeric.plus(x, other.x), numeric.plus(y, other.y))

  def -(other: Pair[A]): Pair[A] =
    Pair(numeric.minus(x, other.x), numeric.minus(y, other.y))

  def *(factor: A): Pair[A] =
    Pair(numeric.times(x, factor), numeric.times(y, factor))

  def unary_- = Pair[A](numeric.negate(x), numeric.negate(y))

  def <(other: Pair[A]): Boolean =
    numeric.lt(x, other.x) && numeric.lt(y, other.y)

  def >(other: Pair[A]): Boolean =
    numeric.gt(x, other.x) && numeric.gt(y, other.y)

  def <=(other: Pair[A]): Boolean =
    numeric.lteq(x, other.x) && numeric.lteq(y, other.y)

  def >=(other: Pair[A]): Boolean =
    numeric.gteq(x, other.x) && numeric.gteq(y, other.y)

  def abs: A = numeric.plus(numeric.abs(x), numeric.abs(y))

  override def equals(x: Any): Boolean = {
    x match {
      case other: Pair[A] => this.x == other.x && this.y == other.y
      case _              => false
    }
  }
  override def hashCode(): Int = (x, y).hashCode()
  override def toString(): String = s"($x, $y)"
}
