import scala.collection.mutable

import util._

class XYZ(val x: Int, val y: Int, val z: Int) {
  def decreaseZ(dz: Int): XYZ = XYZ(x, y, z - dz)

  override def toString(): String = s"($x, $y, $z)"
  override def hashCode(): Int = (x, y, z).hashCode()
}

class Block(val start: XYZ, val end: XYZ) {
  def intersects(other: Block): Boolean = {
    this.start.x <= other.end.x && other.start.x <= this.end.x &&
    this.start.y <= other.end.y && other.start.y <= this.end.y
  }

  def decreaseZ(dz: Int): Block = Block(start.decreaseZ(dz), end.decreaseZ(dz))

  override def toString(): String = s"$start ~ $end"
  override def hashCode(): Int = (start, end).hashCode()
}

object Day extends Solution {
  def parse(lines: List[String]): List[Block] = {
    lines
      .map(line => {
        val Array(sx, sy, sz, ex, ey, ez) =
          """\d+""".r.findAllIn(line).map(_.toInt).toArray
        Block(XYZ(sx, sy, sz), XYZ(ex, ey, ez))
      })
      .sortBy(_.start.z)
  }

  def getGround(blocks: List[Block]): Block = {
    val minX = blocks.map(_.start.x).min
    val minY = blocks.map(_.start.y).min
    val maxX = blocks.map(_.end.x).max
    val maxY = blocks.map(_.end.y).max
    Block(XYZ(minX, minY, 0), XYZ(maxX, maxY, 0))
  }

  def simulateFall(
      settled: Set[Block],
      curr: Block
  ): (Set[Block], Block) = {
    val under = settled.filter(_.intersects(curr))
    val highest = under.maxBy(_.end.z)
    val supports = under.filter(_.end.z == highest.end.z)
    val dz = curr.start.z - (highest.end.z + 1)
    (supports, curr.decreaseZ(dz))
  }

  type Outgoing = Map[Block, List[Block]]
  type Incoming = Map[Block, Set[Block]]

  def simulateFall(
      blocks: List[Block],
      ground: Block
  ): (Set[Block], Outgoing, Incoming) = {
    var falling = blocks
    var settled = Set(ground)

    var soleBreadwinners = Set[Block]()
    val outgoing =
      mutable.Map[Block, List[Block]]().withDefault(_ => List.empty)
    var incoming = Map[Block, Set[Block]]()

    while (falling.nonEmpty) {
      val curr = falling.head
      falling = falling.tail

      val (supports, currSettled) = simulateFall(settled, curr)
      settled += currSettled
      if (supports.size == 1) {
        soleBreadwinners ++= supports
      }

      incoming += (currSettled -> supports)
      outgoing(currSettled) = List.empty
      supports.foreach(support =>
        outgoing(support) = outgoing(support).appended(currSettled)
      )
    }

    (
      soleBreadwinners - ground,
      outgoing.toMap,
      incoming
    )
  }

  def countChainReaction(outgoing: Outgoing, incoming: Incoming)(
      start: Block
  ) = {
    var supportedBy = incoming
    val queue = mutable.Queue[Block](start)
    val falls = mutable.Set[Block]()

    while (queue.nonEmpty) {
      val curr = queue.dequeue()
      falls.add(curr)

      for (out <- outgoing(curr)) {
        supportedBy = supportedBy.updated(out, supportedBy(out) - curr)
        if (supportedBy(out).isEmpty) {
          queue.enqueue(out)
        }
      }
    }

    (falls - start).size
  }

  def solve(lines: List[String]): List[Int] = {
    val blocks = parse(lines)
    val ground = getGround(blocks)
    val (soleBreadwinners, outgoing, incoming) = simulateFall(blocks, ground)

    val part1 = blocks.size - soleBreadwinners.size
    val part2 = soleBreadwinners.toList
      .map(countChainReaction(outgoing, incoming))
      .sum

    List(part1, part2)
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
