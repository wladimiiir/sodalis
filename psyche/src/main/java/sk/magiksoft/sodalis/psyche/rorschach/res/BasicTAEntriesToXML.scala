package sk.magiksoft.sodalis.psyche.rorschach.res

import tools.nsc.io.Path
import java.util.LinkedList
import sk.magiksoft.sodalis.psyche.rorschach.entity.BasicTAEntry
import scala.reflect.io.File
import sk.magiksoft.sodalis.core.imex.ImExManager

/**
 * @author wladimiiir
 * @since 2011/5/22
 */

object BasicTAEntriesToXML {
  def main(args: Array[String]) {
    exportBasicTAEntries()
  }

  def exportBasicTAEntries() {
    val csvFile = File(Path("basicTAEntries.csv"))
    val reader = csvFile.bufferedReader()
    val entries = new LinkedList[BasicTAEntry]()

    read()
    ImExManager.exportData(new java.io.File("basicTAEntries.xml"), entries)

    def read() {
      reader.readLine() match {
        case line: String if !line.isEmpty => {
          val values = line.split(";")
          val entry = new BasicTAEntry
          entry.answerCount = values(0)
          entry.entry = values(1)
          entry.notMarked = values(2)
          entry.inBrackets = values(3)
          entry.marked = values(4)
          entry.underlined = values(5)
          entry.doubleUnderlined = values(6)
          entries.add(entry)
          read()
        }
        case _ =>
      }
    }
  }
}
