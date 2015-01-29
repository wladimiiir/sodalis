package sk.magiksoft.sodalis.core.action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

import sk.magiksoft.sodalis.core.data.DBManagerProvider
import sk.magiksoft.sodalis.core.module.DatabaseModuleManager
import sk.magiksoft.sodalis.core.{SodalisManager, SodalisApplication}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.module.ui.ModuleConfigurationDialog

/**
 * @author Y12370
 * @since  2015/01/20
 */
class ShowModuleConfigDialog extends AbstractAction(LocaleManager.getString("moduleConfiguration")) {
  private lazy val dialog = new ModuleConfigurationDialog(SodalisApplication.get().getMainFrame, new DatabaseModuleManager)

  override def actionPerformed(e: ActionEvent): Unit = {
    dialog.setVisible(true)
  }
}
