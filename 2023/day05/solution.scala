import util.FileUtils

object Solution {
  type Ago = Long
  type Mapping = Array[(Long, Long, Ago)]
  type Mappings = Array[Mapping]

  def parse(lines: List[String]): (Iterable[Long], Mappings) = {
    val sections = lines.mkString("\n").split("\n\n")

    val seeds = sections(0)
      .replace("seeds: ", "")
      .split(" ")
      .map(_.toLong)

    val maps =
      sections
        .drop(1)
        .map(
          _.split("\n")
            .drop(1)
            .map(line => {
              val values = line.split(" ").map(_.toLong)
              values match {
                case Array(a, b, c) => (a, b, c)
              }
            })
        )

    (seeds, maps)
  }

  def toLocation(maps: Mappings)(seed: Long): Long = {
    maps.foldLeft(seed)((current, mapping) => {
      var result = current
      for (line <- mapping) {
        val (dst, src, len) = line
        if (src <= current && current < src + len) {
          result = current - src + dst
        }
      }
      result
    })
  }

  def part2(seeds: Iterable[Long], maps: Mappings): Long = {
    var currentSeedGroups = seeds
      .grouped(2)
      .map(_.toList)
      .map(list => (list(0), list(0) + list(1)))
      .toList
    var nextSeedGroups = List[(Long, Long)]()

    for (map <- maps) {
      while (currentSeedGroups.nonEmpty) {
        val (start, end) = currentSeedGroups.head
        currentSeedGroups = currentSeedGroups.tail

        var notIntersectCount = 0

        for ((dst, src, len) <- map) {
          val startWithinRange = src <= start && start < src + len
          val endWithinRange = src < end && end <= src + len

          (startWithinRange, endWithinRange) match {
            case (false, false) => {
              notIntersectCount += 1
            }
            case (true, true) => {
              val delta = (dst - src)
              nextSeedGroups = (start + delta, end + delta) :: nextSeedGroups
            }
            case (true, false) => { // start within range
              val left = (start, src + len) // split the range
              val right = (src + len, end)
              currentSeedGroups = left :: right :: currentSeedGroups
            }
            case (false, true) => { // end within range
              val left = (start, src) // split the range
              val right = (src, end)
              currentSeedGroups = left :: right :: currentSeedGroups
            }
          }
        }
        if (notIntersectCount == map.length) {
          nextSeedGroups = (start, end) :: nextSeedGroups
        }
      }
      currentSeedGroups = nextSeedGroups
      nextSeedGroups = List.empty
    }

    currentSeedGroups.map(_._1).min
  }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))
    val (seeds, maps) = parse(lines)

    val part1 = seeds.map(toLocation(maps)).min
    println(s"Part 1: $part1")

    println(s"Part 2: ${part2(seeds, maps)}")
  }
}
