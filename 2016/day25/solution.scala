import util.FileUtils

object Solution {
  val cpy = """cpy (.+) ([abcd])""".r
  val inc = """inc ([abcd])""".r
  val dec = """dec ([abcd])""".r
  val jnz = """jnz (.+) (-?\d+)""".r
  val out = """out ([abcd])""".r

  def run(
      program: List[String],
      initialState: Map[String, Int],
      n: Int
  ): List[Int] = {
    var registers = initialState
    var output = List[Int]()
    var i = 0
    while (output.length < n) {
      program(i) match {
        case cpy(x, y) =>
          registers = registers.updated(y, registers.getOrElse(x, x.toInt))
        case inc(x) => registers = registers.updated(x, registers(x) + 1)
        case dec(x) => registers = registers.updated(x, registers(x) - 1)
        case jnz(x, y) => {
          if (registers.getOrElse(x, x.toInt) != 0) {
            i += y.toInt - 1
          }
        }
        case out(x) => output = registers.get(x).get :: output
      }
      i += 1
    }

    output.reverse
  }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))

    val n = 12 // output has a periodicity of 12
    val target = "01".repeat(n / 2)
    var i = 0
    while {
      i += 1
      val signal =
        run(lines, Map("a" -> i, "b" -> 0, "c" -> 0, "d" -> 0), n).mkString

      signal != target
    }
    do ()

    println(s"Part 1: $i")
  }
}
