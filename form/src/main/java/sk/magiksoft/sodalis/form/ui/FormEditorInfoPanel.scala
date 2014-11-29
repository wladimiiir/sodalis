package sk.magiksoft.sodalis.form.ui

import action.{PageRemoved, PageEdited, PageAdded}
import sk.magiksoft.sodalis.form.entity.Form
import java.awt.BorderLayout
import sk.magiksoft.sodalis.form.util.DocumentUtils
import javax.swing.JPanel
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel
import sk.magiksoft.sodalis.core.utils.Conversions._
import scala.swing.Swing._

/**
 * @author wladimiiir
 * @since 2010/8/5
 */

class FormEditorInfoPanel extends AbstractInfoPanel(classOf[Form]) {
  private var form: Option[Form] = None
  private val formEditor = new FormEditor

  def createLayout = {
    val layoutPanel = new JPanel(new BorderLayout)
    layoutPanel.add(formEditor)
    formEditor.setPreferredSize((100, 100))
    formEditor.reactions += {
      case PageAdded(_) => fireEditing
      case PageEdited(_) => fireEditing
      case PageRemoved(_) => fireEditing
    }
    layoutPanel
  }

  def initData = initialized match {
    case true =>
    case false => form match {
      case None =>
      case Some(form) => {
        formEditor.setFormDrawings(form.pages.map(fd => fd.clone.asInstanceOf[FormDrawing]).toList)
        formEditor.documentView.canvasSize = (DocumentUtils.toPix(form.pageWidthMM), DocumentUtils.toPix(form.pageHeightMM))
        initialized = true
      }
    }
  }

  def setupPanel(form: Any) = {
    form match {
      case form: Form => this.form = Option(form)
      case _ => this.form = None
    }
    initialized = false
  }

  def setupObject(form: Any) = initialized match {
    case false =>
    case true => form match {
      case form: Form => formEditor.setupForm(form)
      case _ =>
    }
  }

  def getPanelName = LocaleManager.getString("editor")
}
