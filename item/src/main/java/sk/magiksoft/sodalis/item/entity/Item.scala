package sk.magiksoft.sodalis.item.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import sk.magiksoft.sodalis.item.presenter.Presenter
import java.util.{List => jList}
import scala.beans.BeanProperty
import scala.collection.mutable.ListBuffer
import org.hibernate.`type`.SerializationException
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2010/5/20
 */

class Item extends AbstractDatabaseEntity {
  @BeanProperty var itemType: ItemType = null
  var values = new ListBuffer[ItemPropertyValue]

  def setValues(collection: Any) {
    try {
      collection match {
        case l: jList[_] => values = new ListBuffer[ItemPropertyValue] ++ l.map(_.asInstanceOf[ItemPropertyValue])
        case l: ListBuffer[_] => values = l.map(_.asInstanceOf[ItemPropertyValue])
        case _ => println(collection)
      }
    } catch {
      case e: SerializationException => values = new ListBuffer[ItemPropertyValue]
    }
  }

  def getValues: jList[ItemPropertyValue] = values

  def getPropertyTypeValues(propertyType: String) = {
    val propertyTypeValues = new ListBuffer[ItemPropertyValue]

    itemType.itemProperties.filter(ip => ip.propertyTypes.contains(propertyType)).map {
      ip => ip.getId
    } match {
      case ids => propertyTypeValues ++= values.filter(ipv => ids.contains(ipv.itemPropertyID))
    }

    propertyTypeValues
  }

  def getHTMLInfoString = {
    val infoString = new StringBuilder

    for (itemProperty <- itemType.itemProperties if itemProperty.tableColumn && !itemProperty.name.isEmpty) {
      val presenter = Class.forName(itemProperty.presenterClassName.trim).newInstance.asInstanceOf[Presenter]
      val readableValue = presenter.getReadableValue(values.find(v => v.itemPropertyID == itemProperty.getId) match {
        case Some(value) => value.value
        case None => null
      })
      if (readableValue != null && !readableValue.isEmpty) {
        if (infoString.length > 0) {
          infoString.append(", ")
        }

        infoString.append("<b>").append(itemProperty.name).append(": </b>").append(readableValue)
      }
    }
    infoString.insert(0, "<html><body>").append("</body></html>")

    infoString.toString
  }

  def getInfoString = {
    val infoString = new StringBuilder

    for (itemProperty <- itemType.itemProperties if itemProperty.tableColumn && !itemProperty.name.isEmpty) {
      val presenter = Class.forName(itemProperty.presenterClassName.trim).newInstance.asInstanceOf[Presenter]
      val readableValue = presenter.getReadableValue(values.find(v => v.itemPropertyID == itemProperty.getId) match {
        case Some(value) => value.value
        case None => null
      })
      if (readableValue != null && !readableValue.isEmpty) {
        if (infoString.length > 0) {
          infoString.append("\n")
        }

        infoString.append(itemProperty.name).append(": ").append(readableValue)
      }
    }

    infoString.toString
  }

  override def toString() = getHTMLInfoString

  def updateFrom(entity: DatabaseEntity) = {
    if (entity != this) {
      entity match {
        case ii: Item => {
          //TODO:
        }
      }
    }
  }
}

object Item {
  def apply(itemType: ItemType) = {
    val item = new Item
    item.itemType = itemType
    item
  }
}
