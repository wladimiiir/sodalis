package sk.magiksoft.sodalis.ftpman.ui

import sk.magiksoft.sodalis.ftpman.entity.FTPEntry
import java.util.Comparator
import java.text.Collator
import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.swing.ISTable

/**
 * @author wladimiiir
 * @since 2011/5/6
 */

class FTPEntryTableModel extends ObjectTableModel[FTPEntry](Array(
  LocaleManager.getString("fileName"),
  LocaleManager.getString("host"),
  LocaleManager.getString("path"),
  LocaleManager.getString("extension"),
  LocaleManager.getString("fileSize")
), Array(
  ISTable.LEFT_ALIGNMENT_CLASS,
  ISTable.LEFT_ALIGNMENT_CLASS,
  ISTable.LEFT_ALIGNMENT_CLASS,
  ISTable.LEFT_ALIGNMENT_CLASS,
  ISTable.RIGHT_ALIGNMENT_CLASS
)) {

  private lazy val FILENAME_COMPARATOR = new Comparator[FTPEntry] {
    def compare(o1: FTPEntry, o2: FTPEntry) = Collator.getInstance().compare(o1.fileName, o2.fileName)
  }
  private lazy val HOST_COMPARATOR = new Comparator[FTPEntry] {
    def compare(o1: FTPEntry, o2: FTPEntry) = Collator.getInstance().compare(o1.host, o2.host)
  }
  private lazy val PATH_COMPARATOR = new Comparator[FTPEntry] {
    def compare(o1: FTPEntry, o2: FTPEntry) = Collator.getInstance().compare(o1.path, o2.path)
  }
  private lazy val EXTENSION_COMPARATOR = new Comparator[FTPEntry] {
    def compare(o1: FTPEntry, o2: FTPEntry) = o1.fileName.reverse.takeWhile(_ != '.').reverse.compareToIgnoreCase(o2.fileName.reverse.takeWhile(_ != '.').reverse)
  }
  private lazy val FILESIZE_COMPARATOR = new Comparator[FTPEntry] {
    def compare(o1: FTPEntry, o2: FTPEntry) = 0 //o1.fileSize.toLong.compare(o2.fileSize)
  }

  override def getComparator(column: Int): Comparator[_] = column match {
    case 0 => FILENAME_COMPARATOR
    case 1 => HOST_COMPARATOR
    case 2 => PATH_COMPARATOR
    case 3 => EXTENSION_COMPARATOR
    case 4 => FILESIZE_COMPARATOR
    case _ => super.getComparator(column)
  }

  def getValueAt(rowIndex: Int, columnIndex: Int) = {
    val entry = getObject(rowIndex)
    columnIndex match {
      case 0 => entry.fileName
      case 1 => entry.host
      case 2 => entry.path
      case 3 => entry.fileName.reverse.takeWhile(_ != '.').reverse
      case 4 => ObjectTableModel.NUMBER_WITH_SEPARATOR_FORMAT.format(entry.fileSize)
    }
  }
}
