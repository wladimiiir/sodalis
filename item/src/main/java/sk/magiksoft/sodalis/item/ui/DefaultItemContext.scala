package sk.magiksoft.sodalis.item.ui

import swing.event.ButtonClicked
import sk.magiksoft.sodalis.core.entity.{Entity, DatabaseEntity}
import sk.magiksoft.sodalis.core.data.DefaultDataManager
import sk.magiksoft.sodalis.core.utils.UIUtils
import sk.magiksoft.swing.HideableSplitPane
import sk.magiksoft.sodalis.core.utils.Conversions._
import collection.JavaConversions._
import sk.magiksoft.sodalis.item.event.{ItemTypeSelectionChanged, ItemSelectionChanged}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import collection.mutable.ListBuffer
import sk.magiksoft.sodalis.core.ui.{ISOptionPane, AbstractContext, OkCancelDialog}
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.item.entity.{ItemPropertyValue, Item, ItemType}
import sk.magiksoft.sodalis.core.factory.{EntityFactory, IconFactory}
import sk.magiksoft.sodalis.item.factory.ItemPropertiesFactory
import sk.magiksoft.sodalis.item.action.{ItemTypeImportAction, ItemTypeExportAction, ItemImportAction, ItemExportAction}
import java.awt.{GridBagConstraints, GridBagLayout, BorderLayout}
import swing.{BorderPanel, Swing, Button}
import sk.magiksoft.sodalis.core.module.Module
import sk.magiksoft.swing.table.SelectionListener
import sk.magiksoft.sodalis.category.ui.CategoryTreeComponent
import javax.swing.{AbstractButton, JScrollPane, JOptionPane, JSplitPane}
import collection.immutable.List
import sk.magiksoft.sodalis.category.CategoryManager
import sk.magiksoft.sodalis.item.entity.property.ItemTypeEntityPropertyTranslator
import sk.magiksoft.sodalis.item.ItemSettings
import sk.magiksoft.sodalis.core.settings.Settings
import sk.magiksoft.sodalis.core.printing.{TableColumnWrapper, TablePrintSettings, TablePrintDialog}
import sk.magiksoft.sodalis.category.report.{CategoryWrapperDataSource, CategoryPathWrapper}
import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.sodalis.core.entity.property.{EntityPropertyTranslator, EntityPropertyTranslatorManager, EntityPropertyJRDataSource}
import java.util.Collections
import sk.magiksoft.sodalis.core.ui.controlpanel.ControlPanel
import sk.magiksoft.sodalis.core.filter.ui.FilterPanel
import Swing._

/**
 * @author wladimiiir
 * @since 2010/6/25
 */

class DefaultItemContext(itemClass: Class[_ <: Item], itemTypeKey: String, controlPanel: Option[ControlPanel], itemPropertiesFactory: Option[ItemPropertiesFactory]) extends AbstractContext(itemClass) {
  protected var currentItem: Option[_ <: Item] = None
  protected var currentItemType: Option[ItemType] = None
  protected val itemSubContextPanel = createItemSubContextPanel(itemTypeKey)
  itemSubContextPanel.reactions += {
    case ItemSelectionChanged(item) => {
      currentItem = item
      currentObjectChanged()
    }

    case ItemTypeSelectionChanged(itemType) => {
      currentItemType = itemType
      currentItemType match {
        case Some(itemType) => {
          addButton.enabled = !itemType.getId.equals(-1l)
          currentItem = itemSubContextPanel.getSelectedItems.headOption
          currentObjectChanged()
        }
        case None => {
          addButton.enabled = false
          currentItem = None
          currentObjectChanged()
        }
      }
    }
  }
  val itemDefinitionPanel = itemPropertiesFactory match {
    case Some(factory) => Option(new ItemDefinitionPanel(itemTypeKey, factory))
    case None => None
  }
  protected val mainPanel = new BorderPanel

