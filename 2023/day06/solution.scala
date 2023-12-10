import util.FileUtils

object Solution {
  def distanceTravelled(holdTime: Long, raceTime: Long) =
    holdTime * (raceTime - holdTime)

  def numWays(time: Long, distance: Long) = {
    (1L to time - 1)
      .filter(t => distanceTravelled(t, time) > distance)
      .size
  }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.read(args(0))

    // Part 1
    val parsed = lines.map(s => """(\d+)""".r.findAllIn(s).map(_.toLong))
    val races = parsed(0).zip(parsed(1))
    val part1 = races.map(numWays).product
    println(s"Part 1: $part1")

    // Part 2
    lines.map(s => """(\d)""".r.findAllIn(s).mkString.toLong) match {
      case List(time, distance) => {
        println(s"Part 2: ${numWays(time, distance)}")
      }
      case _ => {}
    }
  }
}
