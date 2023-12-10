import util.FileUtils

object Solution {
  def generateCurve(s: String, length: Int): String = {
    var a = s
    while (a.length < length) {
      a = a + "0" + a.reverse.map(c => if (c == '0') '1' else '0')
    }
    a.slice(0, length)
  }

  def checksum(s: String): String = {
    var result = s
    while
      result = result
        .grouped(2)
        .map(pair => if (pair(0) == pair(1)) "1" else "0")
        .mkString
      result.length % 2 == 0
    do ()
    result
  }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.read(args(0))
    val puzzle = lines(0)
    val n = lines(1).toInt

    assert(generateCurve("1", 3) == "100")
    assert(generateCurve("0", 3) == "001")
    assert(generateCurve("11111", 11) == "11111000000")
    assert(generateCurve("111100001010", 25) == "1111000010100101011110000")
    assert(checksum("110010110100") == "100")

    println(s"Part 1: ${checksum(generateCurve(puzzle, n))}")
    println(s"Part 2: ${checksum(generateCurve(puzzle, 35651584))}")
  }
}
