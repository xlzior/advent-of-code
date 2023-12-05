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
    numElves
  }

  def main(args: Array[String]): Unit = {
    val n: Int = FileUtils.readFileContents(args(0))(0).toInt

    println(s"Part 1: ${stealAdjacent(n)}")
    println(s"Part 1: ${stealAcross(n)}")
  }
}
