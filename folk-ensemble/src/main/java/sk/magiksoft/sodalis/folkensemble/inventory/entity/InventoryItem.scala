
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.folkensemble.inventory.entity

import sk.magiksoft.sodalis.item.entity.Item
import sk.magiksoft.sodalis.category.entity.{Category, Categorized}
import sk.magiksoft.sodalis.core.history.{HistoryEvent, Historizable}
import sk.magiksoft.sodalis.core.logger.LoggerManager
import collection.JavaConversions._
import java.lang.Long
import reflect.BeanProperty
import sk.magiksoft.sodalis.core.entity.{PostCreation, DatabaseEntity}
import java.util.{Map, HashMap, ArrayList, List}
import sk.magiksoft.sodalis.category.CategoryDataManager
import sk.magiksoft.sodalis.folkensemble.inventory.settings.InventorySettings
import sk.magiksoft.sodalis.core.settings.Settings

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jul 1, 2010
 * Time: 11:04:14 AM
 * To change this template use File | Settings | File Templates.
 */

class InventoryItem extends Item with Historizable with Categorized {
  var categories: List[Category] = new ArrayList[Category]
  @BeanProperty var inventoryItemDatas: Map[Class[_], InventoryItemData] = new HashMap[Class[_], InventoryItemData]

  @PostCreation
  def initInventoryItemDatas(properties: Array[Any]) {
    for (property <- properties) {
      if (property.isInstanceOf[Class[_]] && classOf[InventoryItemData].isAssignableFrom(property.asInstanceOf[Class[_]])) {
        try {
          inventoryItemDatas.put(property.asInstanceOf[Class[_ <: InventoryItemData]], (property.asInstanceOf[Class[_]]).newInstance.asInstanceOf[InventoryItemData])
        }
        catch {
          case ex: InstantiationException => {
            LoggerManager.getInstance.error(classOf[InventoryItem], ex)
          }
          case ex: IllegalAccessException => {
            LoggerManager.getInstance.error(classOf[InventoryItem], ex)
          }
        }
      }
    }
    setCategories(CategoryDataManager.getInstance.getCategories(InventorySettings.getInstance.getValue(Settings.O_SELECTED_CATEGORIES).asInstanceOf[List[java.lang.Long]]))
  }

  def getInventoryItemData[T](clazz: Class[T]): T = {
    return inventoryItemDatas.get(clazz).asInstanceOf[T]
  }

  override def updateFrom(entity: DatabaseEntity): Unit = {
    if (entity != this) {
      super.updateFrom(entity)

      entity match {
        case inventoryItem: InventoryItem => {
          this.categories.clear
          this.categories.addAll(inventoryItem.categories)
          for (inventoryItemData <- inventoryItemDatas.values) {
            inventoryItemData.updateFrom(inventoryItem.getInventoryItemData(inventoryItemData.getClass).asInstanceOf[InventoryItemData])
          }
        }
      }
    }
  }

  def getCategories: List[Category] = {
    return categories
  }


  def setCategories(categories: List[Category]): Unit = {
    this.categories = categories
  }


  def addHistoryEvent(event: HistoryEvent): Unit = {
    val historyEvents: List[HistoryEvent] = getInventoryItemData(classOf[InventoryHistoryData]).getHistoryEvents
    historyEvents.add(event)
  }

  def getHistoryEvents(entityID: Long) = getInventoryItemData(classOf[InventoryHistoryData]).getHistoryEvents
}