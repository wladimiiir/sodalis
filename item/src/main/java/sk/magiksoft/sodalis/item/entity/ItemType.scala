
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.item.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import reflect.BeanProperty
import collection.JavaConversions._
import collection.mutable.ListBuffer

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
    case l: java.util.List[ItemProperty] => itemProperties = new ListBuffer[ItemProperty] ++ l
    case l: ListBuffer[ItemProperty] => itemProperties = l
    case _ => println(collection)
  }

  def getItemProperties = asList(itemProperties)

  def updateFrom(entity: DatabaseEntity) = {
    entity match {
      case it: ItemType => {

      }
    }
  }
}