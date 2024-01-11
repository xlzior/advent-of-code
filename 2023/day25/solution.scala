import util._

object Day extends Solution {
  type Graph = Map[Set[String], List[(Set[String], Int)]]

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

  var cuts = Map[Int, Int]()

  def step(graph: Graph): Graph = {
    val start = graph.keys.head
    var outside = graph.keySet.tail
    var supernode = List(start)

    while (outside.nonEmpty) {
      val (node, weight) = supernode
        .flatMap(node => graph(node))
        .groupBy(_._1)
        .mapValues(_.map(_._2).sum)
        .filter((neighbour, weight) => outside.contains(neighbour))
        .maxBy((neighbour, weight) => weight)

      supernode = node :: supernode
      outside -= node

      // take note of the weight of the last edge merged
      if (outside.isEmpty) {
        cuts = cuts.updated(weight, node.size)
      }
    }

    val node1 = supernode(0)
    val node2 = supernode(1)
    val mergedNode = node1 ++ node2
    val mergedEdges = (graph(node1) ++ graph(node2))
      .groupBy(_._1)
      .mapValues(_.map(_._2).sum)
      .toList

    graph
      .removed(node1)
      .removed(node2)
      .map((k, v) => {
        (
          k,
          v.map { case (neighbour, weight) =>
            if (neighbour == node1 || neighbour == node2) (mergedNode, weight)
            else (neighbour, weight)
          }.groupBy(_._1)
            .mapValues(_.map(_._2).sum)
            .toList
        )
      })
      .updated(mergedNode, mergedEdges)
  }

  def part1(initial: Graph): Int = {
    var graph = initial
    while (graph.size > 2) {
      println(graph.size)
      graph = step(graph)
    }
    println(cuts)
    val partition1 = cuts.minBy(_._1)._2
    partition1 * (initial.size - partition1)
  }

  def solve(lines: List[String]): List[Int] = {
    val graph = parse(lines)
    List(part1(graph))
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
  }
}
