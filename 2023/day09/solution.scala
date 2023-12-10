import util.Solution
import util.FileUtils

def predict(line: List[Int]): Int = {
  var curr = line
  var result = curr.last
  while (!curr.forall(_ == 0)) {
    curr = curr.zip(curr.drop(1)).map((a, b) => b - a)
    result += curr.last
  }
  result
}

object Day9 extends Solution {
  def solve(lines: List[String]): (Int, Int) = {
    val histories = lines.map(_.split(" ").map(_.toInt).toList)

    val part1 = histories.map(predict).sum
    val part2 = histories.map(_.reverse).map(predict).sum
    (part1, part2)
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read("puzzle.in")
    val (part1, part2) = solve(lines)
    println(s"Part 1: $part1")
    println(s"Part 2: $part2")
  }
}
