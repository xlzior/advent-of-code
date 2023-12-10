import util.FileUtils

object Solution {
  val swapPosition = """swap position (\d+) with position (\d+)""".r
  val swapLetter = """swap letter (\w) with letter (\w)""".r
  val rotatePosition = """rotate (left|right) (\d+) steps?""".r
  val rotateLetter = """rotate based on position of letter (\w)""".r
  val reverse = """reverse positions (\d+) through (\d+)""".r
  val move = """move position (\d+) to position (\d+)""".r

  def encrypt(string: String, instructions: List[String]): String = {
    instructions
      .foldLeft(string.toVector)((acc, instruction) =>
        instruction match {
          case swapPosition(x, y) => {
            val (a, b) = (acc(x.toInt), acc(y.toInt))
            acc.updated(x.toInt, b).updated(y.toInt, a)
          }
          case swapLetter(x, y) => {
            val (i, j) = (acc.indexOf(x(0)), acc.indexOf(y(0)))
            acc.updated(i, y(0)).updated(j, x(0))
          }
          case rotatePosition("left", n) => {
            acc.slice(n.toInt, acc.length) ++ acc.slice(0, n.toInt)
          }
          case rotatePosition("right", n) => {
            val i = acc.length - n.toInt
            acc.slice(i, acc.length) ++ acc.slice(0, i)
          }
          case rotateLetter(letter) => {
            val i = acc.indexOf(letter(0))
            val j = 1 + i + (if (i >= 4) 1 else 0)
            val k = math.floorMod(acc.length - j, acc.length)
            acc.slice(k, acc.length) ++ acc.slice(0, k)
          }
          case reverse(x, y) => {
            val (i, j) = (x.toInt, y.toInt + 1)
            acc.slice(0, i) ++ acc.slice(i, j).reverse ++ acc
              .slice(j, acc.length)
          }
          case move(x, y) => {
            val moved = acc(x.toInt)
            val without = acc.filter(c => c != moved)
            without.slice(0, y.toInt) ++ (moved +: without
              .slice(y.toInt, without.length))
          }
        }
      )
      .mkString
  }

  def decrypt(string: String, instructions: List[String]): String = {
    string.permutations
      .find(s => encrypt(s, instructions) == string)
      .get
      .mkString
  }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.read(args(0))
    val first = lines(0)
    val second = lines(1)
    val instructions = lines.tail.tail

    println(s"Part 1: ${encrypt(first, instructions)}")
    println(s"Part 1: ${decrypt(second, instructions)}")
  }
}