  protected val addButton = new Button {
    icon = IconFactory.getInstance.getIcon("add")
    borderPainted = false
    focusable = false
    enabled = false
    preferredSize = (25, 25)
    reactions += {
      case ButtonClicked(_) => addItem()
    }
  }
  protected val removeButton = new Button {
    icon = IconFactory.getInstance.getIcon("remove")
    borderPainted = false
    focusable = false
    enabled = false
    preferredSize = (25, 25)
    reactions += {
      case ButtonClicked(_) => removeItem()
    }
  }
  protected val showItemDefinitionButton = new Button {
    icon = IconFactory.getInstance.getIcon("edit")
    borderPainted = false
    focusable = false
    preferredSize = (25, 25)
    reactions += {
      case ButtonClicked(_) => itemDefinitionPanel.get isShowing match {
        case true => {
          remove(itemDefinitionPanel.get.peer)
          add(mainPanel, BorderLayout.CENTER)
          addButton.visible = true
          removeButton.visible = true
        }
        case false => {
          remove(mainPanel)
          add(itemDefinitionPanel.get, BorderLayout.CENTER)
          addButton.visible = false
          removeButton.visible = false
        }
      }
        DefaultItemContext.this.revalidate()
        DefaultItemContext.this.repaint()
    }
  }
  protected val printButton = new Button {
    icon = IconFactory.getInstance.getIcon("print")
    borderPainted = false
    focusable = false
    preferredSize = (25, 25)
    reactions += {
      case ButtonClicked(_) => printItems()
    }
  }
  protected val importButton = new Button {
    icon = IconFactory.getInstance.getIcon("import")
    borderPainted = false
    focusable = false
    preferredSize = (25, 25)
    reactions += {
      case ButtonClicked(_) => itemDefinitionPanel match {
        case Some(itemDefinitionPanel) => itemDefinitionPanel.isShowing match {
          case true => importItemTypes()
          case false => importItems()
        }
        case None => importItems()
      }
    }
  }
  protected val exportButton = new Button {
    icon = IconFactory.getInstance.getIcon("export")
    borderPainted = false
    focusable = false
    preferredSize = (25, 25)
    reactions += {
      case ButtonClicked(_) => itemDefinitionPanel match {
        case Some(itemDefinitionPanel) => itemDefinitionPanel.isShowing match {
          case true => exportItemTypes()
          case false => exportItems()
        }
        case None => exportItems()
      }
    }
  }
  protected val categoryTreeComponent: Option[CategoryTreeComponent] = createHelperCategoryTreeComponent
  protected val itemExportAction = new ItemExportAction(this)
  protected val itemImportAction = new ItemImportAction(this)
  protected val itemTypeExportAction = new ItemTypeExportAction(this)
  protected val itemTypeImportAction = new ItemTypeImportAction(this)
  protected val itemTypeDialogMap = scala.collection.mutable.Map.empty[Long, ItemDialog]

  initComponents()
  itemSubContextPanel.publish(new ItemTypeSelectionChanged(itemSubContextPanel.getCurrentItemType))
  itemSubContextPanel.setSelectionListener(new SelectionListener {
    def selectionWillBeChanged = canChangeEntity
  })

  private def initComponents() {
    setLayout(new BorderLayout)
    mainPanel.peer.add(controlPanel match {
      case Some(controlPanel) => new HideableSplitPane(JSplitPane.VERTICAL_SPLIT, itemSubContextPanel, controlPanel.getControlComponent) {
        setName("itemContext.splitPane." + itemTypeKey)
        setLeftText(LocaleManager.getString("items"))
        setRightText(LocaleManager.getString("details"))
      }
      case None => itemSubContextPanel.peer
    }, BorderLayout.CENTER)
    controlPanel match {
      case Some(controlPanel) => controlPanel.getControlComponent.setMinimumSize((200, 340))
      case None =>
    }
    categoryTreeComponent match {
      case Some(categoryTreeComponent) => categoryTreeComponent.addActionListener(Swing.ActionListener(_ => {
        itemSubContextPanel.setCategoryTreeVisible(categoryTreeComponent.isComponentShown, categoryTreeComponent.getSelectedCategories)
      }))
      case None =>
    }
    add(createToolBar, BorderLayout.NORTH)
    add(mainPanel, BorderLayout.CENTER)
    getFilterPanel match {
      case Some(filterPanel) => add(filterPanel, BorderLayout.EAST)
      case None =>
    }
  }

  protected def getFilterPanel: Option[FilterPanel] = None

  protected def createHelperCategoryTreeComponent: Option[CategoryTreeComponent] = None

  protected def createItemSubContextPanel(itemTypesKey: String) = new ItemSubContextPanel(itemTypeKey)

