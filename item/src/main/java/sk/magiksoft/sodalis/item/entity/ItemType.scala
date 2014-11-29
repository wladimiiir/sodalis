package sk.magiksoft.sodalis.item.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import collection.mutable.ListBuffer
import scala.beans.BeanProperty
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2010/5/20
 */

class ItemType extends AbstractDatabaseEntity {
  @BeanProperty var key: String = null
  @BeanProperty var name = ""
  var itemProperties = new ListBuffer[ItemProperty]

  def setItemProperties(collection: Any) = collection match {
    case l: java.util.List[_] => itemProperties = new ListBuffer[ItemProperty] ++ l.map(_.asInstanceOf[ItemProperty])
    case l: ListBuffer[_] => itemProperties = l.map(_.asInstanceOf[ItemProperty])
    case _ => println(collection)
  }

  def getItemProperties = bufferAsJavaList(itemProperties)

  def updateFrom(entity: DatabaseEntity) = {
    entity match {
      case it: ItemType => {

      }
    }
  }
}
