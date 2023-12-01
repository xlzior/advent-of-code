package util

import scala.io.Source

object FileUtils {
  def readFileContents(fileName: String): List[String] = {
    try {
      val source = Source.fromFile(fileName)
      val lines = source.getLines().toList
      source.close()
      lines
    } catch {
      case e: Exception =>
        println(s"Error reading file: ${e.getMessage}")
        List.empty[String]
    }
  }
}
