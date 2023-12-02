import util.FileUtils

object Solution {
  val pattern = """\((\d+)x(\d+)\)""".r
  def main(args: Array[String]): Unit = {
    val line = args(0)

    var decompressed = ""
    var i = 0
    while (i < line.length()) {
      if (line(i) == '(') {
        val marker = pattern.findFirstMatchIn(line.substring(i))
        val length = marker.get.group(1).toInt
        val repetitions = marker.get.group(2).toInt

        while (line(i) != ')') {
          i += 1
        }
        i += 1 // skip ')'
        decompressed += line.substring(i).substring(0, length) * repetitions
        i += length
      } else {
        decompressed += line(i)
        i += 1
      }
    }

    println(s"Part 1: ${decompressed.length()}")
  }
}
