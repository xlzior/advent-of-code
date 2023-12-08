import util.FileUtils

object Solution {
  val cpy = """cpy (.+) ([abcd])""".r
  val inc = """inc ([abcd])""".r
  val dec = """dec ([abcd])""".r
  val jnz = """jnz (.+) (.+)""".r
  val tgl = """tgl (.+)""".r

  def toggle(inst: String): String = inst match {
    case inc(x)    => s"dec $x"
    case dec(x)    => s"inc $x"
    case tgl(x)    => s"inc $x"
    case cpy(x, y) => s"jnz $x $y"
    case jnz(x, y) => s"cpy $x $y"
  }

  def get(registers: Map[String, Int], x: String): Int = {
    registers.getOrElse(x, x.toInt)
  }

  def run(
      originalProgram: List[String],
      initialState: Map[String, Int]
  ): Map[String, Int] = {
    var registers = initialState
    var program = originalProgram
    var i = 0
    while (i < program.length) {
      program(i) match {
        case cpy(x, y) =>
          registers = registers.updated(y, get(registers, x))
        case inc(x) => registers = registers.updated(x, registers(x) + 1)
        case dec(x) => registers = registers.updated(x, registers(x) - 1)
        case jnz(x, y) => {
          if (get(registers, x) != 0) {
            i += get(registers, y) - 1
          }
        }
        case tgl(x) => {
          val tgli = i + get(registers, x)
          if (0 <= tgli && tgli < program.length) {
            program = program.updated(tgli, toggle(program(tgli)))
          }
        }
      }
      i += 1
    }

    registers
  }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))

    val part1 =
      run(lines, Map[String, Int]("a" -> 7, "b" -> 0, "c" -> 0, "d" -> 0))

    println(s"Part 1: ${part1("a")}")

    val offset = lines
      .slice(19, 21)
      .map(s => """\d+""".r.findFirstIn(s).get.toInt)
      .product
    val part2 = (1 to 12).product + offset

    println(s"Part 1: ${part2}")
  }
}
