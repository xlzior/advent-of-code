import util.FileUtils

object Solution {
  val cpy = """cpy (.+) ([abcd])""".r
  val inc = """inc ([abcd])""".r
  val dec = """dec ([abcd])""".r
  val jnz = """jnz (.+) (-?\d+)""".r

  def run(
      program: List[String],
      initialState: Map[String, Int]
  ): Map[String, Int] = {
    var registers = initialState
    var i = 0
    while (i < program.length) {
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
      }
      i += 1
    }

    registers
  }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))

    var part1 =
      run(lines, Map[String, Int]("a" -> 0, "b" -> 0, "c" -> 0, "d" -> 0))

    println(s"Part 1: ${part1("a")}")

    var part2 =
      run(lines, Map[String, Int]("a" -> 0, "b" -> 0, "c" -> 1, "d" -> 0))

    println(s"Part 2: ${part2("a")}")
  }
}
