package util

class Grid[A](val grid: Array[Array[A]]) {
  val h = grid.length
  val w = grid(0).length

  val iterator = (0 until h).flatMap(r => (0 until w).map(c => Pair(r, c)))

  def contains(pos: Pair[Int]): Boolean =
    Pair[Int](0, 0) <= pos && pos < Pair[Int](h, w)

  def get(pos: Pair[Int]): Option[A] =
    if (contains(pos)) Some(grid(pos.r)(pos.c)) else None

  def find(target: A): Option[Pair[Int]] =
    grid.zipWithIndex
      .flatMap((row, r) =>
        row.zipWithIndex.collect { case (x, c) if x == target => Pair(r, c) }
      )
      .headOption
}
