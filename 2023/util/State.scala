package util

import scala.collection.mutable.PriorityQueue
import scala.collection.mutable.Map

trait State:
  def isGoal: Boolean
  def neighbours: List[(Int, State)]

  implicit val ordering: Ordering[(Int, State)] = Ordering.by(-_._1)

  def shortestPath: Int = {
    val dist = Map(this -> 0)
    val pq = PriorityQueue((0, this))

    while (pq.nonEmpty) {
      val (cost, state) = pq.dequeue()

      if (state.isGoal)
        return cost

      state.neighbours.foreach((weight, nextState) => {
        val alt = cost + weight

        if (alt < dist.getOrElse(nextState, Int.MaxValue)) {
          dist(nextState) = alt
          pq.enqueue((alt, nextState))
        }
      })
    }

    -1
  }
