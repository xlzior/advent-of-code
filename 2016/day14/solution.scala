import java.security.MessageDigest
import scala.collection.mutable.Map
import scala.util.matching.Regex.Match

import util.FileUtils

def md5(s: String): String = {
  MessageDigest
    .getInstance("MD5")
    .digest(s.getBytes)
    .map("%02X".format(_))
    .mkString
    .toLowerCase()
}

def md5(s: String, n: Int): String =
  (0 to n).foldLeft(s)((acc, _) => md5(acc))

object Solution {
  val hashes = Map[Int, String]()
  val stretchedHashes = Map[Int, String]()
  val keys = Vector[String]()

  def containsTriple(hash: String): Option[Match] =
    """(\w)\1\1""".r.findFirstMatchIn(hash)

  def containsQuintuple(c: String)(hash: String): Boolean =
    s"""$c{5}""".r.findFirstIn(hash).isDefined

  def normalHash(salt: String, id: Int): String = {
    if (!hashes.contains(id)) {
      hashes(id) = md5(s"$salt$id")
    }
    hashes(id)
  }

  def stretchedHash(salt: String, id: Int): String = {
    if (!stretchedHashes.contains(id)) {
      stretchedHashes(id) = md5(s"$salt$id", 2016)
    }
    stretchedHashes(id)
  }

  def isKey(salt: String, id: Int, fn: (String, Int) => String): Boolean = {
    val repeated = containsTriple(fn(salt, id))

    if (repeated.isDefined) {
      val start = id + 1
      val end = id + 1001
      (start to end)
        .map(i => fn(salt, i))
        .exists(containsQuintuple(repeated.get.group(1)))
    } else {
      false
    }
  }

  def main(args: Array[String]): Unit = {
    val salt: String = FileUtils.read(args(0))(0)

    var id = 0
    var numKeys = 0
    while (numKeys < 64) {
      if (isKey(salt, id, normalHash)) numKeys += 1
      if (numKeys == 64) {
        println(s"Part 1: ${id}")
      }
      id += 1
    }

    id = 0
    numKeys = 0
    while (numKeys < 64) {
      if (isKey(salt, id, stretchedHash)) {
        numKeys += 1
        println(s"$numKeys: $id")
      }
      if (numKeys == 64) {
        println(s"Part 2: ${id}")
      }
      id += 1
    }
  }
}
