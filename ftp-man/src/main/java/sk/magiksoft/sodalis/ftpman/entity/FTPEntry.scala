/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.ftpman.entity

import sk.magiksoft.sodalis.core.entity.DatabaseEntity
import sk.magiksoft.sodalis.core.search.FullText
import sk.magiksoft.sodalis.category.entity.Categorized

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/6/11
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
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