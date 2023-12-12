import scala.collection.mutable.Map

import util.Solution
import util.FileUtils

object Day extends Solution {
  val seen = Map[(String, List[Int]), Long]()

  def couldBe(curr: String, goal: String): Boolean = {
    curr.length == goal.length && curr.zip(goal).forall {
      case ('.', '#') => false
      case ('#', '.') => false
      case _          => true
    }
  }

  def countWays(condition: String, groups: List[Int]): Long = {
    if (seen.contains((condition, groups))) {
      return seen((condition, groups))
    }

    var result = 0L

    if (condition.length == 0 && groups.length == 0) {
      result = 1L
    } else if (condition.length == 0) {
      result = 0L
    } else if (groups.length == 0) {
      result = if (condition.forall(_ != '#')) 1L else 0L
    } else {
      if (condition.head != '#') { // .
        result += countWays(condition.slice(1, condition.length), groups)
      }

      if (condition.head != '.') { // #
        val n = groups.head
        if (couldBe(condition.slice(0, n + 1), "#".repeat(n) + ".")) {
          result += countWays(
            condition.slice(n + 1, condition.length),
            groups.tail
          )
        }
      }
    }
    seen((condition, groups)) = result
    result
  }

  def part1(parsed: List[(String, List[Int])]): Long = {
    parsed
      .map((condition, groups) => countWays(condition + ".", groups))
      .sum
  }

  def part2(parsed: List[(String, List[Int])]): Long = {
    parsed
      .map((condition, groups) => {
        val expandedCondition = List.fill(5)(condition).mkString("?") + "."
        val expandedGroups = List.fill(5)(groups).flatten
        countWays(expandedCondition, expandedGroups)
      })
      .sum
  }

  def solve(lines: List[String]): List[Long] = {
    val parsed = lines
      .map(_.split(" ").toList match {
        case List(condition, groups) =>
          (condition, groups.split(",").map(_.toInt).toList)
      })

    List(part1(parsed), part2(parsed))
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
