/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.entity

import sk.magiksoft.sodalis.core.entity.DatabaseEntity
import java.util.Calendar
import sk.magiksoft.sodalis.category.entity.CategorizedMixin

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/13/11
 * Time: 11:18 AM
 * To change this template use File | Settings | File Templates.
 */

class PsychoTest extends AbstractDatabaseEntity with CategorizedMixin with HistorizableMixin {
  @BeanProperty var name = ""
  @BeanProperty var date = Calendar.getInstance()
  @BeanProperty var testedSubject = new PersonWrapper()

  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case test: PsychoTest if test ne this => {
        name = test.name
        date = test.date
        testedSubject.updateFrom(test.testedSubject)
      }
      case _ =>
    }
  }


}