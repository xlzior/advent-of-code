import util.FileUtils

class Destination
case class Bot(id: Int) extends Destination {
  override def toString(): String = s"bot $id"
}
case class Output(id: Int) extends Destination {
  override def toString(): String = s"output $id"
}

class Rule(val low: Destination, val high: Destination) {
  override def toString(): String = s"$low / $high"
}

type Chip = Int

object Solution {
  val rule =
    """bot (\d+) gives low to (bot|output) (\d+) and high to (bot|output) (\d+)""".r
  val chip = """value (\d+) goes to bot (\d+)""".r

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))

    val n =
      """bot (\d+)""".r
        .findAllMatchIn(lines.mkString)
        .map(m => m.group(1).toInt)
        .max + 1 // 0-indexing

    var (rules, state) =
      lines.foldLeft((Map[Int, Rule](), Array.fill[List[Chip]](n)(List.empty)))(
        (acc, curr) =>
          curr match {
            case rule(bot, lowType, low, highType, high) => {
              val (rules, state) = acc
              val b = bot.toInt
              val l = low.toInt
              val h = high.toInt
              val lowDestination =
                if (lowType == "output") Output(l) else Bot(l)
              val highDestination =
                if (highType == "output") Output(h) else Bot(h)
              (rules.updated(b, Rule(lowDestination, highDestination)), state)
            }
            case chip(value, bot) => {
              val (rules, state) = acc
              val b = bot.toInt
              val v = value.toInt
              (rules, state.updated(b, state(b).appended(v)))
            }
          }
      )

    (0 to n).foreach(i => {
      if (state(i).length == 2) {
        val rule = rules(i)
        val low = state(i).min
        val high = state(i).max
        state(i) = List()
        state(rule.low) = low :: state(rule.low)
        state(rule.high) = high :: state(rule.high)
      }
    })

    println(state)
  }
}
