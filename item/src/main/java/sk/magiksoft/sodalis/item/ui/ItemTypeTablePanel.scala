
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.item.ui

import sk.magiksoft.swing.ISTable
import swing.{BorderPanel, ScrollPane}
import sk.magiksoft.sodalis.core.factory.ColorList
import sk.magiksoft.sodalis.core.data.DataListener
import sk.magiksoft.sodalis.core.entity.DatabaseEntity
import java.util.List
import collection.JavaConversions._
import sk.magiksoft.sodalis.item.entity.{Item, ItemType}
import collection.mutable.ListBuffer
import sk.magiksoft.sodalis.core.utils.Conversions._
import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.sodalis.category.ui.CategoryTreeComponent
import sk.magiksoft.sodalis.core.module.Module
import sk.magiksoft.swing.table.SelectionListener
import sk.magiksoft.sodalis.category.entity.Category
import swing.BorderPanel.Position

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jun 22, 2010
 * Time: 7:04:46 PM
 * To change this template use File | Settings | File Templates.
 */

class ItemTypeTablePanel(val itemType: ItemType, val itemTableModel: ObjectTableModel[Item]) extends BorderPanel with DataListener {
  def this(itemType: ItemType) = this (itemType, new ItemTableModel(itemType))

  var categoryTreeComponent: Option[CategoryTreeComponent] = None
  val table = new ISTable(itemTableModel) {
    setName("item.table." + itemType.getId)
  }

  add(new ScrollPane(table) {
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
    for (index <- items.map {item => itemTableModel.indexOf(item)} if index != (-1)) {
      table.getSelectionModel.addSelectionInterval(index, index)
    }
  }

  def entitiesAdded(entities: List[_ <: DatabaseEntity]) {
    categoryTreeComponent match {
      case Some(categoryTreeComponent) => if (!entities.filter(e => e.isInstanceOf[Item]).isEmpty) categoryTreeComponent.refresh()
      case None =>
    }
    for (entity <- entities if entity.isInstanceOf[Item] && (itemType.getId.equals(-1l) || entity.asInstanceOf[Item].itemType.getId == itemType.getId)) {
      itemTableModel.addObject(entity.asInstanceOf[Item])
    }
  }

  def entitiesRemoved(entities: List[_ <: DatabaseEntity]) {
    categoryTreeComponent match {
      case Some(categoryTreeComponent) => if (!entities.filter(e => e.isInstanceOf[Item]).isEmpty) categoryTreeComponent.refresh()
      case None =>
    }
    for (entity <- entities if entity.isInstanceOf[Item] && (itemType.getId.equals(-1l) || entity.asInstanceOf[Item].itemType.getId == itemType.getId)) {
      itemTableModel.removeObject(entity.asInstanceOf[Item])
    }
  }

  def entitiesUpdated(entities: List[_ <: DatabaseEntity]) {
    var updated = false
    for (entity <- entities if entity.isInstanceOf[Item] && (itemType.getId.equals(-1l) || entity.asInstanceOf[Item].itemType.getId == itemType.getId)) {
      itemTableModel.getObjects.find(i => i.getId == entity.getId) match {
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