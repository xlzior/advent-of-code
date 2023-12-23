import scala.collection.mutable.Queue

import util._

type Pulse = (String, Boolean, String)

trait Module:
  val name: String
  val inputs: List[String]
  val outputs: List[String]
  var history = Vector[Boolean]()

  def pulse(from: String, isHigh: Boolean): List[Pulse] = {
    receive(from, isHigh)
      .map(s => outputs.map(to => (name, s, to)))
      .getOrElse(List.empty)
  }

  def receive(from: String, isHigh: Boolean): Option[Boolean]

  def log(): Unit = {}

class FlipFlop(val name: String, val outputs: List[String]) extends Module {
  val inputs = List.empty
  var on = false

  def receive(from: String, isHigh: Boolean): Option[Boolean] = {
    isHigh match
      case true => None
      case false => {
        on = !on
        Some(on)
      }
  }

  override def log(): Unit = {
    history = history.appended(on)
  }
}

class Conjunction(
    val name: String,
    val inputs: List[String],
    val outputs: List[String]
) extends Module {
  var memory = inputs.map(i => (i, false)).toMap
  var pulsedHigh = false

  def receive(from: String, isHigh: Boolean): Option[Boolean] = {
    memory = memory.updated(from, isHigh)
    val pulse = memory.values.exists(!_)
    pulsedHigh ||= pulse
    Some(pulse)
  }

  override def log(): Unit = {
    history = history.appended(pulsedHigh)
    pulsedHigh = false
  }
}

class Broadcaster(val outputs: List[String]) extends Module {
  val name = "broadcaster"
  val inputs = List.empty
  def receive(from: String, isHigh: Boolean): Option[Boolean] =
    Some(isHigh)
}

class Test extends Module {
  val name = "output"
  val inputs = List.empty
  val outputs = List.empty
  def receive(from: String, isHigh: Boolean): Option[Boolean] = None
}

def gcd(a: Long, b: Long): Long = if (b == 0) a else gcd(b, a % b)
def lcm(a: Long, b: Long): Long = (a * b) / gcd(a, b)
def lcm(numbers: Iterable[Long]): Long = numbers.reduce((a, b) => lcm(a, b))

object Day extends Solution {
  def splitLine(line: String): (String, List[String]) = {
    val Array(name, o) = line.split(" -> ")
    (name.stripMargin('%').stripMargin('&'), o.split(", ").toList)
  }

  def inputs(conj: String, lines: List[String]): List[String] = lines
    .flatMap(line => {
      val (name, out) = splitLine(line)
      if (out.contains(conj)) Option(name) else None
    })

  def parse(lines: List[String]): Map[String, Module] = lines
    .map(line => {
      val (name, out) = splitLine(line)

      line(0) match {
        case 'b' => (name, Broadcaster(out))
        case '%' => (name, FlipFlop(name, out))
        case '&' =>
          (
            name,
            Conjunction(name, inputs(name, lines), out)
          )
      }
    })
    .toMap

  def pressButton(modules: Map[String, Module]): (Int, Int) = {
    var low = 0
    var high = 0
    val pulses = Queue[Pulse](("button", false, "broadcaster"))

    while (pulses.nonEmpty) {
      val (from, isHigh, to) = pulses.dequeue()

      isHigh match
        case false => low += 1
        case true  => high += 1

      val nextPulses = modules(to).pulse(from, isHigh)
      pulses.enqueueAll(nextPulses)
    }

    (low, high)
  }

  def part1(modules: Map[String, Module]): Int = {
    var low = 0
    var high = 0

    for (i <- 1 to 1000) {
      val (l, h) = pressButton(modules)
      low += l
      high += h
    }

    low * high
  }

  def summarise(history: Vector[Boolean], n: Int): List[(Boolean, Int)] = {
    (history, n) match {
      case (_, 0)                => List.empty
      case (v, _) if v.size == 0 => List.empty
      case _ => {
        val v = history.head
        val section = history.takeWhile(_ == v)
        val rest = history.dropWhile(_ == v)

        (v, section.size) :: summarise(rest, n - 1)
      }
    }
  }

  def part2(modules: Map[String, Module], target: String): Long = {
    for (i <- 1 to 10_000) {
      pressButton(modules)
      modules.values.foreach(_.log())
    }

    val histories = modules
      .filter((key, value) => modules(target).inputs.contains(key))
      .map((name, module) => summarise(module.history, 2).map(_._2).sum.toLong)

    lcm(histories)
  }

  def solve(lines: List[String]): List[Long] = {
    val modules = parse(lines).updated("output", Test()).updated("rx", Test())

    if (lines.exists(_.contains("rx"))) {
      val modules2 = parse(lines).updated("rx", Test())
      List(part1(modules).toLong, part2(modules2, inputs("rx", lines)(0)))
    } else {
      List(part1(modules).toLong)
    }
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