  protected def createToolBar = {
    val toolBar = UIUtils.createToolBar
    val c = new GridBagConstraints

    toolBar.setLayout(new GridBagLayout)
    c.gridx = 0
    c.gridy = 0
    toolBar.add(addButton, c)
    c.gridx += 1
    c.anchor = GridBagConstraints.WEST
    toolBar.add(removeButton, c)
    itemDefinitionPanel match {
      case Some(itemDefinitionPanel) => {
        c.gridx += 1
        toolBar.add(showItemDefinitionButton)
      }
      case None =>
    }
    c.gridx += 1
    toolBar.add(printButton, c)
    c.gridx += 1
    toolBar.add(importButton, c)
    c.gridx += 1
    toolBar.add(exportButton, c)

    categoryTreeComponent match {
      case Some(categoryTreeComponent) => {
        c.gridx += 1
        categoryTreeComponent.getShowCategoryTreeButton.setPreferredSize((25, 25))
        toolBar.add(categoryTreeComponent.getShowCategoryTreeButton, c)
      }
      case None =>
    }

    toolBar
  }

  protected def addItem() {
    var itemTypeDialog: Option[ItemDialog] = None

    itemTypeDialogMap.get(currentItemType.get.getId.longValue) match {
      case Some(dialog) => itemTypeDialog = Option(dialog)
      case None => {
        itemTypeDialog = Option(new ItemDialog(currentItemType.get))
        itemTypeDialogMap += currentItemType.get.getId.longValue -> itemTypeDialog.get
      }
    }

    itemTypeDialog match {
      case Some(dialog) => {
        dialog.presenterPanel.clear
        dialog.setVisible(true)
      }
      case None =>
    }
  }

  protected def removeItem() {
    val selected = getSelectedEntities
    val result = selected.size match {
      case 0 => None
      case 1 => ISOptionPane.showConfirmDialog(this, LocaleManager.getString("deleteItemConfirm"), LocaleManager.getString("item"), JOptionPane.YES_NO_OPTION)
      case _ => ISOptionPane.showConfirmDialog(this, LocaleManager.getString("deleteItemsConfirm"), LocaleManager.getString("items"), JOptionPane.YES_NO_OPTION)
    }

    result match {
      case JOptionPane.YES_OPTION => {
        for (item <- selected) {
          DefaultDataManager.getInstance.removeDatabaseEntity(item)
        }
      }
      case _ =>
    }
  }

  protected def importItemTypes() {
    itemTypeImportAction.actionPerformed(null)
  }

  protected def importItems() {
    itemImportAction.actionPerformed(null)
  }

  protected def exportItemTypes() {
    itemTypeExportAction.actionPerformed(null)
  }

  protected def exportItems() {
    itemExportAction.actionPerformed(null)
  }

  protected def printItems() {
    val categoryComponent = itemSubContextPanel.getSelectedItemTypeTablePanel match {
      case Some(itemTypePanel) => itemTypePanel.categoryTreeComponent
      case None => None
    }
    val categoryShown = categoryComponent match {
      case Some(categoryTreeComponent) => categoryTreeComponent.isComponentShown
      case None => false
    }
    val items = categoryShown match {
      case true => asScalaBuffer(CategoryManager.getInstance.getCategoryPathWrappers(categoryComponent.get.getRoot)).toList
      case false => itemSubContextPanel.getAllItemTypeItems
    }
    val dataSource = categoryShown match {
      case true => new CategoryWrapperDataSource(seqAsJavaList(items.asInstanceOf[List[CategoryPathWrapper]]), new EntityPropertyJRDataSource[Item](List.empty[Item]))
      case false => new EntityPropertyJRDataSource[Item](items.asInstanceOf[List[Item]])
    }
    val translator = getTranslator
    val dialog = new TablePrintDialog(getDefaultPrintSettings, translator, getUserPrintSettings, dataSource)

    EntityPropertyTranslatorManager.registerTranslator(itemClass, translator)
    if (categoryShown) {
      dialog.setGroups(categoryComponent.get.getSelectedCategoryPath)
    }
    dialog.setVisible(true)
  }

