package sk.magiksoft.sodalis.ftpman.entity

import sk.magiksoft.sodalis.core.entity.{AbstractDatabaseEntity, DatabaseEntity}
import sk.magiksoft.sodalis.core.search.FullText
import sk.magiksoft.sodalis.category.entity.{CategorizedMixin, Categorized}
import scala.beans.BeanProperty

/**
 * @author wladimiiir
 * @since 2011/5/6
 */

class FTPEntry extends AbstractDatabaseEntity with CategorizedMixin {
  @FullText
  @BeanProperty var fileName = ""
  @FullText
  @BeanProperty var path = ""
  @FullText
  @BeanProperty var host = ""
  @BeanProperty var fileSize = 0l

  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case entry: FTPEntry if entry ne this => {
        fileName = entry.fileName
        path = entry.path
        host = entry.host
        fileSize = entry.fileSize
      }
      case _ =>
    }
  }
}
