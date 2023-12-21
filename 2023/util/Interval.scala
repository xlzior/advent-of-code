package util

class Interval[T](val start: T, val end: T) {
  def +(other: T)(implicit num: Numeric[T]): Interval[T] = {
    Interval(num.plus(start, other), num.plus(end, other))
  }

  def size(implicit num: Numeric[T]): T = num.minus(end, start)

  def contains(value: T)(implicit ord: Ordering[T]): Boolean = {
    ord.lteq(start, value) && ord.lt(value, end)
  }

  def splitBy(value: T)(implicit ord: Ordering[T]): Boolean = {
    start != value && contains(value)
  }

  def intersects(other: Interval[T])(implicit ord: Ordering[T]): Boolean = {
    ord.lt(start, other.end) && ord.lt(other.start, end)
  }

  /** Splits an Interval by a value
    *
    * @param value
    *   to split the Interval by
    * @return
    *   list of Intervals following the splitting
    */
  def split(value: T)(implicit ord: Ordering[T]): List[Interval[T]] = {
    if (splitBy(value)) {
      List(Interval(start, value), Interval(value, end))
    } else {
      List(this)
    }
  }

  /** Splits an Interval by another Interval
    *
    * @param other
    *   Interval to split by
    * @return
    *   list of Intervals following the splitting
    */
  def split(other: Interval[T])(implicit ord: Ordering[T]): List[Interval[T]] =
    split(other.start).flatMap(interval =>
      if (interval.start == other.end) {
        // other does not actually contain end; they do not actually intersect
        List(interval)
      } else {
        interval.split(other.end)
      }
    )

  override def toString(): String = s"[$start, $end)"
}
