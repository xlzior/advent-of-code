import util.FileUtils

object Solution {
  val pattern = """\((\d+)x(\d+)\)""".r

  def version1(line: String): Int = {
    var decompressed = ""
    var i = 0
    while (i < line.length()) {
      if (line(i) == '(') {
        val marker = pattern.findFirstMatchIn(line.substring(i))
        val length = marker.get.group(1).toInt
        val repetitions = marker.get.group(2).toInt

        while (line(i) != ')') i += 1
        i += 1 // skip ')'
        decompressed += line.substring(i).substring(0, length) * repetitions
        i += length // skip the original
      } else {
        decompressed += line(i)
        i += 1
      }
    }

    decompressed.length()
  }

  def version2(line: String): Long = {
    if (!line.contains('(')) {
      // base case: no more decompression
      return line.length()
    }

    // recursive case: contains a marker
    val marker = pattern.findFirstMatchIn(line)
    val length = marker.get.group(1).toInt
    val repetitions = marker.get.group(2).toInt

    val before = line.substring(0, marker.get.start)
    val sequence = line.substring(marker.get.end).take(length)
    val after = line.substring(marker.get.end + length)

    return List(
      version2(before),
      version2(sequence) * repetitions,
      version2(after)
    ).sum
  }

  def main(args: Array[String]): Unit = {
    val puzzle: String = FileUtils.readFileContents(args(0))(0)

    assert(version1("ADVENT") == 6)
    assert(version1("A(1x5)BC") == 7)
    assert(version1("(3x3)XYZ") == 9)
    assert(version1("A(2x2)BCD(2x2)EFG") == 11)
    assert(version1("(6x1)(1x3)A") == 6)
    assert(version1("X(8x2)(3x3)ABCY") == 18)
    println(s"Part 1: ${version1(puzzle)}")

    assert(version2("(3x3)XYZ") == 9)
    assert(version2("X(8x2)(3x3)ABCY") == 20)
    assert(version2("(27x12)(20x12)(13x14)(7x10)(1x12)A") == 241920)
    assert(
      version2(
        "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN"
      ) == 445
    )
    println(s"Part 2: ${version2(puzzle)}")
  }
}
