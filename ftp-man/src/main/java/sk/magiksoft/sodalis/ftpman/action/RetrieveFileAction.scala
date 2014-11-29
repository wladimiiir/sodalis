package sk.magiksoft.sodalis.ftpman.action

import sk.magiksoft.sodalis.ftpman.entity.FTPEntry
import java.io.File
import sk.magiksoft.sodalis.ftpman.data.FTPManagerDataManager
import java.net.ConnectException
import javax.swing._
import javax.swing.SwingWorker
import java.awt.{Color, BorderLayout}
import scala.swing._
import sk.magiksoft.sodalis.core.action.EntityAction
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.ui.ISOptionPane
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.event.ActionCancelled

/**
 * @author wladimiiir
 * @since 2011/5/7
 */

class RetrieveFileAction extends EntityAction[FTPEntry] {
  private lazy val fileChooser = new FileChooser

  def apply(entities: List[FTPEntry]) {
    var showDialog = true
    entities foreach {
      entry => {
        fileChooser.selectedFile = if (fileChooser.selectedFile ne null) {
          new File(fileChooser.selectedFile.getParentFile, entry.fileName)
        } else {
          new File(entry.fileName)
        }
        if (showDialog) {
          fileChooser.showSaveDialog(Component.wrap(SodalisApplication.get().getMainFrame.getContentPane.asInstanceOf[JComponent])) match {
            case FileChooser.Result.Approve => {
              if (entities.size > 1 && (entry eq entities.head)
                && ISOptionPane.showConfirmDialog(SodalisApplication.get().getMainFrame, LocaleManager.getString("useDirectoryForOthers"),
                LocaleManager.getString("downloadFiles"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                showDialog = false
              }
              actionApproved()
            }
            case _ =>
          }
        } else {
          actionApproved()
        }

        def actionApproved() {
          val publisher = new Publisher {}
          val progressBar = new ProgressBar
          val cancelButton = Button(LocaleManager.getString("cancelAction")) {
            publisher.publish(new ActionCancelled)
          }
          val progressDialog = new JDialog(SodalisApplication.get().getMainFrame) {
            setModal(true)
            setUndecorated(true)
            setLayout(new BorderLayout)
            add(new Label(entry.fileName) {
              border = BorderFactory.createMatteBorder(1, 1, 0, 1, Color.GRAY)
            }.peer, BorderLayout.NORTH)
            add(progressBar.peer, BorderLayout.CENTER)
            add(cancelButton.peer, BorderLayout.SOUTH)
            setSize(200, 58)
            setLocationRelativeTo(null)
          }
          progressBar.labelPainted = true
          publisher.reactions += {
            case FilePartRetrieved(retrieved, total) => {
              progressBar.value = retrieved.toInt
              progressBar.max = total.toInt
            }
          }
          new SwingWorker[Void, Void] {
            def doInBackground() = {
              try {
                FTPManagerDataManager.retrieveFile(entry.host, entry.fileName, entry.path, fileChooser.selectedFile, publisher)
              } catch {
                case e: ConnectException => ISOptionPane.showMessageDialog(LocaleManager.getString("connectionFailed"))
                case e: Exception => ISOptionPane.showMessageDialog(LocaleManager.getString("connectionError"))
              }
              null
            }

            override def done() {
              progressDialog.setVisible(false)
            }
          }.execute()
          progressDialog.setVisible(true)
        }
      }
    }
  }

  def getName(entities: List[FTPEntry]) = entities.size match {
    case 1 => LocaleManager.getString("downloadFile")
    case _ => LocaleManager.getString("downloadFiles")
  }

  def isAllowed(entities: List[FTPEntry]) = !entities.isEmpty
}
