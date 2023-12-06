import scala.collection.mutable.Map
import scala.collection.mutable.Set
import scala.collection.mutable.Queue
import util.FileUtils
import util.Pair

type Node = (Pair, Metadata)

class Metadata(val used: Int, val avail: Int, val isGoal: Boolean) {
  def empty: Metadata = Metadata(0, used + avail, false)
  def +(other: Metadata): Metadata =
    Metadata(used + other.used, avail - other.used, isGoal || other.isGoal)

  override def toString(): String = s"$used/${used + avail}".padTo(5, ' ')
}

class State(val h: Int, val w: Int, val grid: Map[Pair, Metadata]) {
  val dimensions: Pair = Pair(w, h)
  val deltas = List(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))

  def isValid(p: Pair) = Pair(0, 0) <= p && p < dimensions

  def isGoal: Boolean = grid(Pair(0, 0)).isGoal

  def neighboursOf(p: Pair): List[Pair] = {
    deltas
      .map(d => p + d)
      .filter(other =>
        isValid(other) && grid(p).used > 0 && grid(p).used <= grid(other).avail
      )
  }

  def moves: Iterable[(Pair, Pair)] = {
    (0 until h).flatMap(y =>
      (0 until w).flatMap(x => {
        val node = Pair(x, y)
        neighboursOf(node).map(neighbour => (node, neighbour))
      })
    )
  }

  def move(from: Pair, to: Pair) = {
    State(
      h,
      w,
      grid
        .updated(from, grid(from).empty)
        .updated(to, grid(to) + grid(from))
    )
  }

  def next: Iterable[State] = moves.map((from, to) => move(from, to))

  override def toString(): String = {
    (0 until h)
      .map(y => (0 until w).map(x => grid(Pair(x, y))).mkString(" "))
      .mkString("\n")
  }
}

object State {
  def of(files: List[Node]) = {
    val h = files.map(_._1.y).max + 1
    val w = files.map(_._1.x).max + 1
    val graph = files.foldLeft(Map[Pair, Metadata]()) {
      case (acc, (xy, meta)) => acc.updated(xy, meta)
    }

    State(h, w, graph)
  }
}

object Solution {
  def part1(files: List[Node]) = {
    files
      .map((a, metaA) =>
        if (metaA.used == 0) 0
        else files.count((b, metaB) => a != b && metaA.used <= metaB.avail)
      )
      .sum
  }

  def part2(files: List[Node]): Int = {
    val initialState = State.of(files)

    val queue = Queue[(Int, State)]((0, initialState))
    val explored = Set[State](initialState)

    while (queue.nonEmpty) {
      val (numSteps, state) = queue.dequeue()

      if (state.isGoal) return numSteps

      state.next.foreach(next => {
        if (!explored.contains(next)) {
          explored.add(next)
          queue.enqueue((numSteps + 1, next))
        }
      })
    }
    -1
  }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))

    val rawFiles = lines
      .drop(2)
      .flatMap(line =>
        """(\d+)""".r.findAllIn(line).map(_.toInt).toList match {
          case List(x, y, size, used, avail, usedPercent) =>
            List((Pair(x, y), used, avail))
          case _ => List.empty
        }
      )
    val x = rawFiles.map(_._1.x).max
    val goal = Pair(x, 0)
    val files =
      rawFiles.map((xy, used, avail) => (xy, Metadata(used, avail, xy == goal)))

    println(s"Part 1: ${part1(files)}")
    println(s"Part 2: ${part2(files)}")
  }
}
