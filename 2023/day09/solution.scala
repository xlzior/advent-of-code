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

object Solution {
  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))
    val histories = lines.map(_.split(" ").map(_.toInt).toList)

    val part1 = histories.map(predict).sum
    println(s"Part 1: $part1")

    val part2 = histories.map(_.reverse).map(predict).sum
    println(s"Part 2: $part2")
  }
}
