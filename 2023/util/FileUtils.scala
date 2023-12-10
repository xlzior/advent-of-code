package util

import java.io.File
import scala.io.Source

object FileUtils {
  def read(fileName: String): List[String] = {
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

  def testFiles: Array[String] = {
    val files = File(".").listFiles.map(_.getName)
    val inFiles = files.filter(_.endsWith(".in")).map(_.replace(".in", ""))
    val outFiles = files.filter(_.endsWith(".out")).map(_.replace(".out", ""))
    inFiles.filter(file => outFiles.contains(file)).sorted
  }
}
