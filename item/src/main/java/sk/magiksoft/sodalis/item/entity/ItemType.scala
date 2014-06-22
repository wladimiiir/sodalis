
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.item.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import collection.mutable.ListBuffer
import scala.beans.BeanProperty
import scala.collection.JavaConversions._

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: May 20, 2010
 * Time: 10:37:02 PM
 * To change this template use File | Settings | File Templates.
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