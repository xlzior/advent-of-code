import util.FileUtils
import java.security.MessageDigest

def md5(s: String): String = {
  MessageDigest
    .getInstance("MD5")
    .digest(s.getBytes)
    .map("%02X".format(_))
    .mkString
}

object Solution {
  def part1(id: String): String = {
    var password: String = ""
    var i: Int = 0

    while (password.length() < 8) {
      val hash = md5(id + i.toString)
      i += 1
      if (hash.startsWith("00000")) {
        println(hash(5))
        password += hash(5)
      }
    }

    password.toLowerCase()
  }

  def part2(id: String): String = {
    var password: Array[Char] = Array.fill(8)(' ')
    var i: Int = 0

    while (password.contains(' ')) {
      val hash = md5(id + i.toString)
      i += 1
      if (hash.startsWith("00000")) {
        val position = hash(5).asDigit
        val character = hash(6)

        if (position < 8 && password(position) == ' ') {
          password(position) = character
          println(password.mkString.toLowerCase())
        }
      }
    }

    password.mkString.toLowerCase()
  }

  def main(args: Array[String]): Unit = {
    val id = FileUtils.readFileContents(args(0))(0)

    args(1) match {
      case "1" => println(s"Part 1: ${part1(id)}")
      case "2" => println(s"Part 2: ${part2(id)}")
    }
  }
}
