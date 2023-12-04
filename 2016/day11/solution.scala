import scala.collection.mutable.Queue
import scala.collection.mutable.Set
import scala.collection.mutable.Map

import util.FileUtils

type Item = (String, String)
type Layout = List[List[Item]]
type Summarised = (Int, List[List[(String, Int)]])

class State(val floor: Int, val layout: Layout) {
  def isSafe: Boolean = {
    // if a chip is ever left in the same area as another RTG,
    // and it's not connected to its own RTG, the chip will be fried
    layout.forall(floor => {
      val chips = floor.filter((_, item) => item == "M")
      val rtgs = floor.filter((_, item) => item == "G")

      chips.forall((element, _) =>
        rtgs.contains((element, "G")) || rtgs.isEmpty
      )
    })
  }

  def isGoal: Boolean =
    layout
      .flatMap(floor => floor.map(_._1))
      .forall(elem =>
        layout(3).contains((elem, "G")) && layout(3).contains((elem, "M"))
      )

  def summarised: Summarised =
    (floor, layout.map(_.groupBy(_._2).mapValues(_.length).toList))

  def move(items: List[(String, String)], to: Int): Option[State] = {
    if (to < 0 || to >= 4) return None

    val nextState = State(
      to,
      layout
        .updated(floor, layout(floor).diff(items))
        .updated(to, layout(to).appendedAll(items))
    )

    if (!nextState.isSafe) return None

    Some(nextState)
  }

  def steps: Iterator[(List[Item], Int)] = {
    // elevator can either take 1 or 2 items
    val singleItems = layout(floor)
    val combinations =
      singleItems.combinations(1) ++ singleItems.combinations(2)

    combinations.flatMap(items =>
      // elevator can either go up or down
      List(-1, 1).map(direction => (items, direction))
    )
  }

  def next: Iterator[State] = {
    steps
      .map((items, direction) => move(items, floor + direction))
      .flatten
  }

  override def toString(): String = {
    s"Floor: $floor\n${layout.reverse.mkString("\n")}"
  }
}

object Solution {
  val generator = """(\w+) generator""".r
  val microchip = """(\w+)-compatible microchip""".r

  def getNumSteps(initialState: State, verbose: Boolean): Int = {
    val explored = Set[Summarised](initialState.summarised)
    val queue = Queue[(Int, State)]((0, initialState))

    val timer = Map[Int, Long]()
    val startTime = System.currentTimeMillis()

    while (queue.nonEmpty) {
      val (numSteps, state) = queue.dequeue()

      if (verbose && !timer.contains(numSteps)) {
        timer(numSteps) = System.currentTimeMillis()
        println(s"$numSteps: ${timer(numSteps) - startTime} ms")
      }

      if (state.isGoal) return numSteps

      state.next.foreach((state) => {
        if (!explored.contains(state.summarised)) {
          explored.add(state.summarised)
          queue.enqueue((numSteps + 1, state))
        }
      })
    }

    return -1
  }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))

    val verbose = args.length > 1

    val layout = lines.map(floor => {
      val generators =
        generator.findAllMatchIn(floor).map(m => (m.group(1), "G"))
      val microchips =
        microchip.findAllMatchIn(floor).map(m => (m.group(1), "M"))

      (generators ++ microchips).toList
    })

    val initialState = State(0, layout)
    println(s"Part 1: ${getNumSteps(initialState, verbose)}")

    val updatedLayout =
      layout.updated(
        0,
        layout(0).appendedAll(
          List(
            ("elerium", "G"),
            ("elerium", "M"),
            ("dilithium", "M"),
            ("dilithium", "G")
          )
        )
      )
    val updatedState = State(0, updatedLayout)
    println(s"Part 2: ${getNumSteps(updatedState, verbose)}")
  }
}
