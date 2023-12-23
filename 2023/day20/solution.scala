import scala.collection.mutable.Queue
import util._

enum Strength:
  case low, high

type Pulse = (String, Strength, String)

trait Module:
  val name: String
  val outputs: List[String]

  def pulse(from: String, strength: Strength): List[Pulse] = {
    send(receive(from, strength))
  }

  def receive(from: String, strength: Strength): Option[Strength]

  def send(strength: Option[Strength]): List[Pulse] = {
    strength
      .map(s => outputs.map(to => (name, s, to)))
      .getOrElse(List.empty)
  }

class FlipFlop(val name: String, val outputs: List[String]) extends Module {
  var on = false

  def receive(from: String, strength: Strength): Option[Strength] = {
    strength match
      case Strength.high => None
      case Strength.low => {
        on = !on
        Some(if (on) Strength.high else Strength.low)
      }
  }

  override def toString(): String = s"$name: $outputs"
}

class Conjunction(
    val name: String,
    val inputs: List[String],
    val outputs: List[String]
) extends Module {
  var memory = inputs.map(i => (i, Strength.low)).toMap

  def receive(from: String, strength: Strength): Option[Strength] = {
    memory = memory.updated(from, strength)
    Some(
      if (memory.values.forall(_ == Strength.high)) Strength.low
      else Strength.high
    )
  }

  override def toString(): String = s"$name: $inputs | $outputs"
}

class Broadcaster(val outputs: List[String]) extends Module {
  val name = "broadcaster"
  def receive(from: String, strength: Strength): Option[Strength] =
    Some(strength)

  override def toString(): String = s"$name: $outputs"
}

class Test extends Module {
  val name = "output"
  val outputs = List.empty
  def receive(from: String, strength: Strength): Option[Strength] = None
}

object Day extends Solution {
  val broadcaster = """(broadcaster) -> (.+)""".r
  val flipflop = """%(\w+) -> (.+)""".r
  val conjunction = """&(\w+) -> (.+)""".r

  def parseLine(line: String): (String, List[String]) = {
    val Array(n, o) = line.split(" -> ")
    val name = n.stripMargin('%').stripMargin('&')
    val out = o.split(", ").toList

    (name, out)
  }

  def conjInputs(conj: String, lines: List[String]): List[String] = {
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
              Conjunction(name, conjInputs(name, lines), out)
            )
        }
      })
      .toMap
  }

  def part1(modules: Map[String, Module]): Int = {
    var low = 0
    var high = 0

    for (i <- 1 to 1000) {
      val pulses = Queue[Pulse](("button", Strength.low, "broadcaster"))

      while (pulses.nonEmpty) {
        val (from, strength, to) = pulses.dequeue()

        // Part 1
        strength match
          case Strength.low  => low += 1
          case Strength.high => high += 1

        val nextPulses = modules(to).pulse(from, strength)
        pulses.enqueueAll(nextPulses)
      }
    }

    low * high
  }

  def part2(modules: Map[String, Module]): Int = {
    -1
  }

  def solve(lines: List[String]): List[Int] = {
    val modules = parse(lines).updated("output", Test()).updated("rx", Test())
    List(part1(modules), part2(modules))
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
