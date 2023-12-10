package util

import util.FileUtils

trait Solution {
  def solve(lines: List[String]): (Any, Any)

  def main(args: Array[String]): Unit

  def testsPass: Boolean = {
    FileUtils.testFiles
      .map(filename => {
        val lines: List[String] = FileUtils.read(s"$filename.in")
        val solution = solve(lines).toList.map(_.toString)
        val expected: List[String] = FileUtils.read(s"$filename.out")

        val isCorrect =
          solution.zip(expected).forall((o, e) => e == "*" || o == e)
        println(s"$filename ${if (isCorrect) "✅" else "❌"}")
        isCorrect
      })
      .forall(identity)
  }
}
