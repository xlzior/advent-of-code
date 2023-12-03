import util.FileUtils

object Solution {
  type Destination = (String, Int)
  type Rule = (Destination, Destination)
  type Rules = Map[Int, Rule]
  def Rules(): Rules = Map[Int, Rule]()

  type Chip = Int
  type State = Map[Int, List[Chip]]
  def State(): State = Map[Int, List[Chip]]().withDefaultValue(List.empty)

  def updateState(state: State, key: Int, value: Int): State = {
    state.updated(key, state(key).appended(value))
  }

  val rule =
    """bot (\d+) gives low to (bot|output) (\d+) and high to (bot|output) (\d+)""".r
  val chip = """value (\d+) goes to bot (\d+)""".r

  def parse(lines: List[String]): (Rules, State, State) = {
    val (rules, bots) = lines.foldLeft((Rules(), State()))((acc, curr) =>
      curr match {
        case rule(bot, lowType, low, highType, high) => {
          val (rules, state) = acc
          val rule = ((lowType, low.toInt), (highType, high.toInt))
          (rules.updated(bot.toInt, rule), state)
        }
        case chip(value, bot) => {
          val (rules, state) = acc
          val botChips = state(bot.toInt).appended(value.toInt)
          (rules, state.updated(bot.toInt, botChips))
        }
      }
    )

    (rules, bots, State())
  }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))

    var (rules, bots, outputs) = parse(lines)

    var stepped = true
    while (stepped) {
      stepped = false

      bots.keys
        .filter(i => bots(i).length == 2)
        .foreach(i => {
          stepped = true
          val ((lowType, low), (highType, high)) = rules(i)
          val (min, max) = (bots(i).min, bots(i).max)

          if (min == 17 && max == 61) {
            println(s"Part 1: $i")
          }

          bots = bots.updated(i, List.empty)

          lowType match {
            case "output" => outputs = updateState(outputs, low, min)
            case "bot"    => bots = updateState(bots, low, min)
          }

          highType match {
            case "output" => outputs = updateState(outputs, high, max)
            case "bot"    => bots = updateState(bots, high, max)
          }
        })
    }

    println(
      s"Part 2: ${outputs(0).head * outputs(1).head * outputs(2).head}"
    )
  }

}
