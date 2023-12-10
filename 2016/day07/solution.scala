import util.FileUtils

object Solution {
  val ABBA_PATTERN = """(\w)(?!\1)(\w)\2\1""".r
  val BOXED_ABBA_PATTERN = """\[[^\]]*?(\w)(?!\1)(\w)\2\1[^\[]*\]""".r
  val ABA_PATTERN = """(?=((\w)(?!\2)(\w)\2))""".r

  def supportsTLS(s: String): Boolean = {
    if (BOXED_ABBA_PATTERN.findFirstIn(s).isDefined) return false

    ABBA_PATTERN.findFirstIn(s).isDefined
  }

  def supportsSSL(s: String): Boolean = {
    val splitted = s.split(']').map(_.split('[')).transpose
    val supernet = splitted(0)
    val hypernet = splitted(1)

    val supernetABA = supernet.flatMap(s => ABA_PATTERN.findAllMatchIn(s))
    val hypernetABA =
      hypernet.flatMap(h => ABA_PATTERN.findAllMatchIn(h).map(_.group(1)))

    supernetABA.exists(aba => {
      val a = aba.group(2)
      val b = aba.group(3)
      a != b && hypernetABA.contains(b + a + b)
    })
  }

  def main(args: Array[String]): Unit = {
    val lines = FileUtils.read(args(0))

    println(s"Part 1: ${lines.count(supportsTLS)}")
    println(s"Part 2: ${lines.count(supportsSSL)}")
  }
}
