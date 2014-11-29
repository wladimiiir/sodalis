package sk.magiksoft.sodalis.psyche.entity

import sk.magiksoft.sodalis.core.entity.{AbstractDatabaseEntity, DatabaseEntity}
import java.util.Calendar
import sk.magiksoft.sodalis.category.entity.{HistorizableMixin, CategorizedMixin}
import sk.magiksoft.sodalis.person.entity.PersonWrapper
import scala.beans.BeanProperty

/**
 * @author wladimiiir
 * @since 2011/5/13
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
