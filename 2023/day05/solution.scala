import util.FileUtils
import util.Interval

object Solution {
  type Mapping = Array[(Interval[Long], Long)]
  type Mappings = Array[Mapping]

  def parse(lines: List[String]): (Iterable[Long], Mappings) = {
    val sections = lines.mkString("\n").split("\n\n")
    val seeds = sections(0).split(" ").drop(1).map(_.toLong)
    val maps = sections
      .drop(1)
      .map(
        _.split("\n")
          .drop(1)
          .map(_.split(" ").map(_.toLong) match {
            case Array(dst, src, len) => (Interval(src, src + len), dst - src)
          })
      )
    (seeds, maps)
  }

  def toLocation(maps: Mappings)(seed: Long): Long = {
    maps.foldLeft(seed)((current, mapping) => {
      mapping.foldLeft(current) {
        case (acc, (interval, delta)) => {
          if (interval.contains(current)) current + delta else acc
        }
      }
    })
  }

  def part2(seeds: Iterable[Long], maps: Mappings): Long = {
    var current = seeds
      .grouped(2)
      .map(_.toList)
      .map(list => Interval(list(0), list(0) + list(1)))
      .toList
    var next = List[Interval[Long]]()

    for (map <- maps) {
      while (current.nonEmpty) {
        val seedGroup = current.head
        current = current.tail

        val matched = map.foldLeft(false) { case (acc, (interval, delta)) =>
          seedGroup.split(interval) match {
            case List(group) => {
              // atomic: no further splitting
              if (interval.intersects(group)) {
                next = (group + delta) :: next
                true
              } else {
                acc
              }
            }
            case splitIntervals: List[Interval[Long]] => {
              current = splitIntervals ++ current
              true
            }
          }
        }

        if (!matched) next = seedGroup :: next
      }
      current = next
      next = List.empty
    }

    current.map(_.start).min
  }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.read(args(0))
    val (seeds, maps) = parse(lines)

    println(s"Part 1: ${seeds.map(toLocation(maps)).min}")
    println(s"Part 2: ${part2(seeds, maps)}")
  }
}
