package sk.magiksoft.sodalis.psyche.rorschach.res

import java.util

import tools.nsc.io.File
import collection.mutable.ListBuffer
import sk.magiksoft.sodalis.psyche.rorschach.entity.SpecialSign
import java.util.LinkedList
import scala.reflect.io.Path
import sk.magiksoft.sodalis.core.imex.ImExManager

/**
 * @author wladimiiir
 * @since 2011/5/18
 */

object SpecialSignsToXML {

  def exportSpecialSigns() {
    val file = File(Path("zvlastne.txt"))
    val reader = file.bufferedReader()
    val specialSigns = new util.LinkedList[SpecialSign]

    read()
    reader.close()

    def read() {
      reader.readLine() match {
        case line: String if line.startsWith("*") =>
          readSpecialSignCategory(line.substring(1))
          read()
        case line: String => read()
        case _ =>
      }
    }

    def readSpecialSignCategory(categoryName: String) {
      reader.readLine() match {
        case line: String if line.startsWith("*") =>
          readSpecialSignCategory(line.substring(1))
        case line: String if line.trim.endsWith("+-") =>
          val specialSign = new SpecialSign
          specialSign.name = line.trim.dropRight(2).toLowerCase
          specialSign.category = categoryName
          specialSign.qualitySign = true
          specialSigns.add(specialSign)
          readSpecialSignCategory(categoryName)
        case line: String if !line.trim.isEmpty =>
          val specialSign = new SpecialSign
          specialSign.name = line.trim.toLowerCase
          specialSign.category = categoryName
          specialSign.qualitySign = false
          specialSigns.add(specialSign)
          readSpecialSignCategory(categoryName)
        case line: String => readSpecialSignCategory(categoryName)
        case _ =>
      }
    }

    ImExManager.exportData(new java.io.File("specialSigns.xml"), specialSigns)
  }

  def main(args: Array[String]) {
    exportSpecialSigns()
  }
}
