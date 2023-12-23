import scala.collection.mutable.Queue
import util._

enum PulseType:
  case low, high

type Pulse = (String, PulseType, String)

trait Module:
  val name: String
  val outputs: List[String]

  def pulse(from: String, pulseType: PulseType): List[Pulse] = {
    val pulse = pulseType match {
      case PulseType.low  => low(from)
      case PulseType.high => high(from)
    }
    pulse
      .map(pulseType => outputs.map(to => (name, pulseType, to)))
      .getOrElse(List.empty)
  }

  def low(from: String): Option[PulseType]
  def high(from: String): Option[PulseType]

class FlipFlop(val name: String, val outputs: List[String]) extends Module {
  // Flip-flop modules (prefix %) are either on or off; they are initially off.
  var on = false

  def high(from: String): Option[PulseType] = {
    // If a flip-flop module receives a high pulse, it is ignored and nothing happens.
    None
  }

  def low(from: String): Option[PulseType] = {
    // However, if a flip-flop module receives a low pulse, it flips between on and off.
    // If it was off, it turns on and sends a high pulse.
    // If it was on, it turns off and sends a low pulse.
    on = !on
    Some(if (on) PulseType.high else PulseType.low)
  }

  override def toString(): String = s"$name: $outputs"
}

class Conjunction(
    val name: String,
    val inputs: List[String],
    val outputs: List[String]
) extends Module {
  // Conjunction modules (prefix &) remember the type of the most recent pulse received from each of their connected input modules; they initially default to remembering a low pulse for each input.

  var memory = inputs.map(i => (i, PulseType.low)).toMap

  def getPulseType: Option[PulseType] = {
    Some(
      if (memory.values.forall(_ == PulseType.high)) PulseType.low
      else PulseType.high
    )
  }

  // When a pulse is received, the conjunction module first updates its memory for that input.
  // Then, if it remembers high pulses for all inputs, it sends a low pulse;
  // otherwise, it sends a high pulse.
  def high(from: String): Option[PulseType] = {
    memory = memory.updated(from, PulseType.high)
    getPulseType
  }

  def low(from: String): Option[PulseType] = {
    memory = memory.updated(from, PulseType.low)
    getPulseType
  }

  override def toString(): String = s"$name: $inputs | $outputs"
}

class Broadcaster(val outputs: List[String]) extends Module {
  val name = "broadcaster"
  // There is a single broadcast module (named broadcaster).
  // When it receives a pulse, it sends the same pulse to all of its destination modules.

  def high(from: String): Option[PulseType] = Some(PulseType.high)
  def low(from: String): Option[PulseType] = Some(PulseType.low)

  override def toString(): String = s"$name: $outputs"
}

class Test extends Module {
  val name = "output"
  val outputs = List.empty
  def high(from: String): Option[PulseType] = None
  def low(from: String): Option[PulseType] = None

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
      val pulses = Queue[Pulse](("button", PulseType.low, "broadcaster"))

      while (pulses.nonEmpty) {
        val (from, pulseType, to) = pulses.dequeue()

        // Part 1
        pulseType match
          case PulseType.low  => low += 1
          case PulseType.high => high += 1

        val nextPulses = modules(to).pulse(from, pulseType)
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
