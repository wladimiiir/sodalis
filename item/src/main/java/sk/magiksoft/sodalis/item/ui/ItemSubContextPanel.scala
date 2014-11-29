package sk.magiksoft.sodalis.item.ui

import collection.JavaConversions._
import java.lang.String
import swing._
import event.ButtonClicked
import java.util.List
import sk.magiksoft.sodalis.core.data.{DefaultDataManager, DataListener}
import sk.magiksoft.swing.table.SelectionListener
import javax.swing.event.{ListSelectionEvent, ListSelectionListener}
import sk.magiksoft.swing.CardPanel
import sk.magiksoft.sodalis.item.entity.{Item, ItemType}
import sk.magiksoft.sodalis.item.event.{ItemTypeSelectionChanged, ItemSelectionChanged}
import collection.mutable.{ListBuffer, Buffer}
import swing.Swing._
import sk.magiksoft.sodalis.core.utils.Conversions._
import sk.magiksoft.sodalis.category.entity.Category
import swing.GridBagPanel.Fill
import sk.magiksoft.sodalis.core.entity.DatabaseEntity
import sk.magiksoft.sodalis.core.locale.LocaleManager
import scala.swing.BorderPanel.Position
import sk.magiksoft.sodalis.core.table.ObjectTableModel

/**
 * @author wladimiiir
 * @since 2010/6/22
 */

class ItemSubContextPanel(val itemTypeKey: String) extends BorderPanel with DataListener {
  private val itemTypesPanel = new CardPanel {
    def add(itemType: ItemType) {
      super.add(createAndRegisterItemTablePanel(itemType), itemType.getId.toString)
    }

    def remove(itemType: ItemType) {
      contents.find(c => c.isInstanceOf[ItemTypeTablePanel] && c.asInstanceOf[ItemTypeTablePanel].itemType.getId == itemType.getId) match {
        case Some(component) => {
          peer.remove(component.peer)
          revalidate()
          repaint()
        }
        case None =>
      }
    }

    def update(itemType: ItemType) {
      contents.find(c => c.isInstanceOf[ItemTypeTablePanel] && c.asInstanceOf[ItemTypeTablePanel].itemType.getId == itemType.getId) match {
        case Some(component) => {
          peer.remove(component.peer)
          add(itemType)
        }
        case None => add(itemType)
      }
    }
  }

  private val buttonsPanel = new GridBagPanel {
    val emptyPanel = new FlowPanel
    val c = new Constraints
    val group = new ButtonGroup

    c.grid = (0, 0)
    c.fill = Fill.Horizontal
    c.insets = new Insets(3, 3, 0, 3)
    maximumSize = (500, 500)

    def add(itemType: ItemType) = {
      val button = new ItemTypeToggleButton(itemType)
      group.buttons += button
      peer.remove(emptyPanel.peer)
      c.weighty = 0.0
      super.add(button, c)
      c.gridy += 1
      c.weighty = 1.0
      super.add(emptyPanel, c)
    }

    def remove(itemType: ItemType) {
      contents.find(c => c.isInstanceOf[ItemTypeToggleButton] && c.asInstanceOf[ItemTypeToggleButton].itemType.getId == itemType.getId) match {
        case Some(button) => {
          group.buttons -= button.asInstanceOf[ItemTypeToggleButton]
          group.buttons.find(_ => true) match {
            case Some(button) => group.select(button)
            case None =>
          }
          peer.remove(button.peer)
        }
        case None =>
      }
    }

    def update(itemType: ItemType) = {
      contents.find(c => c.isInstanceOf[ItemTypeToggleButton] && c.asInstanceOf[ItemTypeToggleButton].itemType.getId == itemType.getId) match {
        case Some(button) => {
          button.asInstanceOf[ItemTypeToggleButton].text = itemType.name
        }
        case None => {
          add(itemType)
        }
      }
    }
  }
  private var selectionListener: Option[SelectionListener] = None

  initComponents()
  reloadItemTypes()
  buttonsPanel.contents.filter(c => c.isInstanceOf[ItemTypeToggleButton]).headOption match {
    case Some(button) => button.asInstanceOf[ItemTypeToggleButton].doClick()
    case None =>
  }

