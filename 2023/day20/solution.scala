import scala.collection.mutable.Queue

import util._

enum Strength:
  case low, high

type Pulse = (String, Strength, String)

trait Module:
  val name: String
  val inputs: List[String]
  val outputs: List[String]
  var history = Vector[Boolean]()

  def pulse(from: String, strength: Strength): List[Pulse] = {
    send(receive(from, strength))
  }

  def receive(from: String, strength: Strength): Option[Strength]

  def send(strength: Option[Strength]): List[Pulse] = {
    strength
      .map(s => outputs.map(to => (name, s, to)))
      .getOrElse(List.empty)
  }

  def log(): Unit = {}

class FlipFlop(val name: String, val outputs: List[String]) extends Module {
  val inputs = List.empty
  var on = false

  def receive(from: String, strength: Strength): Option[Strength] = {
    strength match
      case Strength.high => None
      case Strength.low => {
        on = !on
        Some(if (on) Strength.high else Strength.low)
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
  var memory = inputs.map(i => (i, Strength.low)).toMap
  var pulsedHigh = false

  def receive(from: String, strength: Strength): Option[Strength] = {
    memory = memory.updated(from, strength)
    val outputStrength =
      if (memory.values.forall(_ == Strength.high)) Strength.low
      else Strength.high

    pulsedHigh = pulsedHigh || outputStrength == Strength.high
    Some(outputStrength)
  }

  override def log(): Unit = {
    history = history.appended(pulsedHigh)
    pulsedHigh = false
  }
}

class Broadcaster(val outputs: List[String]) extends Module {
  val name = "broadcaster"
  val inputs = List.empty
  def receive(from: String, strength: Strength): Option[Strength] =
    Some(strength)
}

class Test extends Module {
  val name = "output"
  val inputs = List.empty
  val outputs = List.empty
  def receive(from: String, strength: Strength): Option[Strength] = None
}

def gcd(a: Long, b: Long): Long = if (b == 0) a else gcd(b, a % b)
def lcm(a: Long, b: Long): Long = (a * b) / gcd(a, b)
def lcm(numbers: Iterable[Long]): Long = numbers.reduce((a, b) => lcm(a, b))

object Day extends Solution {
  val broadcaster = """(broadcaster) -> (.+)""".r
  val flipflop = """%(.+) -> (.+)""".r
  val conjunction = """&(.+) -> (.+)""".r

  def parseLine(line: String): (String, List[String]) = {
    val Array(n, o) = line.split(" -> ")
    val name = n.stripMargin('%').stripMargin('&')
    val out = o.split(", ").toList

    (name, out)
  }

  def inputs(conj: String, lines: List[String]): List[String] = {
    val result = lines
      .flatMap(line => {
        val (name, out) = parseLine(line)
        if (out.contains(conj)) Option(name) else None
      })
    result
  }

  def parse(lines: List[String]): Map[String, Module] = {
    lines
      .map(line => {
        val (name, out) = parseLine(line)

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
  }

  def pressButton(modules: Map[String, Module]): (Int, Int) = {
    var low = 0
    var high = 0
    val pulses = Queue[Pulse](("button", Strength.low, "broadcaster"))

    while (pulses.nonEmpty) {
      val (from, strength, to) = pulses.dequeue()

      strength match
        case Strength.low  => low += 1
        case Strength.high => high += 1

      val nextPulses = modules(to).pulse(from, strength)
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

  def part2(modules: Map[String, Module]): Long = {
    for (i <- 1 to 10_000) {
      pressButton(modules)
      modules.values.foreach(_.log())
    }

    val histories = modules
      .filter((key, value) => {
        // TODO: don't hardcode this
        modules("lg").inputs.contains(key)
      })
      .map((name, module) => {
        summarise(module.history, 2).map(_._2).sum.toLong
      })

    lcm(histories)
  }

  def solve(lines: List[String]): List[Long] = {
    val modules = parse(lines).updated("output", Test()).updated("rx", Test())
    val modules2 = parse(lines).updated("output", Test()).updated("rx", Test())
    List(part1(modules).toLong, part2(modules2))
  }

  def main(args: Array[String]): Unit = {
    // assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
