package sk.magiksoft.sodalis.item.ui

import scala.swing.{Component, ScrollPane, BorderPanel}
import java.util.List
import sk.magiksoft.sodalis.item.entity.{Item, ItemType}
import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.sodalis.core.data.DataListener
import sk.magiksoft.sodalis.category.ui.CategoryTreeComponent
import sk.magiksoft.swing.ISTable
import sk.magiksoft.sodalis.core.factory.ColorList
import scala.swing.BorderPanel.Position
import sk.magiksoft.sodalis.core.module.Module
import sk.magiksoft.swing.table.SelectionListener
import scala.collection.mutable.ListBuffer
import sk.magiksoft.sodalis.category.entity.Category
import sk.magiksoft.sodalis.core.entity.DatabaseEntity
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2010/6/22
 */

class ItemTypeTablePanel(val itemType: ItemType, val itemTableModel: ObjectTableModel[Item]) extends BorderPanel with DataListener {
  def this(itemType: ItemType) = this(itemType, new ItemTableModel(itemType))

  var categoryTreeComponent: Option[CategoryTreeComponent] = None
  val table = new ISTable(itemTableModel) {
    setName("item.table." + itemType.getId)
  }

  add(new ScrollPane(Component.wrap(table)) {
    peer.getViewport.setBackground(ColorList.SCROLLPANE_BACKGROUND)
  }, Position.Center)

  protected def initCategoryTreeComponent(moduleClass: Class[_ <: Module]) {
    categoryTreeComponent = Option(new CategoryTreeComponent(moduleClass, itemTableModel, contents(0).asInstanceOf[ScrollPane].peer))
  }

  def addSelectionListener(listener: SelectionListener) {
    table.addSelectionListener(listener)
    categoryTreeComponent match {
      case Some(categoryTreeComponent) => categoryTreeComponent.addSelectionListener(listener)
      case None =>
    }
  }

  def getSelectedItems = {
    val items = new ListBuffer[Item]

    useCategoryComponentItems match {
      case true => for (item <- categoryTreeComponent.get.getSelectedObjects if item.isInstanceOf[Item]) {
        items += item.asInstanceOf[Item]
      }
      case false => for (row <- table.getSelectedRows) {
        items += itemTableModel.getObject(row)
      }
    }

    items.toList
  }

  def setCategoryTreeVisible(visible: Boolean, selectedCategories: List[Category]) {
    categoryTreeComponent match {
      case Some(categoryTreeComponent) => categoryTreeComponent.refresh(visible, selectedCategories)
      case None =>
    }
  }

  private def useCategoryComponentItems = categoryTreeComponent match {
    case Some(categoryTreeComponent) => categoryTreeComponent.isComponentShown
    case None => false
  }

  def setSelectedItems(items: collection.immutable.List[Item]) {
    table.clearSelection()
    for (index <- items.map {
      item => itemTableModel.indexOf(item)
    } if index != (-1)) {
      table.getSelectionModel.addSelectionInterval(index, index)
    }
  }

  def entitiesAdded(entities: List[_ <: DatabaseEntity]) {
    categoryTreeComponent match {
      case Some(categoryTreeComponent) => if (!entities.filter(_.isInstanceOf[Item]).isEmpty) categoryTreeComponent.refresh()
      case None =>
    }
    for (entity <- entities if entity.isInstanceOf[Item] && (itemType.getId.equals(-1l) || entity.asInstanceOf[Item].itemType.getId == itemType.getId)) {
      itemTableModel.addObject(entity.asInstanceOf[Item])
    }
  }

  def entitiesRemoved(entities: List[_ <: DatabaseEntity]) {
    categoryTreeComponent match {
      case Some(categoryTreeComponent) => if (!entities.filter(_.isInstanceOf[Item]).isEmpty) categoryTreeComponent.refresh()
      case None =>
    }
    for (entity <- entities if entity.isInstanceOf[Item] && (itemType.getId.equals(-1l) || entity.asInstanceOf[Item].itemType.getId == itemType.getId)) {
      itemTableModel.removeObject(entity.asInstanceOf[Item])
    }
  }

  def entitiesUpdated(entities: List[_ <: DatabaseEntity]) {
    var updated = false
    for (entity <- entities if entity.isInstanceOf[Item] && (itemType.getId.equals(-1l) || entity.asInstanceOf[Item].itemType.getId == itemType.getId)) {
      itemTableModel.getObjects.find(_.getId == entity.getId) match {
        case Some(item) => {
          item.updateFrom(entity)
          updated = true
        }
        case None =>
      }
    }
    if (updated) {
      categoryTreeComponent match {
        case Some(categoryTreeComponent) => categoryTreeComponent.isComponentShown match {
          case true => categoryTreeComponent.repaint()
          case false => fireTableDataChanged()
        }
        case None => fireTableDataChanged()
      }
    }
  }

  private def fireTableDataChanged() {
    val selected = table.getSelectedRows

    itemTableModel.fireTableDataChanged()
    selected.foreach(index => table.getSelectionModel.addSelectionInterval(index, index))
  }

}
