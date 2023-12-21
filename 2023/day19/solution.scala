import util._

object Day extends Solution {
  type Workflow = List[String]
  type Workflows = Map[String, Workflow]
  type Part = Map[String, Int]
  type Parts = List[Part]

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

  def sort(workflow: Workflow, part: Part): String = {
    workflow.head match {
      case default(dst)                                             => dst
      case lt(key, threshold, dst) if (part(key) < threshold.toInt) => dst
      case gt(key, threshold, dst) if (part(key) > threshold.toInt) => dst
      case _ => sort(workflow.tail, part)
    }
  }

  def sort(workflows: Workflows, part: Part): String = {
    var name = "in"
    while (name != "A" && name != "R") {
      name = sort(workflows(name), part)
    }
    name
  }

  def part1(workflows: Workflows, parts: List[Part]): Int = {
    parts.filter(part => sort(workflows, part) == "A").map(_.values.sum).sum
  }

  def part2(workflows: Workflows): Int = {
    -1
  }

  def solve(lines: List[String]): List[Int] = {
    val (workflows, parts) = parse(lines)
    List(part1(workflows, parts), part2(workflows))
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
