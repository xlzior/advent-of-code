import scala.collection.immutable.ListMap

import util.Solution
import util.FileUtils

object Day extends Solution {
  val remove = """([a-z]+)-""".r
  val add = """([a-z]+)=(\d)""".r

  def hash(string: String): Int =
    string.foldLeft(0)((acc, curr) => ((acc + curr) * 17) % 256)

  def part1(steps: List[String]): Int =
    steps.map(hash).sum

  def part2(steps: List[String]): Int = {
    val boxes = Array.fill(256)(ListMap[String, Int]())

    steps.foreach(_ match {
      case add(label, focalLength) => {
        val box = hash(label)
        boxes(box) = boxes(box).updated(label, focalLength.toInt)
      }
      case remove(label) => {
        val box = hash(label)
        boxes(box) = boxes(box).removed(label)
      }
    })

    boxes.zipWithIndex
      .flatMap((box, boxNumber) =>
        box.toList.zipWithIndex.map((lens, slotNumber) =>
          (boxNumber + 1) * (slotNumber + 1) * lens._2
        )
      )
      .sum
  }

  def solve(lines: List[String]): List[Int] = {
    val steps = lines(0).split(",").toList
    List(part1(steps), part2(steps))
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
