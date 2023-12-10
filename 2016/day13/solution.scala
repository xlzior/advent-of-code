import scala.collection.mutable.Queue
import scala.collection.mutable.Map
import util.FileUtils
import util.Pair

object Solution {
  val deltas = List(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))

  def isOpenSpace(n: Int, p: Pair): Boolean = {
    val (x, y) = p.toTuple
    val z = (x * x + 3 * x + 2 * x * y + y + y * y + n)
    z.toBinaryString.count(c => c == '1') % 2 == 0
  }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.read(args(0))

    val n = lines(0).toInt
    val goal = Pair(lines(1).toInt, lines(2).toInt)

    val explored = Map[Pair, Int]()
    val queue = Queue[(Pair, Int)]()

    val start = Pair(1, 1)
    explored(start) = 0
    queue.enqueue((start, 0))

    while (queue.nonEmpty) {
      val (current, distance) = queue.dequeue()

      if (current == goal) {
        println(s"Part 1: $distance")

        val part2 = explored.filter((_, distance) => distance <= 50).size
        println(s"Part 2: $part2")
        return
      }

      deltas.foreach(d => {
        val neighbour = current + d
        if (
          neighbour >= Pair(0, 0) && isOpenSpace(n, neighbour) && !explored
            .contains(neighbour)
        ) {
          explored(neighbour) = distance + 1
          queue.enqueue((neighbour, distance + 1))
        }
      })
    }
  }
}
