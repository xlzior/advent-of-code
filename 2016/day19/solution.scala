import util.FileUtils

object Solution {
  def stealAdjacent(numElves: Int): Int = {
    var n = numElves
    var r = 0
    var elves = Stream.from(1, 1).take(n)

    while (n > 1) {
      elves = elves.zipWithIndex.filter((_, i) => i % 2 == r).map(_._1).force
      r = (r + n) % 2
      n = elves.length
    }

    elves.head
  }

  def stealAcross(numElves: Int): Int = {
    // index of the first victim
    val firstVictim = numElves / 2

    val firstSafe = if (numElves % 2 == 0) firstVictim + 2 else firstVictim + 1
    var r = firstSafe % 3

    var n = numElves
    var elves = Stream
      .from(1, 1)
      .take(n)
      .zipWithIndex
      .filter((_, i) => i < firstVictim || i % 3 == r)
      .map(_._1)
      .force
    r = (r - (n % 3) + 3) % 3
    n = elves.length

    while (n > 1) {
      elves = elves.zipWithIndex.filter((_, i) => i % 3 == r).map(_._1).force
      r = (r - (n % 3) + 3) % 3
      n = elves.length
    }

    elves.head
  }

  def main(args: Array[String]): Unit = {
    val n: Int = FileUtils.readFileContents(args(0))(0).toInt

    println(s"Part 1: ${stealAdjacent(n)}")

    assert(stealAcross(5) == 2)
    assert(stealAcross(11) == 2)
    assert(stealAcross(12) == 3)
    println(s"Part 2: ${stealAcross(n)}")
  }
}
