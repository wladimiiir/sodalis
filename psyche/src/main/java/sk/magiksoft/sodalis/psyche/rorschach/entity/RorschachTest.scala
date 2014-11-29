package sk.magiksoft.sodalis.psyche.rorschach.entity

import sk.magiksoft.sodalis.psyche.entity.PsychoTest
import sk.magiksoft.sodalis.core.entity.DatabaseEntity
import sk.magiksoft.sodalis.core.locale.LocaleManager
import scala.beans.BeanProperty

/**
 * @author wladimiiir
 * @since 2011/6/16
 */

class RorschachTest extends PsychoTest {
  @BeanProperty var testResult = new TestResult

  name = LocaleManager.getString("rorschachTest")

  override def updateFrom(entity: DatabaseEntity) {
    super.updateFrom(entity)
    entity match {
      case test: RorschachTest if test ne this => {
        testResult.updateFrom(test.testResult)
      }
      case _ =>
    }
  }
}
