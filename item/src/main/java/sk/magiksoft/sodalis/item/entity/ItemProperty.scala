
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.item.entity

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity
import java.io.Serializable
import reflect.BeanProperty
import sk.magiksoft.sodalis.item.presenter.Presenter

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: May 20, 2010
 * Time: 11:05:58 PM
 * To change this template use File | Settings | File Templates.
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

  def getPropertyTypes = asList(propertyTypes)

  def setPropertyTypes(collection: Any) = collection match {
    case l: java.util.List[String] => propertyTypes = new ListBuffer[String] ++ l
    case l: ListBuffer[String] => propertyTypes = l
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