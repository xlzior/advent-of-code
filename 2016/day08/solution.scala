import util.FileUtils

class Pair(val x: Int, val y: Int) {
  def +(other: Pair): Pair = new Pair(x + other.x, y + other.y)
  def *(factor: Int): Pair = new Pair(x * factor, y * factor)
  def %(other: Pair): Pair = new Pair(x % other.x, y % other.y)
  def abs: Int = Math.abs(x) + Math.abs(y)
  override def equals(x: Any): Boolean = {
    x match {
      case other: Pair => this.x == other.x && this.y == other.y
      case _           => false
    }
  }
  override def hashCode(): Int = {
    val prime = 31 // Pick a prime number
    var result = 1
    result = prime * result + x.hashCode()
    result = prime * result + y.hashCode()
    result
  }
  override def toString(): String = s"($x, $y)"
}

object Solution {
  val rect = """rect (\d+)x(\d+)""".r
  val rotateColumn = """rotate column x=(\d+) by (\d+)""".r
  val rotateRow = """rotate row y=(\d+) by (\d+)""".r

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))

    val screenSize = if (args(0) == "sample.txt") Pair(7, 3) else Pair(50, 6)

    val output = lines.foldLeft(Set[Pair]())((acc, curr) =>
      curr match {
        case rect(w, h) => {
          acc ++ (0 to w.toInt - 1).flatMap(x =>
            (0 to h.toInt - 1).map(y => Pair(x, y))
          )
        }
        case rotateColumn(xStr, dyStr) => {
          val x = xStr.toInt
          val dy = dyStr.toInt

          val toShift = acc.filter(p => p.x == x)
          val shifted = toShift.map(p => (p + Pair(0, dy)) % screenSize)
          acc -- toShift ++ shifted
        }
        case rotateRow(yStr, dxStr) => {
          val y = yStr.toInt
          val dx = dxStr.toInt

          val toShift = acc.filter(p => p.y == y)
          val shifted = toShift.map(p => (p + Pair(dx, 0)) % screenSize)
          acc -- toShift ++ shifted
        }
      }
    )

    println(s"Part 1: ${output.size}")

    (0 to screenSize.y - 1).foreach(y => {
      (0 to screenSize.x - 1).foreach(x => {
        print(if (output.contains(Pair(x, y))) "█" else " ")
      })
      println()
    })
  }
}