  def setCategoryTreeVisible(visible: Boolean, selectedCategories: List[Category]) {
    for (itemTypePanel <- itemTypesPanel.contents if itemTypePanel.isInstanceOf[ItemTypeTablePanel]) {
      itemTypePanel.asInstanceOf[ItemTypeTablePanel].setCategoryTreeVisible(visible, selectedCategories)
    }
  }

  private def itemTypes: Buffer[ItemType] = DefaultDataManager.getInstance.getDatabaseEntities(classOf[ItemType], itemTypeKey match {
    case key: String => "key='" + key + "'"
    case _ => "key is null"
  })

  protected def initComponents() {
    add(new ScrollPane(buttonsPanel) {
      preferredSize = (130, 150)
    }, Position.West)
    add(itemTypesPanel, Position.Center)
  }

  protected def createItemTypeTablePanel(itemType: ItemType): ItemTypeTablePanel = {
    new ItemTypeTablePanel(itemType)
  }

  protected def createGeneralItemTypeTablePanel(itemType: ItemType): ItemTypeTablePanel = {
    new ItemTypeTablePanel(itemType, getGeneralItemTableModel.get)
  }

  protected def getGeneralItemTableModel: Option[ObjectTableModel[Item]] = None

  private def createAndRegisterItemTablePanel(itemType: ItemType): ItemTypeTablePanel = {
    val itemTypeTablePanel = if (!itemType.getId.equals(-1l)) createItemTypeTablePanel(itemType)
    else createGeneralItemTypeTablePanel(itemType)
    itemTypeTablePanel.table.getSelectionModel.addListSelectionListener(new ListSelectionListener {
      def valueChanged(e: ListSelectionEvent) {
        if (itemTypeTablePanel eq getSelectedItemTypeTablePanel.get) {
          currentObjectChanged()
        }
      }
    })
    selectionListener match {
      case Some(selectionListener) => {
        itemTypeTablePanel.addSelectionListener(selectionListener)
      }
      case None =>
    }

    itemTypeTablePanel
  }

  protected def currentObjectChanged() {
    publish(new ItemSelectionChanged(getSelectedItems.headOption))
  }

  private def reloadItemTypes() {
    getGeneralItemTableModel match {
      case Some(model) => addAllOption()
      case None =>
    }

    for (itemType <- itemTypes) {
      addItemType(itemType)
    }
  }

  private def addAllOption() {
    val itemType = new ItemType
    itemType.setId(-1)
    itemType.name = LocaleManager.getString("allTypes")
    itemType.key = itemTypeKey
    addItemType(itemType)
  }

  private def addItemType(itemType: ItemType) {
    itemTypesPanel.add(itemType)
    buttonsPanel.add(itemType)
    itemTypesPanel.currentConstraints match {
      case Some(constraints) => {
        itemTypesPanel.show(constraints)
        publish(new ItemTypeSelectionChanged(Option(itemType)))
      }
      case None =>
    }
  }

  private def removeItemType(itemType: ItemType) {
    itemTypesPanel.remove(itemType)
    buttonsPanel.remove(itemType)
    if (itemType.getId.toString == itemTypesPanel.currentConstraints) {
      itemTypesPanel.currentConstraints = None
      publish(new ItemTypeSelectionChanged(None))
    }
  }

  private def updateItemType(itemType: ItemType) {
    itemTypesPanel.update(itemType)
    buttonsPanel.update(itemType)
    itemTypesPanel.currentConstraints match {
      case Some(constraints) => {
        itemTypesPanel.show(constraints)
        publish(new ItemTypeSelectionChanged(Option(itemType)))
      }
      case None =>
    }
  }

  def getCurrentItemType = {
    buttonsPanel.contents.find(c => c.isInstanceOf[ToggleButton] && c.asInstanceOf[ToggleButton].selected) match {
      case Some(button) => Option(button.asInstanceOf[ItemTypeToggleButton].itemType)
      case None => None
    }
  }

