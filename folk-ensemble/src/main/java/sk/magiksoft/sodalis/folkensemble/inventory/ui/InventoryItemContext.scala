package sk.magiksoft.sodalis.folkensemble.inventory.ui

import java.lang.String
import sk.magiksoft.sodalis.item.ui.DefaultItemContext
import sk.magiksoft.sodalis.core.utils.UIUtils
import sk.magiksoft.sodalis.folkensemble.inventory.settings.InventorySettings
import sk.magiksoft.sodalis.folkensemble.inventory.{InventoryContextManager, InventoryModule}
import swing.FlowPanel
import sk.magiksoft.sodalis.category.ui.{CategoryTreeComponent, CategoryTreeComboBox}
import java.awt.{GridBagLayout, GridBagConstraints}
import javax.swing.{AbstractButton, JScrollPane}
import sk.magiksoft.swing.ISTable
import swing.Swing._
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslator
import sk.magiksoft.sodalis.folkensemble.inventory.entity.BorrowingInventoryItemData.InventoryItemState
import sk.magiksoft.sodalis.folkensemble.inventory.entity.{BorrowingInventoryItemData, InventoryItem}
import sk.magiksoft.sodalis.core.filter.ui.FilterPanel
import sk.magiksoft.sodalis.item.entity.Item
import sk.magiksoft.sodalis.item.factory.ItemPropertiesFactory
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.item.entity.property.ItemTypeEntityPropertyTranslator

/**
 * @author wladimiiir
 * @since 2010/7/31
 */

class InventoryItemContext(itemPropertiesFactory: Option[ItemPropertiesFactory])
  extends DefaultItemContext(classOf[InventoryItem], "Inventory", Option(new InventoryControlPanel), itemPropertiesFactory) {
  SodalisApplication.get.getStorageManager.registerComponent("inventoryItemUI", this)

  override protected def createHelperCategoryTreeComponent = Option(new CategoryTreeComponent(classOf[InventoryModule], new GeneralInventoryItemTableModel, new JScrollPane(new ISTable)))

  override protected def getTranslator = currentItemType match {
    case Some(itemType) => itemType.getId.asInstanceOf[Long] match {
      case -1l => new GeneralEntityPropertyTranslator
      case _ => new ItemTypeEntityPropertyTranslator(itemType)
    }
    case None => new GeneralEntityPropertyTranslator
  }

  override protected def createToolBar = {
    val toolBar = UIUtils.createToolBar
    val c = new GridBagConstraints

    toolBar.setLayout(new GridBagLayout)
    c.gridx = 0
    c.gridy = 0
    toolBar.add(addButton.peer, c)
    c.gridx += 1
    toolBar.add(removeButton.peer, c)
    itemDefinitionPanel match {
      case Some(itemDefinitionPanel) => {
        c.gridx += 1
        toolBar.add(showItemDefinitionButton.peer)
      }
      case None =>
    }
    c.gridx += 1
    toolBar.add(printButton.peer, c)
    c.gridx += 1
    toolBar.add(importButton.peer, c)
    c.gridx += 1
    toolBar.add(exportButton.peer, c)

    categoryTreeComponent match {
      case Some(categoryTreeComponent) => {
        val showCategoryTreeButton: AbstractButton = categoryTreeComponent.getShowCategoryTreeButton
        showCategoryTreeButton.setFocusable(false)
        showCategoryTreeButton.setBorderPainted(false)
        showCategoryTreeButton.setPreferredSize((25, 25))
        c.gridx += 1
        toolBar.add(showCategoryTreeButton, c)
      }
      case None =>
    }

    categoryTreeComboBox = new CategoryTreeComboBox(classOf[InventoryModule])
    categoryTreeComboBox.addChangeListener(new CategoryTreeComboBoxChangeListener(InventorySettings.getInstance, InventoryContextManager))

    c.gridx += 1
    c.weightx = 1.0
    toolBar.add(new FlowPanel {
      opaque = false
    }.peer, c)

    c.gridx += 1
    c.weightx = 0.0
    toolBar.add(categoryTreeComboBox)

    toolBar
  }

  override protected def getFilterPanel = InventoryContextManager.getFilterPanel match {
    case filterPanel: FilterPanel => Option(filterPanel)
    case _ => None
  }

  override protected def createItemSubContextPanel(itemTypesKey: String) = new InventoryItemSubContextPanel(itemTypesKey)

  private class GeneralEntityPropertyTranslator extends EntityPropertyTranslator[Item] {
    def getTranslations = List(
      EntityTranslation("inventoryItem", item => Option(item.itemType.name)),
      EntityTranslation("evidenceNumber", item => item.getPropertyTypeValues("EvidenceNumber").headOption match {
        case Some(value) => Option(value.value.toString)
        case None => None
      }),
      EntityTranslation("description", item => Option(item.getInfoString)),
      EntityTranslation("state", item => {
        val inventoryItemData = item.asInstanceOf[InventoryItem].getInventoryItemData(classOf[BorrowingInventoryItemData])
        val state = inventoryItemData.getState
        Option(state match {
          case InventoryItemState.BORROWED => state + " (" + inventoryItemData.getCurrentBorrowing.getBorrower.getFullName(false) + ")"
          case _ => state
        })
      })
    )
  }

}
