package util

class Grid[A](val grid: Array[Array[A]]) {
  val h = grid.length
  val w = grid(0).length

  def contains(pos: Pair[Int]): Boolean =
    Pair[Int](0, 0) <= pos && pos < Pair[Int](h, w)

  def get(pos: Pair[Int]): Option[A] =
    if (contains(pos)) Some(grid(pos.r)(pos.c)) else None
}
