package sk.magiksoft.sodalis.ftpman

import action.{ScanCancelled, FileScanned, ScanFinished, ScanStarted}
import data.FTPManagerDataManager
import entity.ScanInfo.State
import entity.{FTPEntry, ScanInfo, FTPScanCriteria}
import ui.{ScanInfoDialog, FTPEntryContext}
import java.net.{SocketTimeoutException, ConnectException}
import scala.swing.{Publisher, Swing}
import sk.magiksoft.sodalis.core.logger.LoggerManager
import sk.magiksoft.sodalis.core.context.AbstractContextManager
import sk.magiksoft.sodalis.core.event.ActionCancelled
import org.apache.commons.net.ftp.FTPConnectionClosedException
import scala.collection.JavaConversions._
import akka.actor.{Props, ActorSystem, Actor}

/**
 * @author wladimiiir
 * @since 2011/5/6
 */

object FTPManager extends AbstractContextManager {
  private val actorSystem = ActorSystem("ftpActorSystem")

  def getDataManager = FTPManagerDataManager

  def getDefaultQuery = "from FTPEntry e"

  def createContext() = new FTPEntryContext

  def isFullTextActive = true

  def performScan(criteria: FTPScanCriteria) {
    val dialog = new ScanInfoDialog
    val scanActor = actorSystem.actorOf(Props(classOf[FTPScanActor], dialog))
    //TODO: bound the size of mailbox

    dialog.setCloseOnCancel(false)
    dialog.getOkButton.setEnabled(false)
    dialog.getCancelButton.addActionListener(Swing.ActionListener {
      e => {
        actorSystem.stop(scanActor)
        dialog.model.getObjects.filter(info => info.state == State.Scanning || info.state == State.Nothing).foreach {
          _.state = State.Cancelled
        }
        dialog.model.fireTableDataChanged()
      }
    })
    criteria.hostList.foreach(scanActor ! _)
    dialog.setVisible(true)
  }
}

class FTPScanActor(private val dialog: ScanInfoDialog) extends Actor {
  override def receive = {
    case host: String => scan(host)
    case _ => {
      if (!dialog.model.getObjects.exists(info => info.state == State.Scanning || info.state == State.Nothing)) {
        dialog.getOkButton.setEnabled(true)
      }
    }
  }

  private def scan(host: String) {
    val scanInfo = new ScanInfo
    val publisher = new Publisher {}
    val actionListener = Swing.ActionListener {
      e => publisher.publish(new ActionCancelled)
    }
    dialog.getCancelButton.addActionListener(actionListener)
    scanInfo.host = host
    dialog.model.addObject(scanInfo)
    publisher.reactions += {
      case ScanStarted() => {
        scanInfo.state = State.Scanning
        dialog.model.fireTableDataChanged()
      }
      case FileScanned(fileName, host, path, size) => {
        val entry = new FTPEntry
        entry.fileName = fileName
        entry.host = host
        entry.path = path
        entry.fileSize = size
        scanInfo.entries += entry
        dialog.model.fireTableDataChanged()
      }
      case ScanFinished() => {
        scanInfo.state = State.Done
        dialog.model.fireTableDataChanged()
      }
      case ScanCancelled() => {
        scanInfo.state = State.Cancelled
        dialog.model.fireTableDataChanged()
      }
    }

    try {
      FTPManagerDataManager.scanHost(host, publisher)
    } catch {
      case e: Exception if e.isInstanceOf[ConnectException] || e.isInstanceOf[SocketTimeoutException] => {
        scanInfo.state = State.ConnectionFailed
        dialog.model.fireTableDataChanged()
      }
      case e: FTPConnectionClosedException => {
        scanInfo.state = State.Failed
        dialog.model.fireTableDataChanged()
      }
      case e: Exception => {
        LoggerManager.getInstance().error(FTPManager.getClass, e)
      }
    } finally {
      dialog.getCancelButton.removeActionListener(actionListener)
    }
  }
}

