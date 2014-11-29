package sk.magiksoft.sodalis.item.entity

import java.io.Serializable
import sk.magiksoft.sodalis.core.entity.{AbstractDatabaseEntity, DatabaseEntity}
import scala.beans.BeanProperty

/**
 * @author wladimiiir
 * @since 2010/5/28
 */

class ItemPropertyValue extends AbstractDatabaseEntity {
  @BeanProperty var itemPropertyID: Long = -1
  @BeanProperty var value: Serializable = null

  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case propertyValue: ItemPropertyValue if propertyValue ne this => {
        this.value = propertyValue.value
      }
    }
  }
}
