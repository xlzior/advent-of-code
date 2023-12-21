import util._

object Day extends Solution {
  type Workflow = List[String]
  type Workflows = Map[String, Workflow]
  type Part = Map[String, Int]
  type Parts = List[Part]
  type IntervalPart = Map[String, Interval[Int]]

  val workflow = """([a-z]+)\{([a-z<>:\d,]+)\}"""
  val category = """([xmas])=(\d+)""".r
  val lt = """([xmas])<(\d+):([ARa-z]+)""".r
  val gt = """([xmas])>(\d+):([ARa-z]+)""".r
  val default = """([ARa-z]+)""".r

  def parse(lines: List[String]): (Workflows, Parts) = {
    val Array(w, p) = lines.mkString("\n").split("\n\n")

    val workflows = w
      .split("\n")
      .map(s => {
        val Array(name, steps) = s.replace("}", "").split('{')
        (name, steps.split(",").toList)
      })
      .toMap

    val parts = p
      .split("\n")
      .map(part =>
        category
          .findAllMatchIn(part)
          .map(m => (m.group(1), m.group(2).toInt))
          .toMap
      )
      .toList

    (workflows, parts)
  }

  def sortPart(workflow: Workflow, part: Part): String = {
    workflow.head match {
      case default(dst)                                             => dst
      case lt(key, threshold, dst) if (part(key) < threshold.toInt) => dst
      case gt(key, threshold, dst) if (part(key) > threshold.toInt) => dst
      case _ => sortPart(workflow.tail, part)
    }
  }

  def sortPart(workflows: Workflows, part: Part): String = {
    var name = "in"
    while (name != "A" && name != "R") {
      name = sortPart(workflows(name), part)
    }
    name
  }

  def part1(workflows: Workflows, parts: List[Part]): Int = {
    parts.filter(part => sortPart(workflows, part) == "A").map(_.values.sum).sum
  }

  def splitIntervalPart(
      part: IntervalPart,
      key: String,
      value: Int
  ): List[IntervalPart] = {
    part(key).split(value) match {
      case List(only) => List(part)
      case List(left, right) =>
        List(part.updated(key, left), part.updated(key, right))
    }
  }

  def sortInterval(
      workflow: Workflow,
      part: IntervalPart
  ): List[(String, IntervalPart)] = {
    workflow.head match {
      case default(dst) => List((dst, part))
      case lt(key, value, dst) => {
        val threshold = value.toInt
        if (part(key).splitBy(threshold)) {
          splitIntervalPart(part, key, threshold).flatMap(subpart =>
            sortInterval(workflow, subpart)
          )
        } else if (part(key).end <= threshold) {
          List((dst, part))
        } else {
          sortInterval(workflow.tail, part)
        }
      }
      case gt(key, value, dst) => {
        val threshold = value.toInt
        if (part(key).splitBy(threshold + 1)) {
          splitIntervalPart(part, key, threshold + 1).flatMap(part =>
            sortInterval(workflow, part)
          )
        } else if (part(key).end <= threshold + 1) {
          sortInterval(workflow.tail, part)
        } else {
          List((dst, part))
        }
      }
    }
  }

  def part2(workflows: Workflows): Long = {
    var unsortedParts = List(
      (
        "in",
        Map(
          "x" -> Interval(1, 4001),
          "m" -> Interval(1, 4001),
          "a" -> Interval(1, 4001),
          "s" -> Interval(1, 4001)
        )
      )
    )

    var sum = 0L

    while (unsortedParts.nonEmpty) {
      val (name, intervalPart) = unsortedParts.head
      unsortedParts = unsortedParts.tail

      if (name == "A") {
        sum += intervalPart.values.map(_.size.toLong).product
      }
      if (workflows.contains(name)) {
        unsortedParts = unsortedParts.appendedAll(
          sortInterval(workflows(name), intervalPart)
        )
      }
    }

    sum
  }

  def solve(lines: List[String]): List[Long] = {
    val (workflows, parts) = parse(lines)
    List(part1(workflows, parts).toLong, part2(workflows))
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
