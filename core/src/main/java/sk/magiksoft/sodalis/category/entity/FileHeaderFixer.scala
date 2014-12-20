package sk.magiksoft.sodalis.category.entity

import java.io.{FileWriter, BufferedWriter, BufferedReader, File}
import java.text.{ParseException, DateFormat}
import java.util.Calendar
import java.util.regex.Pattern

import scala.io.Source

/**
 * @author wladimiiir 
 * @since 2014/11/28
 */
object FileHeaderFixer extends App {
  val outputPath = "./output"
  val datePattern = "\\s+\\*\\s+Date:\\s+(\\d+)/(\\d+)/(\\d+)\\s*".r

  processDir("", new File("."))

  def processDir(currentPath: String, dir: File) {
    dir.listFiles()
      .filter(file => file.isDirectory || file.getName.endsWith(".java") || file.getName.endsWith(".scala"))
      .foreach(file => file.isDirectory match {
      case true => processDir(currentPath + File.separatorChar + file.getName, file)
      case false => processFile(currentPath, file)
    })
  }

  def processFile(currentPath: String, file: File) {
    def createWriter(filePath: String) = {
      val file = new File(filePath)
      file.getParentFile.mkdirs()
      new BufferedWriter(new FileWriter(file))
    }

    val reader = Source.fromFile(file, "UTF-8").bufferedReader()
    val writer = createWriter(outputPath + File.separatorChar + currentPath + File.separatorChar + file.getName)
    processReader(reader, writer, packageAdded = false)
    reader.close()
    writer.close()
  }

  def processReader(reader: BufferedReader, writer: BufferedWriter, packageAdded: Boolean): Unit = reader.readLine() match {
    case line: String
      if line == " * Created by IntelliJ IDEA."
        || line == " * To change this template use File | Settings | File Templates." =>
      processReader(reader, writer, packageAdded)

    case line: String if line matches "\\s+\\*\\s+Time:\\s+\\d+:\\d+(:\\d+)?\\s+[A-Z]+" =>
      processReader(reader, writer, packageAdded)

    case datePattern(month, day, year) =>
      writer.write(" * @since 20" + year + "/" + month + "/" + day)
      writer.newLine()
      processReader(reader, writer, packageAdded)

    case line: String if line startsWith " * Date:" =>
      try {
        val date = DateFormat.getDateInstance.parse(line.substring(9).trim)
        val calendar = Calendar.getInstance()
        calendar.setTime(date)
        writer.write(" * @since " + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DATE))
        writer.newLine()
        processReader(reader, writer, packageAdded)
      } catch {
        case e: ParseException =>
          println("Wrong date: " + line.substring(9).trim)
          System.exit(1)
      }

    case line: String if line startsWith "package " =>
      writer.write(line)
      writer.newLine()
      processReader(reader, writer, packageAdded = true)

    case line: String =>
      packageAdded match {
        case true =>
          writer.write(line)
          writer.newLine()
        case false =>
      }
      processReader(reader, writer, packageAdded)

    case _ =>
  }
}
