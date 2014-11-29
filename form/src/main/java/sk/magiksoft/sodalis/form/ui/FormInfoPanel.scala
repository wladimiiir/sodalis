package sk.magiksoft.sodalis.form.ui

import java.awt.{GridBagConstraints, GridBagLayout}
import java.text.DecimalFormat
import sk.magiksoft.sodalis.form.entity.{Format, Form}
import sk.magiksoft.sodalis.form.util.DocumentUtils
import javax.swing.{JComponent, JPanel}
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel
import scala.swing._
import scala.Some
import sk.magiksoft.sodalis.core.locale.LocaleManager
import scala.swing.event.{ValueChanged, SelectionChanged}
import org.jhotdraw.draw.AttributeKeys
import Swing._
import sk.magiksoft.sodalis.core.utils.Conversions._

/**
 * @author wladimiiir
 * @since 2010/8/6
 *
 */

class FormInfoPanel extends AbstractInfoPanel(classOf[Form]) {
  private var form: Option[Form] = None

  private val nameUI = new TextField
  private val formatUI = new ComboBox(Format.values.toSeq)
  private val widthUI = new FormattedTextField(new DecimalFormat("0.00"))
  private val heightUI = new FormattedTextField(new DecimalFormat("0.00"))
  private val descriptionUI = new TextArea(5, 10)

  private var adjusting = false

  def createLayout = {
    val layoutPanel = new JPanel(new GridBagLayout)
    val c = new GridBagConstraints

    c.gridx = 0
    c.gridy = 0
    c.insets = new Insets(5, 5, 0, 0)
    c.anchor = GridBagConstraints.EAST
    c.fill = GridBagConstraints.NONE
    layoutPanel.add(new Label(LocaleManager.getString("name")), c)

    c.gridx += 1
    c.insets = new Insets(5, 3, 0, 5);
    c.anchor = GridBagConstraints.WEST
    c.fill = GridBagConstraints.HORIZONTAL
    layoutPanel.add(nameUI, c)

    c.gridx = 0
    c.gridy += 1
    c.insets = new Insets(3, 5, 0, 0)
    c.anchor = GridBagConstraints.EAST
    c.fill = GridBagConstraints.NONE
    layoutPanel.add(new Label(LocaleManager.getString("format")), c)

    c.gridx += 1
    c.insets = new Insets(3, 3, 0, 5);
    c.anchor = GridBagConstraints.WEST
    c.fill = GridBagConstraints.HORIZONTAL
    layoutPanel.add(formatUI, c)

    c.gridx = 0
    c.gridy += 1
    c.insets = new Insets(3, 5, 0, 0)
    c.anchor = GridBagConstraints.EAST
    c.fill = GridBagConstraints.NONE
    layoutPanel.add(new Label(LocaleManager.getString("width")), c)

    c.gridx += 1
    c.insets = new Insets(3, 3, 0, 5);
    c.anchor = GridBagConstraints.WEST
    c.fill = GridBagConstraints.HORIZONTAL
    layoutPanel.add(widthUI, c)

    c.gridx = 0
    c.gridy += 1
    c.insets = new Insets(3, 5, 0, 0)
    c.anchor = GridBagConstraints.EAST
    c.fill = GridBagConstraints.NONE
    layoutPanel.add(new Label(LocaleManager.getString("height")), c)

    c.gridx += 1
    c.insets = new Insets(3, 3, 0, 5);
    c.anchor = GridBagConstraints.WEST
    c.fill = GridBagConstraints.HORIZONTAL
    layoutPanel.add(heightUI, c)

    c.gridy += 1
    c.gridx = 0
    c.fill = GridBagConstraints.NONE
    c.insets = new Insets(3, 5, 5, 0)
    c.weightx = 0
    c.anchor = GridBagConstraints.NORTHEAST
    layoutPanel.add(new Label(LocaleManager.getString("description")), c)
    c.gridx += 1
    c.insets = new Insets(3, 3, 5, 5);
    c.anchor = GridBagConstraints.WEST
    c.fill = GridBagConstraints.HORIZONTAL
    layoutPanel.add(new ScrollPane(descriptionUI), c)

    nameUI.preferredSize = (200, 21)

    nameUI.peer.getDocument.addDocumentListener(documentListener)
    descriptionUI.peer.getDocument.addDocumentListener(documentListener)
    formatUI.selection.reactions += {
      case SelectionChanged(_) => formatUI.selection.item match {
        case Format.Custom =>
        case f: Format.Format => {
          if (!adjusting) {
            adjusting = true
            widthUI.peer.setValue(Format.getWidthMM(f) / 10)
            heightUI.peer.setValue(Format.getHeightMM(f) / 10)
            adjusting = false
          }
        }
          fireEditing
      }
    }
    formatUI.selection.item = Format.A4
    widthUI.reactions += {
      case ValueChanged(_) => {
        if (!adjusting) {
          adjusting = true
          formatUI.selection.item = findFormat
          adjusting = false
          fireEditing
        }
      }
    }
    heightUI.reactions += {
      case ValueChanged(_) => {
        if (!adjusting) {
          adjusting = true
          formatUI.selection.item = findFormat
          adjusting = false
          fireEditing
        }
      }
    }
    layoutPanel
  }

  def findFormat = {
    try {
      val width = widthUI.text.replaceAll(",", ".").toDouble * 10
      val height = heightUI.text.replaceAll(",", ".").toDouble * 10
      val format = Format.values.find(f => Format.getWidthMM(f) == width && Format.getHeightMM(f) == height)

      format match {
        case Some(value) => value
        case None => Format.Custom
      }
    } catch {
      case e: NumberFormatException => Format.Custom
    }

  }

  def initData = initialized match {
    case true =>
    case false => form match {
      case None =>
      case Some(form) => {
        nameUI.text = form.name
        descriptionUI.text = form.description
        formatUI.selection.item = form.format
        if (form.format == Format.Custom) {
          adjusting = true
          widthUI.peer.setValue(form.pageWidthMM / 10)
          heightUI.peer.setValue(form.pageHeightMM / 10)
          adjusting = false
        }
        initialized = true
      }
    }
  }

  def setupPanel(form: Any) = form match {
    case form: Form => {
      this.form = Option(form)
      initialized = false
    }
    case _ =>
  }

  def setupObject(form: Any) = form match {
    case form: Form => {
      form.name = nameUI.text
      form.description = descriptionUI.text
      form.format = formatUI.selection.item
      form.pageWidthMM = widthUI.peer.getValue.asInstanceOf[Number].doubleValue * 10d
      form.pageHeightMM = heightUI.peer.getValue.asInstanceOf[Number].doubleValue * 10d
      for (page <- form.pages) {
        page.set(AttributeKeys.CANVAS_WIDTH, new java.lang.Double(DocumentUtils.toPix(form.pageWidthMM)))
        page.set(AttributeKeys.CANVAS_HEIGHT, new java.lang.Double(DocumentUtils.toPix(form.pageHeightMM)))
      }
    }
    case _ =>
  }

  def getPanelName = LocaleManager.getString("document")
}
