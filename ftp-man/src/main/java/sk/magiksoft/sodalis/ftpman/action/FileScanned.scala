package sk.magiksoft.sodalis.ftpman.action

import scala.swing.event.Event


/**
 * @author wladimiiir
 * @since 2011/5/6
 */

case class FileScanned(fileName: String, host: String, path: String, fileSize: Long) extends Event
