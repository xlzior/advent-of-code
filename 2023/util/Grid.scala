package util

class Grid[A](val grid: Array[Array[A]]) {
  val h = grid.length
  val w = grid(0).length

  def isValid(pos: Pair): Boolean = Pair(0, 0) <= pos && pos < Pair(h, w)

  def get(pos: Pair): Option[A] =
    if (isValid(pos)) Some(grid(pos.r)(pos.c)) else None
}
