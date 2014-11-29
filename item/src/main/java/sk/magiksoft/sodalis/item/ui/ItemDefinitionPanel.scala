package sk.magiksoft.sodalis.item.ui

import swing._
import scala.swing.event.{ValueChanged, ButtonClicked}
import sk.magiksoft.sodalis.core.utils.Functions
import sk.magiksoft.sodalis.core.factory.IconFactory
import sk.magiksoft.sodalis.item.presenter.Presenter
import sk.magiksoft.sodalis.item.entity.{ItemProperty, ItemType}
import sk.magiksoft.sodalis.item.factory.ItemPropertiesFactory
import sk.magiksoft.sodalis.core.data.DefaultDataManager
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.swing.itemcomponent.ItemComponentListener
import sk.magiksoft.sodalis.core.ui.ISOptionPane
import javax.swing.{JOptionPane, SpinnerNumberModel, BorderFactory, JSpinner}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import scala.collection.mutable.ListBuffer
import Swing._
import Component._

/**
 * @author wladimiiir
 * @since 2010/6/10
 */

class ItemDefinitionPanel(itemTypeKey: String, itemPropertiesFactory: ItemPropertiesFactory) extends BorderPanel {
  private val itemTypeComponent = new ItemTypeComponent(itemTypeKey)
  private val propertiesChooser = new scala.swing.ComboBox[String](itemPropertiesFactory.itemPropertyTypeNames)
  private val propertiesPanel = new PropertiesPanel {
    visible = false
  }
  private val presenterPanel = new ItemTypePresenterPanel
  private val removeItemTypes = new ListBuffer[ItemType]

  initComponents
  reloadItemTypes

  private def initComponents = {
    add(new SplitPane(Orientation.Horizontal, new GridPanel(1, 2) {
      contents += Component.wrap(itemTypeComponent)
      contents += new ScrollPane(propertiesPanel) {
        border = BorderFactory.createTitledBorder(LocaleManager.getString("properties"))
      }
    }, new ScrollPane(presenterPanel) {
      border = BorderFactory.createTitledBorder(LocaleManager.getString("preview"))
    }), BorderPanel.Position.Center)

    add(new FlowPanel(FlowPanel.Alignment.Right)(
      new Button(Action(LocaleManager.getString("save"))(saveItemTypes)) {
        icon = IconFactory.getInstance.getIcon("ok")
      }, new Button(Action(LocaleManager.getString("cancelAction"))(reloadItemTypes)) {
        icon = IconFactory.getInstance.getIcon("cancel")
      }), BorderPanel.Position.South)

    itemTypeComponent.setBorder(BorderFactory.createTitledBorder(LocaleManager.getString("itemTypes")))
    itemTypeComponent.addItemComponentListener(new ItemComponentListener[ItemType] {
      def selectionChanged = {
        val visible = itemTypeComponent.getSelectedItem match {
          case it: ItemType => true
          case _ => false
        }

        propertiesPanel.visible = visible
        presenterPanel.visible = visible
        reloadItemType
      }

      def itemUpdated(item: ItemType) = {}

      def itemRemoved(item: ItemType) = removeItemTypes += item

      def itemAdded(item: ItemType) = {}
    })
    propertiesChooser.peer.setPreferredSize((250, 22))
  }

  private def reloadItemTypes = {
    var selected = itemTypeComponent.getSelectedItem
    itemTypeComponent.setItems(DefaultDataManager.getInstance.getDatabaseEntities(classOf[ItemType], if (itemTypeKey == null) "key is null"; else "key='" + itemTypeKey + "'"))
    itemTypeComponent.setSelectedItem(selected)
    removeItemTypes.clear
  }

  def getItemTypes = itemTypeComponent.getItems

  def getSelectedItemTypes = itemTypeComponent.getSelectedItems

  private def reloadItemType = {
    propertiesPanel.reloadProperties
    presenterPanel.reload(itemTypeComponent.getSelectedItem)
  }

  private def saveItemTypes = {
    for (itemType <- removeItemTypes) {
      if (!DefaultDataManager.getInstance.canDelete(itemType)) {
        ISOptionPane.showMessageDialog(this.peer, LocaleManager.getString("cannotDelete.inUse"), LocaleManager.getString("warning"), JOptionPane.INFORMATION_MESSAGE)
      } else {
        DefaultDataManager.getInstance.removeDatabaseEntity(itemType)
      }
    }
    DefaultDataManager.getInstance.addOrUpdateEntities(itemTypeComponent.getItems)
    SodalisApplication.get.showMessage(LocaleManager.getString("successfulySaved"))
    reloadItemTypes
  }

  private def addProperty = {
    val propertyTypeName = propertiesChooser.selection.item
    val itemType = itemTypeComponent.getSelectedItem

    if (propertyTypeName != null && itemType != null) {
      itemType.itemProperties += itemPropertiesFactory.createItemProperty(propertyTypeName)
      reloadItemType
    }
  }

  private class PropertiesPanel extends BorderPanel {
    val emptyPanel = new FlowPanel

    add(createPropertyChooserPanel, BorderPanel.Position.North)

    def createPropertyChooserPanel = new GridBagPanel {
      add(propertiesChooser, new Constraints {
        grid = (0, 0)
        insets = new Insets(3, 3, 3, 3);
      })
      add(new Button(new Action(LocaleManager.getString("add")) {
        def apply() = addProperty
      }), new Constraints {
        grid = (1, 0)
        insets = new Insets(3, 0, 3, 3);
      })
      add(new FlowPanel, new Constraints {
        grid = (2, 0)
        weightx = 1.0;
        insets = new Insets(3, 0, 3, 0);
      })
    }

