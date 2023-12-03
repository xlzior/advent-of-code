import scala.collection.mutable.Queue
import scala.collection.mutable.Set
import scala.collection.mutable.Map

import util.FileUtils

object Solution {
  val generator = """(\w+) generator""".r
  val microchip = """(\w+)-compatible microchip""".r

  type State = List[List[(String, String)]]

  def isSafe(state: State): Boolean = {
    // if a chip is ever left in the same area as another RTG,
    // and it's not connected to its own RTG, the chip will be fried
    state.forall(floor => {
      val chips = floor.filter((_, item) => item == "M")
      val rtgs = floor.filter((_, item) => item == "G")

      chips.forall((element, _) =>
        rtgs.contains((element, "G")) || rtgs.isEmpty
      )
    })
  }

  def isGoal(elements: List[String], state: State): Boolean = {
    elements.forall(elem =>
      state(3).contains((elem, "G")) && state(3).contains((elem, "M"))
    )
  }

  def countFreeFloors(state: State): Int = {
    state.zipWithIndex
      .filter((items, _) => items.nonEmpty)
      .headOption
      .getOrElse((List.empty, 0))
      ._2 + 1
  }

  def move(
      state: State,
      items: List[(String, String)],
      from: Int,
      to: Int,
      bestFreeFloors: Int
  ): Option[State] = {
    if (to < 0 || to >= 4) {
      return None
    }

    // pruning: if I am going down, only bring one thing
    if (to < from && items.length > 1) {
      return None
    }

    val nextState = state
      .updated(from, state(from).diff(items))
      .updated(to, state(to).appendedAll(items))

    if (!isSafe(nextState)) {
      return None
    }

    // pruning: if I already cleared a lower floor, don't go down there again
    if (countFreeFloors(nextState) < bestFreeFloors) {
      return None
    }

    Some(nextState)
  }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))

    val initialState = lines.map(floor => {
      val generators =
        generator.findAllMatchIn(floor).map(m => (m.group(1), "G"))
      val microchips =
        microchip.findAllMatchIn(floor).map(m => (m.group(1), "M"))

      (generators ++ microchips).toList
    })

    val elements = initialState.flatMap(floor => floor.map(_._1))

    var bestFreeFloors = 0
    val explored = Set[(State, Int)]()
    val queue = Queue[(Int, State, Int)]()
    explored.add((initialState, 0))
    queue.enqueue((0, initialState, 0))

    val timer = Map[Int, Long]()
    val startTime = System.currentTimeMillis()

    while (queue.nonEmpty) {
      val (numSteps, state, floor) = queue.dequeue()

      bestFreeFloors = bestFreeFloors.max(countFreeFloors(state))

      if (!timer.contains(numSteps)) {
        timer(numSteps) = System.currentTimeMillis()
        println(s"$numSteps: ${timer(numSteps) - startTime} ms")
      }

      if (isGoal(elements, state)) {
        println(s"Part 1: $numSteps")
        return
      }

      // elevator can either take 1 or 2 items
      val singleItems = state(floor)
      val pairsOfItems = singleItems.combinations(2)
      val combinations = singleItems.map(x => List(x)) ++ pairsOfItems

      // elevator can either go up or down
      combinations.foreach(items => {
        List(-1, 1).foreach(direction => {
          val nextState =
            move(state, items, floor, floor + direction, bestFreeFloors)

          if (nextState.isDefined) {
            val s = nextState.get
            if (!explored.contains((s, floor + direction))) {
              explored.add((s, floor + direction))
              queue.enqueue((numSteps + 1, s, floor + direction))
            }
          }
        })
      })
    }
  }
}
