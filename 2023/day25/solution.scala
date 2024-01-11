import scala.collection.mutable

import util._

object Day extends Solution {
  type Graph = Map[Set[String], List[(Set[String], Int)]]
  type MutableGraph = mutable.Map[Set[String], List[(Set[String], Int)]]

  def parse(lines: List[String]): Graph = {
    lines
      .flatMap(_.split(": ") match {
        case Array(a, right) =>
          right.split(" ").flatMap(b => List((a, b), (b, a)))
      })
      .groupBy(_._1)
      .mapValues(_.map(_._2))
      .map((k, v) => (Set(k), v.map(s => (Set(s), 1))))
      .toMap
  }

  def step(graph: MutableGraph): Int = {
    var supernode = List(graph.keys.head)
    var outside = mutable.Set(graph.keySet.tail.toSeq: _*)

    while (outside.nonEmpty) {
      val (node, weight) = supernode
        .flatMap(node => graph(node))
        .groupBy(_._1)
        .mapValues(_.map(_._2).sum)
        .filter((neighbour, weight) => outside.contains(neighbour))
        .maxBy((neighbour, weight) => weight)

      supernode = node :: supernode
      outside -= node

      if (outside.isEmpty && weight == 3) {
        return node.size
      }
    }

    val node1 = supernode(0)
    val node2 = supernode(1)
    val mergedNode = node1 ++ node2
    val mergedEdges = (graph(node1) ++ graph(node2))
      .filter((n, _) => n != node1 && n != node2)
      .groupBy(_._1)
      .mapValues(_.map(_._2).sum)
      .toList

    graph.remove(node1)
    graph.remove(node2)
    graph(mergedNode) = mergedEdges
    mergedEdges.foreach((u, weight) => {
      val edge = (mergedNode, weight)
      graph(u) = edge :: graph(u).filter((v, w) => v != node1 && v != node2)
    })

    -1
  }

  def solve(lines: List[String]): List[Int] = {
    val initial = parse(lines)
    var graph = mutable.Map(initial.toSeq: _*)
    var result = -1
    while (result < 0) {
      println(graph.size)
      result = step(graph)
    }
    List(result * (initial.size - result))
  }

  def main(args: Array[String]): Unit = {
    // assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
  }
}
