package sk.magiksoft.sodalis.ftpman.action

import javax.swing.{Action, AbstractAction}
import java.awt.event.ActionEvent
import sk.magiksoft.sodalis.ftpman.ui.FTPScanCriteriaPanel
import sk.magiksoft.sodalis.ftpman.FTPManager
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.ui.OkCancelDialog
import sk.magiksoft.sodalis.core.factory.IconFactory
import scala.swing.Swing

/**
 * @author wladimiiir
 * @since 2011/5/6
 */

class ScanFTPServersAction extends AbstractAction("", IconFactory.getInstance().getIcon("scan")) {
  private lazy val criteriaPanel = new FTPScanCriteriaPanel
  private lazy val dialog = new OkCancelDialog(LocaleManager.getString("scanCriteria")) {
    setMainPanel(criteriaPanel.peer)
    setSize(320, 240)
    setLocationRelativeTo(null)
    getOkButton.addActionListener(Swing.ActionListener {
      e => {
        setVisible(false)
        FTPManager.performScan(criteriaPanel.createCriteria)
      }
    })
  }

  putValue(Action.SHORT_DESCRIPTION, LocaleManager.getString("scanFTPs"))

  def actionPerformed(e: ActionEvent) {
    dialog.setVisible(true)
  }
}
