package util
class Triple[A](val x: A, val y: A, val z: A)(implicit numeric: Numeric[A]) {
  def +(other: Triple[A]): Triple[A] =
    Triple(
      numeric.plus(x, other.x),
      numeric.plus(y, other.y),
      numeric.plus(z, other.z)
    )

  def -(other: Triple[A]): Triple[A] =
    Triple(
      numeric.minus(x, other.x),
      numeric.minus(y, other.y),
      numeric.minus(z, other.z)
    )

  def *(factor: A): Triple[A] =
    Triple(
      numeric.times(x, factor),
      numeric.times(y, factor),
      numeric.times(z, factor)
    )

  def unary_- =
    Triple[A](numeric.negate(x), numeric.negate(y), numeric.negate(z))

  def <(other: Triple[A]): Boolean =
    numeric.lt(x, other.x) && numeric.lt(y, other.y) && numeric.lt(z, other.z)

  def >(other: Triple[A]): Boolean =
    numeric.gt(x, other.x) && numeric.gt(y, other.y) && numeric.gt(z, other.z)

  def <=(other: Triple[A]): Boolean =
    numeric.lteq(x, other.x) &&
      numeric.lteq(y, other.y) &&
      numeric.lteq(z, other.z)

  def >=(other: Triple[A]): Boolean =
    numeric.gteq(x, other.x) &&
      numeric.gteq(y, other.y) &&
      numeric.gteq(z, other.z)

  def abs: A =
    numeric.plus(numeric.plus(numeric.abs(x), numeric.abs(y)), numeric.abs(z))

  override def equals(x: Any): Boolean = {
    x match {
      case other: Triple[A] =>
        this.x == other.x && this.y == other.y && this.z == other.z
      case _ => false
    }
  }
  override def hashCode(): Int = (x, y, z).hashCode()
  override def toString(): String = s"($x, $y, $z)"
}
