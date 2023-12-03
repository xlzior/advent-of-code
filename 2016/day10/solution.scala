import util.FileUtils

object Solution {
  type Destination = (String, Int)
  type Rule = (Destination, Destination)
  type Rules = Map[Int, Rule]
  type Chip = Int
  type State = Array[List[Chip]]

  val rule =
    """bot (\d+) gives low to (bot|output) (\d+) and high to (bot|output) (\d+)""".r
  val chip = """value (\d+) goes to bot (\d+)""".r

  def parse(
      lines: List[String],
      numBots: Int,
      numOutputs: Int
  ): (Rules, State, State) = {
    val (rules, botState) = lines.foldLeft(
      (Map[Int, Rule](), Array.fill[List[Chip]](numBots)(List.empty))
    )((acc, curr) =>
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

    val outputState = Array.fill[List[Chip]](numOutputs)(List.empty)

    (rules, botState, outputState)
  }

  def main(args: Array[String]): Unit = {
    val lines: List[String] = FileUtils.readFileContents(args(0))

    val numBots = """bot (\d+)""".r
      .findAllMatchIn(lines.mkString)
      .map(m => m.group(1).toInt)
      .max + 1 // 0-indexing

    val numOutputs = """output (\d+)""".r
      .findAllMatchIn(lines.mkString)
      .map(m => m.group(1).toInt)
      .max + 1 // 0-indexing

    val (rules, botState, outputState) = parse(lines, numBots, numOutputs)

    var stepped = true
    while (stepped) {
      stepped = false
      (0 to numBots - 1).foreach(i => {
        if (botState(i).length == 2) {
          stepped = true
          val (low, high) = rules(i)
          val min = botState(i).min
          val max = botState(i).max

          if (min == 17 && max == 61) {
            println(s"Part 1: $i")
          }
          low._1 match {
            case "output" =>
              outputState(low._2) = outputState(low._2).appended(min)
            case "bot" => botState(low._2) = botState(low._2).appended(min)
          }

          high._1 match {
            case "output" =>
              outputState(high._2) = outputState(high._2).appended(max)
            case "bot" => botState(high._2) = botState(high._2).appended(max)
          }

          botState(i) = List.empty
        }
      })
    }

    println(s"Part 2: ${outputState.take(3).map(_.head).product}")
  }

}
