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

  def parseWorkflow(s: String): (String, Workflow) = {
    val Array(name, steps) = s.replace("}", "").split('{')
    (name, steps.split(",").toList)
  }

  def parsePart(s: String): Part = category
    .findAllMatchIn(s)
    .map(m => (m.group(1), m.group(2).toInt))
    .toMap

  def parse(lines: List[String]): (Workflows, Parts) = {
    val Array(w, p) = lines.mkString("\n").split("\n\n").map(_.split("\n"))
    val workflows = w.map(parseWorkflow).toMap
    val parts = p.map(parsePart).toList
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

  def part1(workflows: Workflows, parts: List[Part]): Int = {
    parts
      .filter(part => {
        var name = "in"
        while (name != "A" && name != "R") {
          name = sortPart(workflows(name), part)
        }
        name == "A"
      })
      .map(_.values.sum)
      .sum
  }

  def split(part: IntervalPart, key: String, value: Int): List[IntervalPart] = {
    part(key).split(value) match {
      case List(only) => List(part)
      case List(left, right) =>
        List(part.updated(key, left), part.updated(key, right))
    }
  }

  def sortInterval(
      workflow: Workflow
  )(part: IntervalPart): List[(String, IntervalPart)] = {
    workflow.head match {
      case default(dst) => List((dst, part))
      case lt(key, value, dst) => {
        val threshold = value.toInt
        if (part(key).splitBy(threshold)) {
          split(part, key, threshold).flatMap(sortInterval(workflow))
        } else if (part(key).end <= threshold) {
          List((dst, part))
        } else {
          sortInterval(workflow.tail)(part)
        }
      }
      case gt(key, value, dst) => {
        val threshold = value.toInt + 1
        if (part(key).splitBy(threshold)) {
          split(part, key, threshold).flatMap(sortInterval(workflow))
        } else if (part(key).end <= threshold) {
          sortInterval(workflow.tail)(part)
        } else {
          List((dst, part))
        }
      }
    }
  }

  def part2(workflows: Workflows): Long = {
    val pangaea = Map(
      "x" -> Interval(1, 4001),
      "m" -> Interval(1, 4001),
      "a" -> Interval(1, 4001),
      "s" -> Interval(1, 4001)
    )
    var parts = List(("in", pangaea))
    var sum = 0L

    while (parts.nonEmpty) {
      val (name, part) = parts.head
      parts = parts.tail

      if (name == "A") {
        sum += part.values.map(_.size.toLong).product
      }
      if (workflows.contains(name)) {
        parts = parts.appendedAll(sortInterval(workflows(name))(part))
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
