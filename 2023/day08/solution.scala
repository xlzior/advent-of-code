import scala.collection.mutable.Map as MutableMap
import scala.collection.mutable.Queue
import util.FileUtils

def gcd(a: Long, b: Long): Long = if (b == 0) a else gcd(b, a % b)
def lcm(a: Long, b: Long): Long = (a * b) / gcd(a, b)
def lcm(numbers: Iterable[Long]): Long = numbers.reduce((a, b) => lcm(a, b))

object Solution {
  def part1(insts: List[Int], graph: Map[String, Array[String]]): Int = {
    var count = 0
    var curr = "AAA"
    while (curr != "ZZZ") {
      curr = graph(curr)(insts(count % insts.length))
      count += 1
    }

    count
  }

  def part2(insts: List[Int], graph: Map[String, Array[String]]): Long = {
    var count = 0
    var ghosts = graph.filterKeys(_.endsWith("A")).keys.toList
    var reachedZ = MutableMap[Int, Int]()

    while (reachedZ.size < ghosts.size) {
      ghosts.zipWithIndex.foreach((location, i) => {
        if (location.endsWith("Z")) {
          reachedZ(i) = count
          println(s"Ghost $i reached $location in $count steps")
        }
      })
      ghosts = ghosts.map(curr => graph(curr)(insts(count % insts.length)))
      count += 1
    }

    lcm(reachedZ.values.map(_.toLong))
  }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))
    val instructions = lines(0).map(c => if (c == 'L') 0 else 1).toList
    val graph =
      lines
        .slice(2, lines.length)
        .map(line =>
          """\w{3}""".r.findAllIn(line).toList match {
            case List(node, left, right) => (node, Array(left, right))
          }
        )
        .toMap

    println(s"Part 1: ${part1(instructions, graph)}")
    println(s"Part 2: ${part2(instructions, graph)}")
  }
}
