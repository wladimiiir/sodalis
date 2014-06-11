/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.ftpman.entity

import collection.mutable.ListBuffer
import sk.magiksoft.sodalis.core.locale.LocaleManager

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/6/11
 * Time: 4:56 PM
 * To change this template use File | Settings | File Templates.
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