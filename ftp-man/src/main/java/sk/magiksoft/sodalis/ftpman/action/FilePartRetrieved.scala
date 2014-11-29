package sk.magiksoft.sodalis.ftpman.action

import scala.swing.event.Event


/**
 * @author wladimiiir
 * @since 2011/5/8
 */

case class FilePartRetrieved(retrievedSize: Long, totalSize: Long) extends Event
