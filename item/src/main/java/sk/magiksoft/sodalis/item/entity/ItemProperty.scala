package sk.magiksoft.sodalis.item.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import java.io.Serializable
import sk.magiksoft.sodalis.item.presenter.Presenter
import scala.beans.BeanProperty
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2010/5/20
 */

class ItemProperty extends AbstractDatabaseEntity {
  @BeanProperty var typeName: String = ""
  @BeanProperty var name: String = ""
  var propertyTypes = new ListBuffer[String]
  @BeanProperty var model: Serializable = null
  @BeanProperty var presenterClassName: String = null
  @BeanProperty var column = 0
  @BeanProperty var rows = 1
  @BeanProperty var tableColumn = true

  def getPropertyTypes = bufferAsJavaList(propertyTypes)

  def setPropertyTypes(collection: Any) = collection match {
    case l: java.util.List[_] => propertyTypes = new ListBuffer[String] ++ l.map(_.asInstanceOf[String])
    case l: ListBuffer[_] => propertyTypes = l.map(_.asInstanceOf[String])
    case _ => println(collection)
  }

  def getValue(item: Item) = {
    val presenter = Class.forName(presenterClassName.trim).newInstance.asInstanceOf[Presenter]

    presenter.getReadableValue(item.values.find(v => v.itemPropertyID == getId) match {
      case Some(value) => value.value
      case None => null
    })
  }

  def updateFrom(entity: DatabaseEntity) = {
    entity match {
      case iip: ItemProperty => {

      }
    }
  }


}