    def reloadProperties: Unit = {
      val itemType = itemTypeComponent.getSelectedItem

      itemType match {
        case it: ItemType => {
          var panel = new GridBagPanel {
            val c = new Constraints
            c.grid = (0, 0)
            c.weightx = 1.0
            c.insets = new Insets(1, 0, 1, 0)
            c.fill = GridBagPanel.Fill.Horizontal
            for (itemProperty <- it.itemProperties) {
              add(createPropertyPanel(itemProperty), c)
              c.gridy += 1
            }
            c.weighty = 1.0
            add(new FlowPanel, c)
          }

          add(panel, BorderPanel.Position.Center)
          revalidate
          repaint()
        }
        case _ => add(emptyPanel, BorderPanel.Position.Center)
      }

      def createPropertyPanel(itemProperty: ItemProperty) = {
        val presenter = Class.forName(itemProperty.presenterClassName.trim).newInstance.asInstanceOf[Presenter]

        new GridBagPanel {
          add(new Label(itemProperty.typeName) {
            tooltip = itemProperty.typeName
            preferredSize = (150, 21)
            horizontalAlignment = Alignment.Right
          }, new Constraints {
            grid = (0, 0)
            anchor = GridBagPanel.Anchor.East
            insets = new Insets(1, 3, 0, 5)
          })
          add(new TextField(itemProperty.name) {
            tooltip = LocaleManager.getString("name")
            preferredSize = (150, 21)
            reactions += {
              case ValueChanged(textField) => {
                itemProperty.name = text
                presenterPanel.reload(itemTypeComponent.getSelectedItem)
              }
            }
          }, new Constraints {
            grid = (1, 0)
            anchor = GridBagPanel.Anchor.West
            insets = new Insets(1, 3, 0, 5)
          })
          add(new Button(new Action(null) {
            icon = IconFactory.getInstance.getIcon("arrowUp")
            enabled = itemType.itemProperties.indexOf(itemProperty) > 0

            def apply() = {
              val index: Int = itemType.itemProperties.indexOf(itemProperty)
              itemType.itemProperties -= itemProperty
              itemType.itemProperties.insert(index - 1, itemProperty)
              reloadItemType
            }
          }) {
            tooltip = LocaleManager.getString("moveUp")
            focusable = false
            preferredSize = (21, 21)
          }, new Constraints {
            grid = (2, 0)
            insets = new Insets(1, 3, 0, 0)
          })
          add(new Button(new Action(null) {
            icon = IconFactory.getInstance.getIcon("arrowDown")
            enabled = itemType.itemProperties.last != itemProperty

            def apply() = {
              val index: Int = itemType.itemProperties.indexOf(itemProperty)
              itemType.itemProperties -= itemProperty
              itemType.itemProperties.insert(index + 1, itemProperty)
              reloadItemType
            }
          }) {
            tooltip = LocaleManager.getString("moveDown")
            focusable = false
            preferredSize = (21, 21)
          }, new Constraints {
            grid = (3, 0)
            insets = new Insets(1, 1, 0, 0)
          })
          add(new Button(new Action(null) {
            icon = IconFactory.getInstance.getIcon("minus")

            def apply() = {
              itemType.itemProperties -= itemProperty
              reloadItemType
            }
          }) {
            tooltip = LocaleManager.getString("delete")
            focusable = false
            preferredSize = (21, 21)
          }, new Constraints {
            grid = (4, 0)
            insets = new Insets(1, 3, 0, 0)
          })
          add(wrap(new JSpinner(new SpinnerNumberModel(1, 1, 10, 1)) {
            setToolTipText(LocaleManager.getString("column"))
            setValue(itemProperty.column + 1)
            addChangeListener(Swing.ChangeListener(_ => {
              itemProperty.column = getValue.asInstanceOf[Number].intValue - 1
              reloadItemType
            }))
          }), new Constraints {
            grid = (5, 0)
            insets = new Insets(1, 3, 0, 0)
          })
          add(wrap(new JSpinner(new SpinnerNumberModel(1, 1, 10, 1)) {
            setToolTipText(LocaleManager.getString("rowCount"))
            setValue(itemProperty.rows)
            addChangeListener(Swing.ChangeListener(_ => {
              itemProperty.rows = getValue.asInstanceOf[Number].intValue
              reloadItemType
            }))
          }), new Constraints {
            grid = (6, 0)
            insets = new Insets(1, 3, 0, 0)
          })
          add(new CheckBox {
            tooltip = LocaleManager.getString("showInTable")
            selected = itemProperty.tableColumn
            reactions += {
              case ButtonClicked(_) => {
                itemProperty.tableColumn = selected
                reloadItemType
              }
            }
          }, new Constraints {
            grid = (7, 0)
            insets = new Insets(1, 3, 0, 0)
          })

          add(new FlowPanel(FlowPanel.Alignment.Left)(Functions.transform(presenter.getEditorActions(itemProperty, reloadItemType),
            (item: Action) => new Button(item)): _*) {
            vGap = 0
            hGap = 0
          },
            new Constraints {
              grid = (8, 0)
              insets = new Insets(1, 3, 0, 0)
            })
          add(new FlowPanel, new Constraints {
            grid = (9, 0)
            weightx = 1.0
          })
        }
      }
    }
  }

}
