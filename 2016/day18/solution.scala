import scala.collection.mutable.Set
import util.FileUtils

object Solution {
  val preTrap = Set("^^.", ".^^", "^..", "..^")

  def next(row: String): String = {
    s".$row."
      .sliding(3)
      .map(seq => if (preTrap.contains(seq)) "^" else ".")
      .mkString
  }

  def countSafe(start: String, numRows: Int): Int = {
    (1 to numRows)
      .foldLeft((0, start))((acc, _) => {
        val (total, row) = acc
        (total + row.count(c => c == '.'), next(row))
      })
      ._1
  }

  def main(args: Array[String]): Unit = {
    val row: String = FileUtils.readFileContents(args(0))(0)

    assert(countSafe(".^^.^.^^^^", 10) == 38)

    println(s"Part 1: ${countSafe(row, 40)}")
    println(s"Part 2: ${countSafe(row, 400_000)}")
    // Note: rows don't seem to repeat, no point memoising
  }
}
