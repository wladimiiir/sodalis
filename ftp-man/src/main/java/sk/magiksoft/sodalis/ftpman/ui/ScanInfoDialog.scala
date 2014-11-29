package sk.magiksoft.sodalis.ftpman.ui

import sk.magiksoft.sodalis.ftpman.entity.ScanInfo.State
import sk.magiksoft.sodalis.ftpman.data.FTPManagerDataManager
import java.util.List
import sk.magiksoft.sodalis.ftpman.entity.{FTPEntry, ScanInfo}
import sk.magiksoft.sodalis.ftpman.FTPManager
import javax.swing.{JDialog, SwingWorker}
import java.awt.BorderLayout
import javax.swing.event.{TableModelEvent, TableModelListener}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.ui.OkCancelDialog
import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.swing.ISTable
import scala.swing._
import sk.magiksoft.sodalis.core.SodalisApplication
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2011/5/6
 */

class ScanInfoDialog extends OkCancelDialog(LocaleManager.getString("scanning")) {
  val model: ObjectTableModel[ScanInfo] = new ScanInfoTableModel
  private val table = new ISTable(model)

  table.getModel.addTableModelListener(new TableModelListener {
    def tableChanged(e: TableModelEvent) {
      if (e.getType == TableModelEvent.INSERT) {
        table.scrollRectToVisible(table.getCellRect(e.getLastRow, 0, true))
      }
    }
  })
  setMainPanel(createMainPanel.peer)
  setSize(400, 300)
  setLocationRelativeTo(null)
  getOkButton.addActionListener(Swing.ActionListener {
    e => {
      val progressBar = new ProgressBar
      val progressDialog = new JDialog(SodalisApplication.get().getMainFrame) {
        setUndecorated(true)
        setLayout(new BorderLayout)
        add(progressBar.peer, BorderLayout.CENTER)
        setSize(200, 21)
        setLocationRelativeTo(null)
      }
      val entries = new ListBuffer[FTPEntry]

      model.getObjects.foreach {
        entries ++= _.entries
      }
      progressBar.labelPainted = true
      progressBar.label = "0/" + entries.size
      progressBar.max = entries.size
      progressDialog.setVisible(true)
      new SwingWorker[Void, Int]() {
        def doInBackground() = {
          val hosts = new ListBuffer[String]
          var index = 0
          for (entry <- entries) {
            if (!hosts.contains(entry.host)) {
              FTPManagerDataManager.executeHQLQuery("delete from FTPEntry where host='" + entry.host + "'")
              FTPManager.entitiesRemoved(
                FTPManager.getContext.getEntities.map {
                  _.asInstanceOf[FTPEntry]
                }.filter {
                  _.host == entry.host
                }
              )
              hosts += entry.host
            }
            index += 1
            FTPManagerDataManager.addDatabaseEntity(entry)
            publish(index)
          }
          null
        }

        override def process(chunks: List[Int]) {
          progressBar.value = chunks.get(0)
          progressBar.label = chunks.get(0) + "/" + entries.size
        }

        override def done() {
          setVisible(false)
          progressDialog.setVisible(false)
        }
      }.execute()
    }
  })

  private def createMainPanel = {
    val panel = new BorderPanel {
      add(new ScrollPane(Component.wrap(table)), BorderPanel.Position.Center)
    }

    panel
  }

  private class ScanInfoTableModel extends ObjectTableModel[ScanInfo](Array(
    LocaleManager.getString("host"),
    LocaleManager.getString("filesCount"),
    LocaleManager.getString("state")
  )) {
    def getValueAt(rowIndex: Int, columnIndex: Int) = {
      val info = getObject(rowIndex)
      columnIndex match {
        case 0 => info.host
        case 1 => info.entries.size.toString()
        case 2 => State.localize(info.state)
      }
    }
  }

}
