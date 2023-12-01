import util.FileUtils

object Solution {
  def checksum(name: String): String = {
    val charCounts =
      name.replaceAll("-", "").groupBy(identity).mapValues(_.length)
    charCounts.toList
      .sortBy { case (char, count) => (-count, char) }
      .take(5)
      .map(_._1)
      .mkString
  }

  def decrypt(name: String, sectorId: Int): String = {
    name.map {
      case '-' => ' '
      case c =>
        val offset = sectorId % 26
        val newChar = (c.toInt + offset) % ('z'.toInt + 1)
        if (newChar < 'a'.toInt) {
          (newChar + 'a'.toInt).toChar
        } else {
          newChar.toChar
        }
    }.mkString
  }

  def main(args: Array[String]): Unit = {
    val lines = FileUtils.readFileContents(args(0))

    val roomPattern = """([a-z-]+)-(\d+)\[(\w+)\]""".r
    val part1 = lines.map {
      case roomPattern(name: String, sectorId: String, sum: String) =>
        if (decrypt(name, sectorId.toInt).contains("northpole")) {
          println(s"Part 2: $sectorId")
        }
        if (checksum(name) == sum) sectorId.toInt else 0
    }.sum

    println(s"Part 1: $part1")
  }
}
