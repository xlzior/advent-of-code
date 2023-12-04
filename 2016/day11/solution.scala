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

  type SummarisedState = List[(Int, Int)]

  def summarise(state: State): SummarisedState = {
    state.map(floor =>
      (
        floor.count((_, itemType) => itemType == "G"),
        floor.count((_, itemType) => itemType == "M")
      )
    )
  }

  def move(
      state: State,
      items: List[(String, String)],
      from: Int,
      to: Int
  ): Option[State] = {
    if (to < 0 || to >= 4) {
      return None
    }

    val nextState = state
      .updated(from, state(from).diff(items))
      .updated(to, state(to).appendedAll(items))

    if (!isSafe(nextState)) {
      return None
    }

    Some(nextState)
  }

  def getSteps(
      singleItems: List[(String, String)]
  ): List[(List[(String, String)], Int)] = {
    // elevator can either take 1 or 2 items
    val pairsOfItems = singleItems.combinations(2)
    val combinations = singleItems.map(x => List(x)) ++ pairsOfItems

    combinations.flatMap(items =>
      // elevator can either go up or down
      List(-1, 1).map(direction => (items, direction))
    )
  }

  def getNextStates(state: State, floor: Int): List[(State, Int)] = {
    getSteps(state(floor)).flatMap((items, direction) =>
      val nextState = move(state, items, floor, floor + direction)
      if (nextState.isDefined) {
        List((nextState.get, floor + direction))
      } else {
        List.empty
      }
    )
  }

  def getNumSteps(initialState: State, verbose: Boolean): Int = {
    val elements = initialState.flatMap(floor => floor.map(_._1))

    val explored = Set[(SummarisedState, Int)]()
    val queue = Queue[(Int, State, Int)]()
    explored.add((summarise(initialState), 0))
    queue.enqueue((0, initialState, 0))

    val timer = Map[Int, Long]()
    val startTime = System.currentTimeMillis()

    while (queue.nonEmpty) {
      val (numSteps, state, floor) = queue.dequeue()

      if (verbose && !timer.contains(numSteps)) {
        timer(numSteps) = System.currentTimeMillis()
        println(s"$numSteps: ${timer(numSteps) - startTime} ms")
      }

      if (isGoal(elements, state)) {
        return numSteps
      }

      getNextStates(state, floor).map((state, floor) => {
        if (!explored.contains((summarise(state), floor))) {
          explored.add((summarise(state), floor))
          queue.enqueue((numSteps + 1, state, floor))
        }
      })
    }

    return -1
  }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))

    val verbose = args.length > 1

    val initialState = lines.map(floor => {
      val generators =
        generator.findAllMatchIn(floor).map(m => (m.group(1), "G"))
      val microchips =
        microchip.findAllMatchIn(floor).map(m => (m.group(1), "M"))

      (generators ++ microchips).toList
    })

    println(s"Part 1: ${getNumSteps(initialState, verbose)}")

    val updatedState =
      initialState.updated(
        0,
        initialState(0).appendedAll(
          List(
            ("elerium", "G"),
            ("elerium", "M"),
            ("dilithium", "M"),
            ("dilithium", "G")
          )
        )
      )
    println(s"Part 2: ${getNumSteps(updatedState, verbose)}")
  }
}
