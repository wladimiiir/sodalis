
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.form.action

import java.util.List
import java.awt.event.ActionEvent
import sk.magiksoft.sodalis.core.action.{ActionMessage, MessageAction}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.ui.OkCancelDialog
import sk.magiksoft.sodalis.form.ui.FormInfoPanel
import sk.magiksoft.sodalis.form.entity.Form
import sk.magiksoft.sodalis.core.SodalisApplication
import swing.Swing
import sk.magiksoft.sodalis.form.FormDataManager
import sk.magiksoft.sodalis.core.factory.{IconFactory, EntityFactory}
import sk.magiksoft.sodalis.core.utils.UIUtils

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Aug 6, 2010
 * Time: 9:46:20 AM
 * To change this template use File | Settings | File Templates.
 */

class AddFormAction extends MessageAction(null, IconFactory.getInstance.getIcon("add")) {
  private var formDialog: Option[OkCancelDialog] = None
  private var formInfoPanel: Option[FormInfoPanel] = None

  def getActionMessage(objects: List[_]) = new ActionMessage(true, LocaleManager.getString("addForm"))

  def actionPerformed(e: ActionEvent) = {
    val form = EntityFactory.getInstance.createEntity(classOf[Form])

    formInfoPanel match {
      case Some(infoPanel) => {
        infoPanel.initLayout
        infoPanel.setupPanel(form)
        infoPanel.initData
      }
      case None => {
        val dialog: OkCancelDialog = new OkCancelDialog(SodalisApplication.get.getMainFrame)
        val infoPanel: FormInfoPanel = new FormInfoPanel

        dialog.setMainPanel(infoPanel)
        dialog.setSize(400, 300)
        dialog.setModal(true)
        dialog.setTitle(LocaleManager.getString("addForm"))
        dialog.setLocationRelativeTo(null)
        dialog.getOkButton.addActionListener(Swing.ActionListener(e => {
          infoPanel.setupObject(form)
          FormDataManager.addDatabaseEntity(form)
        }))
        UIUtils.makeISDialog(dialog)

        formInfoPanel = Option(infoPanel)
        formDialog = Option(dialog)
        infoPanel.initLayout
      }
    }

    formDialog.get.setVisible(true)
  }
}