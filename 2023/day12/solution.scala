import scala.collection.mutable.Set
import scala.collection.mutable.Map
import scala.collection.mutable.Queue

import util.Solution
import util.FileUtils

object Day extends Solution {
  def countGroups(condition: String): List[Int] = {
    """(#+)""".r.findAllIn(condition).map(_.length).toList
  }

  def isPrefix(curr: List[Int], goal: List[Int]) = {
    val n = curr.length
    curr.take(n - 1) == goal.take(n - 1)
  }

  def countWays(condition: String, groups: List[Int]): Int = {
    if (!condition.contains('?')) {
      return if (countGroups(condition) == groups) 1 else 0
    }

    val i = condition.indexOf("?")

    if (!isPrefix(countGroups(condition.slice(0, 1)), groups)) {
      return 0
    }

    val broken = countWays(condition.updated(i, '#'), groups)
    val working = countWays(condition.updated(i, '.'), groups)
    broken + working
  }

  def part1(parsed: List[(String, List[Int])]): Int = {
    parsed
      .map((condition, groups) => countWays(condition, groups))
      .sum
  }

  def part2(parsed: List[(String, List[Int])]): Int = {
    parsed
      .map((condition, groups) => {
        val expandedCondition = (condition + "?").repeat(5)
        val expandedGroups = List.fill(5)(groups).flatten
        countWays(expandedCondition, expandedGroups)
      })
      .sum
  }

  def solve(lines: List[String]): List[Int] = {
    val parsed = lines
      .map(_.split(" ").toList match {
        case List(condition, groups) =>
          (condition, groups.split(",").map(_.toInt).toList)
      })

    List(part1(parsed))
    // List(part1(parsed), part2(parsed))
  }

  def main(args: Array[String]): Unit = {
    // assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
