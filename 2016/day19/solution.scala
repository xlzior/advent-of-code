import util.FileUtils

object Solution {
  def stealAdjacent(numElves: Int): Int = {
    var elves = List.range(1, numElves + 1)
    var r = 0
    var n = numElves

    while (n > 1) {
      elves = elves.zipWithIndex.filter((_, i) => i % 2 == r).map(_._1)
      r = (r + n) % 2
      n = elves.length
    }

    elves.head
  }

  def stealAcross(numElves: Int): Int = {
    var firstVictim = numElves / 2

    var elves = List.range(1, numElves + 1)
    var r = math.floorMod(2 - numElves, 3)
    var n = elves.length

    while (n > 1) {
      elves = elves.zipWithIndex
        .filter((_, i) => i < firstVictim || i % 3 == r)
        .map(_._1)
      r = math.floorMod(r - n, 3)
      n = elves.length
      firstVictim = 0 // after the first round, no elf is safe
    }

    elves.head
  }

  def main(args: Array[String]): Unit = {
    val n: Int = FileUtils.readFileContents(args(0))(0).toInt

    println(s"Part 1: ${stealAdjacent(n)}")

    assert(stealAcross(5) == 2)
    assert(stealAcross(11) == 2)
    assert(stealAcross(12) == 3)
    assert(stealAcross(13) == 4)
    println(s"Part 2: ${stealAcross(n)}")
  }
}