  protected def getDefaultPrintSettings = {
    val settings = new TablePrintSettings("")
    val tableColumns = new ListBuffer[TableColumnWrapper]

    itemSubContextPanel.getSelectedItemTypeTablePanel match {
      case Some(itemTypePanel) => {
        for (column <- itemTypePanel.table.getColumnModel.getColumns) {
          val identifier = itemTypePanel.table.getModel.asInstanceOf[ObjectTableModel[Item]].getColumnIdentificator(column.getModelIndex)
          if (identifier != null) {
            tableColumns += new TableColumnWrapper(identifier.toString, column.getHeaderValue.toString, 75)
          }
        }
      }
      case None =>
    }

    settings.setTableColumnWrappers(seqAsJavaList(tableColumns))
    settings.setShowPageNumbers(true)
    settings
  }

  protected def getUserPrintSettings = ItemSettings.getValue(Settings.O_USER_PRINT_SETTINGS).asInstanceOf[java.util.List[TablePrintSettings]]

  protected def getTranslator: EntityPropertyTranslator[Item] = currentItemType match {
    case Some(itemType) => new ItemTypeEntityPropertyTranslator(itemType)
    case None => null
  }

  override def currentObjectChanged() {
    controlPanel match {
      case Some(controlPanel) => {
        controlPanel.setupControlPanel(currentItem match {
          case Some(item) => item
          case None => null
        })
        controlPanel.setAdditionalObjects(currentItem match {
          case Some(item) => bufferAsJavaList(getSelectedEntities.filter {
            _ != item
          }.map {
            _.asInstanceOf[Object]
          })
          case None => Collections.emptyList[Object]
        })
      }

      case None =>
    }
    currentItem match {
      case Some(item) => {
        removeButton.enabled = true
      }
      case None => {
        removeButton.enabled = false
      }
    }
  }

  def removeAllRecords() {
    itemSubContextPanel.removeAllItems()
  }

  def getEntities = bufferAsJavaList(new ListBuffer[Item] ++ itemSubContextPanel.getAllItems)

  def getSelectedEntities = new ListBuffer[Item] ++ itemSubContextPanel.getSelectedItems

  def setSelectedEntities(entities: java.util.List[_ <: Entity]) {
    itemSubContextPanel.setSelectedItems(asScalaBuffer(entities.asInstanceOf[java.util.List[Item]]).toList)
  }

  def canChangeEntity = controlPanel match {
    case Some(controlPanel) => {
      if (controlPanel.isEditing) {
        ISOptionPane.showConfirmDialog(this, LocaleManager.getString("saveChangesQuestion"),
          currentItem.get.toString(), JOptionPane.YES_NO_CANCEL_OPTION) match {
          case JOptionPane.YES_OPTION => {
            controlPanel.doUpdate()
            true
          }
          case JOptionPane.NO_OPTION => {
            controlPanel.cancelEditing()
            true
          }
          case JOptionPane.CANCEL_OPTION => false
        }
      } else {
        true
      }
    }
    case None => true
  }

  def entitiesRemoved(entities: java.util.List[_ <: DatabaseEntity]) {
    itemSubContextPanel.entitiesRemoved(entities)
  }

  def entitiesUpdated(entities: java.util.List[_ <: DatabaseEntity]) {
    itemSubContextPanel.entitiesUpdated(entities)
    for (entity <- entities if entity.isInstanceOf[ItemType]) {
      itemTypeDialogMap -= (entity.getId)
    }
  }

  def entitiesAdded(entities: java.util.List[_ <: DatabaseEntity]) {
    itemSubContextPanel.entitiesAdded(entities.filter(e => super.acceptEntity(e)))
  }

  protected class ItemDialog(val itemType: ItemType) extends OkCancelDialog(SodalisApplication.get.getMainFrame, itemType.name) {
    val presenterPanel = ItemTypePresenterPanel(itemType)

    setModal(true)
    setMainPanel(new JScrollPane(presenterPanel.peer) {
      setBorder(null)
    })
    setSize(800, 400)
    setLocationRelativeTo(null)

    getOkButton.addActionListener(Swing.ActionListener(e => {
      val item = EntityFactory.getInstance().createEntity(itemClass)
      item.itemType = itemType
      item.values = new ListBuffer[ItemPropertyValue] ++ presenterPanel.getValues
      DefaultDataManager.getInstance.addDatabaseEntity(item)
    }))
  }

}
