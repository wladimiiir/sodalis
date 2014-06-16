
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.form.ui

import action.{PageRemoved, PageEdited, PageAdded}
import sk.magiksoft.sodalis.form.entity.Form
import java.awt.BorderLayout
import sk.magiksoft.sodalis.form.util.DocumentUtils
import javax.swing.JPanel

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Aug 5, 2010
 * Time: 3:44:07 PM
 * To change this template use File | Settings | File Templates.
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