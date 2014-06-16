/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.ftpman

import action.{ScanCancelled, FileScanned, ScanFinished, ScanStarted}
import data.FTPManagerDataManager
import entity.ScanInfo.State
import entity.{FTPEntry, ScanInfo, FTPScanCriteria}
import ui.{ScanInfoDialog, FTPEntryContext}
import concurrent.MailBox
import java.net.{SocketTimeoutException, ConnectException}
import swing.Swing
import sk.magiksoft.sodalis.core.logger.LoggerManager

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/6/11
 * Time: 10:49 AM
 * To change this template use File | Settings | File Templates.
 */

object FTPManager extends AbstractContextManager {
  def getDataManager = FTPManagerDataManager

  def getDefaultQuery = "from FTPEntry e"

  def createContext() = new FTPEntryContext

  def isFullTextActive = true

  def performScan(criteria: FTPScanCriteria) {
    val dialog = new ScanInfoDialog
    val mailBox = new MailBox
    var running = true

    dialog.setCloseOnCancel(false)
    dialog.getOkButton.setEnabled(false)
    dialog.getCancelButton.addActionListener(Swing.ActionListener {
      e => {
        running = false
        if (dialog.model.getObjects.exists(info => info.state == State.Scanning || info.state == State.Nothing)) {
          dialog.model.getObjects.filter(info => info.state == State.Scanning || info.state == State.Nothing).foreach {
            _.state = State.Cancelled
          }
          dialog.model.fireTableDataChanged()
        } else {
          dialog.setVisible(false)
        }
      }
    })
    criteria.hostList.foreach(mailBox.send(_))
    for (thread <- Range(0, criteria.threads)) {
      scanNextHost()
    }
    dialog.setVisible(true)

    def scanNextHost() {
      mailBox.receiveWithin(50) {
        case host: String if running => {
          createScanThread(host).start()
        }
        case _ => {
          if (!dialog.model.getObjects.exists(info => info.state == State.Scanning || info.state == State.Nothing)) {
            dialog.getOkButton.setEnabled(true)
          }
        }
      }
    }

    def createScanThread(host: String) = new Thread(new Runnable {
      def run() {
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
            scanNextHost()
          }
          case ScanCancelled() => {
            scanInfo.state = State.Cancelled
            dialog.model.fireTableDataChanged()
            scanNextHost()
          }
        }
        try {
          FTPManagerDataManager.scanHost(host, publisher)
        } catch {
          case e: Exception if e.isInstanceOf[ConnectException] || e.isInstanceOf[SocketTimeoutException] => {
            scanInfo.state = State.ConnectionFailed
            dialog.model.fireTableDataChanged()
            scanNextHost()
          }
          case e: FTPConnectionClosedException => {
            scanInfo.state = State.Failed
            dialog.model.fireTableDataChanged()
            scanNextHost()
          }
          case e: Exception => {
            LoggerManager.getInstance().error(FTPManager.getClass, e)
          }
        } finally {
          dialog.getCancelButton.removeActionListener(actionListener)
        }

      }
    })
  }
}