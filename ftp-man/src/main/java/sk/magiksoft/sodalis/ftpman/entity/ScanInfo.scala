package sk.magiksoft.sodalis.ftpman.entity

import scala.collection.mutable.ListBuffer
import sk.magiksoft.sodalis.core.locale.LocaleManager


/**
 * @author wladimiiir
 * @since 2011/5/6
 */

class ScanInfo {
  var host = ""
  var entries = new ListBuffer[FTPEntry]
  var state = ScanInfo.State.Nothing
}

object ScanInfo {

  object State extends Enumeration {
    type State = Value
    val Nothing, Scanning, ConnectionFailed, Failed, Done, Cancelled = Value

    def localize(state: State) = state match {
      case Nothing => " "
      case Scanning => LocaleManager.getString("scanning")
      case ConnectionFailed => LocaleManager.getString("connectionFailed")
      case Failed => LocaleManager.getString("failed")
      case Done => LocaleManager.getString("done")
      case Cancelled => LocaleManager.getString("cancelled")
    }
  }

}
