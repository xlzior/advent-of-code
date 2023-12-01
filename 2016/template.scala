import util.FileUtils

object Solution {
  def main(args: Array[String]): Unit = {
    val lines = FileUtils.readFileContents(args(0))
  }
}
