package sk.magiksoft.sodalis.core.entity

import property.EntityPropertyTranslatorManager
import sk.magiksoft.sodalis.core.search.FullText
import sk.magiksoft.sodalis.core.security.util.SecurityUtils
import org.dom4j.Element
import sk.magiksoft.sodalis.core.logger.{LogInfoIgnored, LogInfoManager, LoggerManager, LogInfo}
import java.lang.{String, Long}

/**
 * @author wladimiiir
 * @since 2011/2/6
 */
@SerialVersionUID(-2756099904679830161L)
abstract class AbstractDatabaseEntity extends DatabaseEntity with LogInfo with NoteHolder {
  var id: java.lang.Long = null
  var internalID: java.lang.Long = null
  var note: String = null
  var updater: String = null
  @FullText private var readableUpdater: Option[String] = None

  def getInternalID = internalID

  def setInternalID(internalID: Long) {
    this.internalID = internalID
  }

  def getId = id

  def setId(id: Long) {
    this.id = id
  }

  def setNote(note: String) {
    this.note = note
  }

  def getNote = note

  def getUpdater = updater

  def setUpdater(updater: String): Unit = {
    this.updater = updater
    this.readableUpdater = None
  }

  def getReadableUpdater = readableUpdater match {
    case Some(updater) => updater
    case None => {
      readableUpdater = Option(if (updater == null) "" else SecurityUtils.getReadableUser(updater))
      readableUpdater.get
    }
  }

  def isDeleted = false

  def addLogInfoNode(parent: Element) = {
    for (translation <- EntityPropertyTranslatorManager.getTranslator(getClass.asInstanceOf[Class[AbstractDatabaseEntity]]).getTranslations
         if !translation.isInstanceOf[LogInfoIgnored]) {
      translation.getValue(Option(this)) match {
        case Some(value) => LogInfoManager.addValue(parent, translation.name, value)
        case None =>
      }
    }
  }

  override def equals(obj: Any): Boolean = {
    if (obj == null) {
      return false
    }
    if (getClass != obj.asInstanceOf[AnyRef].getClass) {
      return false
    }
    val other: AbstractDatabaseEntity = obj.asInstanceOf[AbstractDatabaseEntity]
    if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
      return false
    }
    if (this.id == null || other.id == null) {
      return super.equals(obj)
    }
    return true
  }

  override def hashCode: Int = {
    var hash: Int = 3
    hash = 83 * hash + (if (this.id != null) {
      this.id.hashCode
    } else {
      0
    })
    return hash
  }

  override def clone: Object = {
    try {
      val cloned = getClass.newInstance.asInstanceOf[DatabaseEntity]
      cloned.setId(this.getId)
      cloned.updateFrom(this)
      return cloned
    } catch {
      case ex: InstantiationException => {
        LoggerManager.getInstance.error(getClass, ex)
      }
      case ex: IllegalAccessException => {
        LoggerManager.getInstance.error(getClass, ex)
      }
    }
    return null
  }

  def clearIDs {
    id = null
  }
}