  def removeAllItems() {
    for (itemTypePanel <- itemTypesPanel.contents if itemTypePanel.isInstanceOf[ItemTypeTablePanel]) {
      itemTypePanel.asInstanceOf[ItemTypeTablePanel].itemTableModel.removeAllObjects()
    }
  }

  def getAllItems = {
    val items = new ListBuffer[Item]
    for (itemTypePanel <- itemTypesPanel.contents if itemTypePanel.isInstanceOf[ItemTypeTablePanel]) {
      items ++= itemTypePanel.asInstanceOf[ItemTypeTablePanel].itemTableModel.getObjects
    }
    items.toList
  }

  def getAllItemTypeItems = getSelectedItemTypeTablePanel match {
    case Some(itemTypeTablePanel) => asScalaBuffer(itemTypeTablePanel.itemTableModel.getObjects).toList
    case None => scala.collection.immutable.List.empty[Item]
  }

  def getSelectedItems = {
    import scala.collection.immutable.List
    getSelectedItemTypeTablePanel match {
      case Some(itemTypeTablePanel) => itemTypeTablePanel.getSelectedItems
      case None => List.empty[Item]
    }
  }

  def getSelectedItemTypeTablePanel = itemTypesPanel.currentConstraints match {
    case Some(constraints) => itemTypesPanel.contents.find(c => c.isInstanceOf[ItemTypeTablePanel]
      && c.asInstanceOf[ItemTypeTablePanel].itemType.getId.toString == constraints) match {
      case Some(c) => Option(c.asInstanceOf[ItemTypeTablePanel])
      case None => None
    }
    case None => None
  }

  def setSelectedItems(items: collection.immutable.List[Item]) {
    for (itemTypePanel <- itemTypesPanel.contents if itemTypePanel.isInstanceOf[ItemTypeTablePanel]) {
      itemTypePanel.asInstanceOf[ItemTypeTablePanel].setSelectedItems(items)
    }
  }

  def setSelectionListener(listener: SelectionListener) {
    selectionListener = Option(listener)
    for (itemTypePanel <- itemTypesPanel.contents if itemTypePanel.isInstanceOf[ItemTypeTablePanel]) {
      itemTypePanel.asInstanceOf[ItemTypeTablePanel].addSelectionListener(listener)
    }
  }

  def entitiesRemoved(entities: List[_ <: DatabaseEntity]) {
    for (itemTypePanel <- itemTypesPanel.contents if itemTypePanel.isInstanceOf[ItemTypeTablePanel]) {
      itemTypePanel.asInstanceOf[ItemTypeTablePanel].entitiesRemoved(entities)
    }
    for (entity <- entities if entity.isInstanceOf[ItemType]) {
      removeItemType(entity.asInstanceOf[ItemType])
    }
  }

  def entitiesUpdated(entities: List[_ <: DatabaseEntity]) {
    for (itemTypePanel <- itemTypesPanel.contents if itemTypePanel.isInstanceOf[ItemTypeTablePanel]) {
      itemTypePanel.asInstanceOf[ItemTypeTablePanel].entitiesUpdated(entities)
    }

    for (entity <- entities if entity.isInstanceOf[ItemType]) {
      updateItemType(entity.asInstanceOf[ItemType])
    }
  }

  def entitiesAdded(entities: List[_ <: DatabaseEntity]) {
    for (itemTypePanel <- itemTypesPanel.contents if itemTypePanel.isInstanceOf[ItemTypeTablePanel]) {
      itemTypePanel.asInstanceOf[ItemTypeTablePanel].entitiesAdded(entities)
    }

    for (entity <- entities if entity.isInstanceOf[ItemType]) {
      addItemType(entity.asInstanceOf[ItemType])
    }
  }

  private class ItemTypeToggleButton(var itemType: ItemType) extends ToggleButton(itemType.name) {
    tooltip = itemType.name
    focusable = false
    preferredSize = (121, 21)
    reactions += {
      case ButtonClicked(_) => {
        itemTypesPanel.show(itemType.getId.toString)
        ItemSubContextPanel.this.publish(new ItemTypeSelectionChanged(Option(itemType)))
      }
    }
  }

}
