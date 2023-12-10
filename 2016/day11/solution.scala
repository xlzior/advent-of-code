import scala.collection.mutable.Queue
import scala.collection.mutable.Set
import scala.collection.mutable.Map

import util.FileUtils

type Item = (String, String)
type Layout = List[List[Item]]
type Summarised = (Int, List[List[(String, Int)]])

class State(val floor: Int, val layout: Layout) {
  def isSafe: Boolean =
    // if a chip is ever left in the same area as another RTG,
    // and it's not connected to its own RTG, the chip will be fried
    layout.forall(floor => {
      val chips = floor.filter((_, item) => item == "M")
      val rtgs = floor.filter((_, item) => item == "G")
      chips.forall((elem, _) => rtgs.contains((elem, "G")) || rtgs.isEmpty)
    })

  def isGoal: Boolean = layout.slice(0, 3).forall(_.isEmpty)

  def summarised: Summarised =
    (floor, layout.map(_.groupBy(_._2).mapValues(_.length).toList))

  def move(items: List[Item], to: Int): State =
    State(
      to,
      layout
        .updated(floor, layout(floor).diff(items))
        .updated(to, layout(to).appendedAll(items))
    )

  def next: Iterator[State] = {
    val items = layout(floor)
    (items.combinations(1) ++ items.combinations(2))
      .flatMap(move => List(-1, 1).map(dir => (move, floor + dir)))
      .filter((_, to) => 0 <= to && to < 4) // invalid floor
      .map((items, to) => move(items, to))
      .filter(_.isSafe)
  }

  override def toString(): String = {
    s"Floor: $floor\n${layout.reverse.mkString("\n")}"
  }
}

object Solution {
  val itemPattern = """(\w+)( generator|-compatible microchip)""".r

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
    val lines: List[String] = FileUtils.read(args(0))

    val verbose = args.length > 1

    val layout = lines.map(floor =>
      itemPattern
        .findAllMatchIn(floor)
        .map(m => (m.group(1), if (m.group(2) == " generator") "G" else "M"))
        .toList
    )

    val initialState = State(0, layout)
    println(s"Part 1: ${getNumSteps(initialState, verbose)}")

    val updatedLayout =
      layout.updated(
        0,
        layout(0).appendedAll(
          List("elerium", "dilithium").flatMap(elem =>
            List("G", "M").map(t => (elem, t))
          )
        )
      )
    val updatedState = State(0, updatedLayout)
    println(s"Part 2: ${getNumSteps(updatedState, verbose)}")
  }
}
