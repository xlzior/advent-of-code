import scala.collection.mutable.Set
import scala.collection.mutable.Map
import scala.collection.mutable.Queue

import util._

class XYZ(val x: Int, val y: Int, val z: Int) {
  def decreaseZ(dz: Int): XYZ = XYZ(x, y, z - dz)

  override def toString(): String = s"($x, $y, $z)"

  override def equals(x: Any): Boolean = {
    x match {
      case other: XYZ =>
        this.x == other.x && this.y == other.y && this.z == other.z
      case _ => false
    }
  }
  override def hashCode(): Int = (x, y, z).hashCode()
}

class Block(val start: XYZ, val end: XYZ) {
  def intersects(other: Block): Boolean = {
    this.start.x <= other.end.x && other.start.x <= this.end.x &&
    this.start.y <= other.end.y && other.start.y <= this.end.y
  }

  def decreaseZ(dz: Int): Block = Block(start.decreaseZ(dz), end.decreaseZ(dz))

  override def toString(): String = s"$start ~ $end"

  override def equals(x: Any): Boolean = x match {
    case other: Block => this.start == other.start && this.end == other.end
    case _            => false
  }
  override def hashCode(): Int = (start, end).hashCode()
}

object Day extends Solution {
  def getGround(blocks: List[Block]): Block = {
    val minX = blocks.map(_.start.x).min
    val minY = blocks.map(_.start.y).min
    val maxX = blocks.map(_.end.x).max
    val maxY = blocks.map(_.end.y).max
    Block(XYZ(minX, minY, 0), XYZ(maxX, maxY, 0))
  }

  def simulateFall(settled: Set[Block], curr: Block): (Set[Block], Int) = {
    val under = settled.filter(_.intersects(curr))
    val highest = under.maxBy(_.end.z)
    val supports = under.filter(_.end.z == highest.end.z)
    (supports, curr.start.z - (highest.end.z + 1))
  }

  def solve(lines: List[String]): List[Int] = {
    var falling = lines
      .map(line => {
        val Array(sx, sy, sz, ex, ey, ez) =
          """\d+""".r.findAllIn(line).map(_.toInt).toArray
        Block(XYZ(sx, sy, sz), XYZ(ex, ey, ez))
      })
      .sortBy(_.start.z)

    val ground = getGround(falling)
    val settled = Set(ground)
    val soleBreadwinners = Set[Block]()
    val outgoing = Map[Block, List[Block]](ground -> List.empty)
    val incoming = Map[Block, Set[Block]]()

    while (falling.nonEmpty) {
      val curr = falling.head
      falling = falling.tail

      val (supports, dz) = simulateFall(settled, curr)
      val currSettled = curr.decreaseZ(dz)
      settled.add(currSettled)
      if (supports.size == 1) {
        soleBreadwinners.addAll(supports)
      }

      incoming(currSettled) = supports
      outgoing(currSettled) = List.empty
      supports.foreach(support =>
        outgoing(support) = outgoing(support).appended(currSettled)
      )
    }

    val part1 = settled.size - soleBreadwinners.size

    val part2 = (soleBreadwinners - ground).toList
      .map(start => {
        var supportedBy = incoming.toMap.map((k, v) => (k, v.toSet))
        val queue = Queue[Block](start)
        val falls = Set[Block]()

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
      })
      .sorted

    // println(part2)

    List(part1, part2.sum)
  }

  def main(args: Array[String]): Unit = {
    assert(testsPass)

    val lines: List[String] = FileUtils.read(s"${args(0)}.in")
    val solution = solve(lines)
    println(s"Part 1: ${solution.head}")
    println(s"Part 2: ${solution.last}")
  }
}
