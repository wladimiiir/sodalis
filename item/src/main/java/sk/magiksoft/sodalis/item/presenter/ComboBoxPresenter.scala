package sk.magiksoft.sodalis.item.presenter

import sk.magiksoft.sodalis.item.presenter.model.ComboBoxPresenterModel
import sk.magiksoft.sodalis.item.entity.ItemProperty
import java.io.Serializable
import sk.magiksoft.sodalis.core.enumeration.{EnumerationFactory, EnumerationEntry, Enumeration}
import javax.swing.{JComboBox, DefaultComboBoxModel, JDialog, BorderFactory}
import scala.swing._
import sk.magiksoft.sodalis.core.data.ComboBoxDataManager
import scala.swing.event.SelectionChanged
import scala.swing.ListView.Renderer
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.utils.UIUtils
import sk.magiksoft.sodalis.core.ui.OkCancelDialog
import scala.swing.event.SelectionChanged
import scala.collection.JavaConversions._
import Component._

/**
 * @author wladimiiir
 * @since 2010/6/18
 */

class ComboBoxPresenter extends Presenter {
  private val CUSTOM_MODEL = "CUSTOM_MODEL"

  def getValue(component: Component) = component.asInstanceOf[ComboBox[Any]].selection.item.asInstanceOf[Serializable]

  def getReadableValue(value: Serializable) = value match {
    case s: String => s
    case e: EnumerationEntry => e.getText
    case _ => null
  }

  def getComponent(itemProperty: ItemProperty, value: Serializable) = {
    val comboBox = new ComboBox[Any](List(None))

    //TODO: bug in scala compiler does not allow it to compile
//    peer.setModel(new DefaultComboBoxModel[Any])
    itemProperty.model match {
      case model: ComboBoxPresenterModel => {
        if (model.enumerationKey != null) {
//          ComboBoxDataManager.getInstance.registerComboBox(model.enumerationKey, comboBox.peer)
        } else {
          for (item <- model.items) {
//            comboBox.peer.addItem(item)
          }
        }
      }
      case _ => null
    }
    comboBox
  }

  def getEditorActions(itemProperty: ItemProperty, reloadAction: => Unit) = List(new ItemDialogAction(itemProperty, reloadAction))

  def setValue(c: Component, value: Serializable) = c.asInstanceOf[ComboBox[Any]].selection.item = value

  def addName = true

  def addChangeListener(component: Component, listener: () => Unit) = {
    component.asInstanceOf[ComboBox[_]].selection.reactions += {
      case SelectionChanged(_) => listener()
    }
  }

  class ItemDialogAction(itemProperty: ItemProperty, reloadAction: => Unit) extends Action(LocaleManager.getString("comboBoxItems")) {
    val CUSTOM_ITEM = new Enumeration("custom")
    var dialog: JDialog = null
    var enumerations: ComboBox[Enumeration] = null
    var itemsTextArea: TextArea = null

    def apply() = {
      if (dialog == null) {
        dialog = createDialog
      }
      dialog.setVisible(true)
    }

    private def createDialog = {
      val items = EnumerationFactory.getInstance.getEnumerations
      items.add(0, CUSTOM_ITEM)
      enumerations = new ComboBox[Enumeration](items) {
        renderer = Renderer(e => LocaleManager.getString(e.getName))
        selection.reactions += {
          case SelectionChanged(_) => {
            reloadItems
          }
        }
      }
      itemsTextArea = new TextArea
      loadFromItemProperty

      UIUtils.makeISDialog(new OkCancelDialog {
        setMainPanel(new BorderPanel {
          add(new BorderPanel {
            add(enumerations, BorderPanel.Position.Center)
            border = BorderFactory.createTitledBorder(LocaleManager.getString("enumerations"))
          }, BorderPanel.Position.North)
          add(new BorderPanel {
            add(new ScrollPane(itemsTextArea), BorderPanel.Position.Center)
            border = BorderFactory.createTitledBorder(LocaleManager.getString("comboBoxItems"))
          }, BorderPanel.Position.Center)
        }.peer)

        getOkButton.addActionListener(Swing.ActionListener(e => {
          itemProperty.model = new ComboBoxPresenterModel {
            enumerations.selection.item match {
              case CUSTOM_ITEM => items = itemsTextArea.text.split("\n").toList
              case e: Enumeration => enumerationKey = e.getName
            }
          }
          reloadAction
        }))
        setTitle(LocaleManager.getString("comboBoxItems"))
        setSize(250, 300)
        setLocationRelativeTo(null)
      })
    }

    private def loadFromItemProperty: Unit = {
      itemProperty.model match {
        case model: ComboBoxPresenterModel => {
          if (model.enumerationKey != null) {
            enumerations.selection.item = EnumerationFactory.getInstance.getEnumeration(model.enumerationKey)
          } else {
            enumerations.selection.item = CUSTOM_ITEM
            itemsTextArea.text = model.items.mkString("\n")
          }
        }
        case _ => reloadItems
      }
    }

    private def reloadItems = {
      enumerations.selection.item match {
        case CUSTOM_ITEM => {
          itemsTextArea.enabled = true
          itemsTextArea.text = null
        }
        case e: Enumeration => {
          itemsTextArea.enabled = false
          itemsTextArea.text = e.getEntries.map(ee => ee.getText).mkString("\n")
        }
      }
    }
  }

}
