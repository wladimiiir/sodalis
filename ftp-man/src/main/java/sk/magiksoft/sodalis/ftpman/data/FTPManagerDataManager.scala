package sk.magiksoft.sodalis.ftpman.data

import java.io.{FileOutputStream, File}
import sk.magiksoft.sodalis.ftpman.action._
import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager
import scala.swing.Publisher
import sk.magiksoft.sodalis.core.event.ActionCancelled
import sk.magiksoft.sodalis.core.logger.LoggerManager
import org.apache.commons.net.ftp.{FTPFileFilter, FTP, FTPFile, FTPClient}

/**
 * @author wladimiiir
 * @since 2011/5/6
 */

object FTPManagerDataManager extends ClientDataManager {
  def scanHost(host: String, publisher: Publisher) {
    val ftp = new FTPClient

    ftp.setConnectTimeout(2000)
    ftp.connect(host)
    try {
      if (!ftp.login("anonymous", "")) {
        return
      }
      ftp.enterLocalPassiveMode()
      publisher.reactions += {
        case ActionCancelled() => {
          ftp.disconnect()
          publisher.publish(new ScanCancelled)
        }
      }
      publisher.publish(new ScanStarted)
      scanFiles("", ftp.listFiles("/"))
      publisher.publish(new ScanFinished)
    } finally {
      ftp.disconnect()
    }

    def scanFiles(currentPath: String, files: Array[FTPFile]) {
      for (dir <- files if dir.isDirectory && !dir.getName.isEmpty) {
        val childPath = currentPath + '/' + dir.getName
        LoggerManager.getInstance().debug(getClass, "Scanning next ftp directory: " + childPath)
        scanFiles(childPath, ftp.listFiles(childPath))
      }
      for (file <- files if file.isFile) {
        publisher.publish(new FileScanned(file.getName, host, currentPath, file.getSize))
      }
    }
  }

  def retrieveFile(host: String, fileName: String, path: String, localFile: File, publisher: Publisher) {
    val ftp = new FTPClient

    ftp.setConnectTimeout(2000)
    ftp.connect(host)
    try {
      if (!ftp.login("anonymous", "")) {
        false
      }

      ftp.enterLocalPassiveMode()
      val filesize = getFile match {
        case Some(file) => file.getSize
        case None => -1
      }
      if (filesize == -1) {
        false
      }
      val output = new FileOutputStream(localFile)
      ftp.setFileType(FTP.BINARY_FILE_TYPE)
      try {
        val input = ftp.retrieveFileStream(path + '/' + fileName)
        val buffer = new Array[Byte](1024)
        var cancelled = false

        publisher.reactions += {
          case ActionCancelled() => cancelled = true
        }

        write(0)
        input.close()
        ftp.completePendingCommand()

        def write(retrieved: Long) {
          input.read(buffer) match {
            case read: Int if read <= 0 || cancelled =>
            case read: Int => {
              output.write(buffer, 0, read)
              publisher.publish(new FilePartRetrieved(retrieved + read, filesize))
              write(retrieved + read)
            }
          }
        }
      } finally {
        output.close()
      }
    } finally {
      ftp.disconnect()
    }

    def getFile = {
      ftp.listFiles(path, new FTPFileFilter {
        def accept(file: FTPFile) = file.getName == fileName
      }).headOption
    }
  }


}
