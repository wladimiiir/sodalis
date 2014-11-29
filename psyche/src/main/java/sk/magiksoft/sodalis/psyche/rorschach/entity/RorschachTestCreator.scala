package sk.magiksoft.sodalis.psyche.rorschach.entity

import sk.magiksoft.sodalis.psyche.rorschach.ui.BlotSigningDialog
import sk.magiksoft.sodalis.psyche.entity.{PsychoTest, PsychoTestCreator}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.utils.UIUtils

/**
 * @author wladimiiir
 * @since 2011/6/24
 */

class RorschachTestCreator extends PsychoTestCreator {
  private lazy val dialog = new BlotSigningDialog

  def createPsychoTest(generalPsychoTest: PsychoTest) = {
    UIUtils.doWithProgress(LocaleManager.getString("initializingUI"), new Runnable {
      override def run(): Unit = dialog.init()
    })

    val rorschachTest = new RorschachTest
    rorschachTest.date = generalPsychoTest.date
    rorschachTest.testedSubject = generalPsychoTest.testedSubject
    dialog.show(rorschachTest)
  }

  def getPsychoTestName = LocaleManager.getString("rorschachTest")
}
