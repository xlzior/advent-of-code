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

        val checks = solution.zip(expected).map((o, e) => e == "*" || o == e)
        val isCorrect = checks.forall(identity)

        if (isCorrect) {
          println(s"$filename ✅")
        } else {
          print(s"$filename ❌    ")
          checks.zipWithIndex
            .filter(!_._1)
            .foreach((_, i) =>
              print(
                s"Part ${i + 1}: ${solution(i)} (expected: ${expected(i)})    "
              )
            )
        }
        println()
        isCorrect
      })
      .forall(identity)
  }
}
