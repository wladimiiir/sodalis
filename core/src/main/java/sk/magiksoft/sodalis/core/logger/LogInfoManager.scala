package sk.magiksoft.sodalis.core.logger

import java.util.Calendar
import java.text.{DateFormat, DecimalFormat}
import org.dom4j.{DocumentFactory, Element}
import java.io.StringWriter

/**
 * @author wladimiiir
 * @since 2011/2/4
 */
object LogInfoManager {
  val ELEMENT_INFO = "info"
  val ELEMENT_PROPERTY = "property"
  val ELEMENT_PROPERTIES = "properties"
  val ATTRIBUTE_NAME = "name"
  private val doubleFormat = new DecimalFormat("0.00###")
  private val dateTimeFormat = DateFormat.getDateTimeInstance
  private val dateFormat = DateFormat.getDateInstance
  private val timeFormat = DateFormat.getTimeInstance

  def createLogInfo(info: LogInfo): String = {
    val document = new DocumentFactory().createDocument
    val writer = new StringWriter
    info.addLogInfoNode(document.addElement(ELEMENT_INFO))
    document.write(writer)
    writer.close
    writer.toString
  }

  def addValue(parent: Element, name: String, value: Any) = {
    val stringValue = value match {
      case string: String => Option(string)
      case int: Int => Option(int.toString)
      case double: Double => Option(doubleFormat.format(double))
      case calendar: Calendar => if (calendar == null) None else Option(dateTimeFormat.format(calendar.getTime))
      case historizable: LogInfo => {
        historizable.addLogInfoNode(parent.addElement(ELEMENT_PROPERTIES).addAttribute(ATTRIBUTE_NAME, name))
        None
      }
    }
    stringValue match {
      case Some(string) => addString(parent, name, string)
      case None =>
    }
  }

  def addString(parent: Element, name: String, stringValue: String) = {
    parent.addElement(ELEMENT_PROPERTY).addAttribute(ATTRIBUTE_NAME, name).setText(stringValue)
  }

  def addDate(parent: Element, name: String, date: Calendar) = date match {
    case c: Calendar => addString(parent, name, dateFormat.format(c.getTime))
    case _ =>
  }

  def addTime(parent: Element, name: String, date: Calendar) = date match {
    case c: Calendar => addString(parent, name, timeFormat.format(c.getTime))
    case _ =>
  }
}
