
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.item.entity

import java.io.Serializable
import sk.magiksoft.sodalis.core.entity.{AbstractDatabaseEntity, DatabaseEntity}
import scala.beans.BeanProperty

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: May 28, 2010
 * Time: 9:43:47 PM
 * To change this template use File | Settings | File Templates.
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