/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.entity

import sk.magiksoft.sodalis.psyche.entity.PsychoTest

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 6/16/11
 * Time: 1:28 PM
 * To change this template use File | Settings | File Templates.
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