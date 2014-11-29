package sk.magiksoft.sodalis.item.ui

import scala.swing.{ScrollPane, BorderPanel}
import sk.magiksoft.sodalis.core.factory.ColorList
import sk.magiksoft.sodalis.core.data.DataListener
import java.util.List
import collection.JavaConversions._
import sk.magiksoft.sodalis.core.utils.Conversions._
import sk.magiksoft.sodalis.item.entity.{Item, ItemType}
import sk.magiksoft.swing.ISTable
import scala.collection.mutable.ListBuffer
import sk.magiksoft.sodalis.core.entity.DatabaseEntity

/**
 * @author wladimiiir
 * @since 2010/6/22
 */

class AllItemTypesTablePanel(var itemType: ItemType) extends BorderPanel with DataListener {
  val model = new ItemTableModel(itemType)
  val table = new ISTable(model)

  add(new ScrollPane(table) {
    peer.getViewport.setBackground(ColorList.SCROLLPANE_BACKGROUND)
  }, BorderPanel.Position.Center)

  def getSelectedItems = {
    val items = new ListBuffer[Item]

    for (row <- table.getSelectedRows) {
      items += model.getObject(row)
    }

    items.toList
  }

  def setSelectedItems(items: collection.immutable.List[Item]) = {
    table.clearSelection
    for (index <- items.map {
      item => model.indexOf(item)
    } if index != (-1)) {
      table.getSelectionModel.addSelectionInterval(index, index)
    }
  }

  def entitiesRemoved(entities: List[_ <: DatabaseEntity]) = {
    for (entity <- entities if entity.isInstanceOf[Item] && entity.asInstanceOf[Item].itemType.getId == itemType.getId) {
      model.removeObject(entity.asInstanceOf[Item])
    }
  }

  def entitiesUpdated(entities: List[_ <: DatabaseEntity]) = {
    for (entity <- entities if entity.isInstanceOf[Item] && entity.asInstanceOf[Item].itemType.getId == itemType.getId) {
      model.getObjects.find(i => i.getId == entity.getId) match {
        case Some(item) => item.updateFrom(entity)
        case None =>
      }
    }
  }

  def entitiesAdded(entities: List[_ <: DatabaseEntity]) = {
    for (entity <- entities if entity.isInstanceOf[Item] && entity.asInstanceOf[Item].itemType.getId == itemType.getId) {
      model.addObject(entity.asInstanceOf[Item])
    }
  }
}
