/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.entity

import sk.magiksoft.sodalis.psyche.rorschach.ui.TableSigningDialog
import sk.magiksoft.sodalis.psyche.entity.{PsychoTest, PsychoTestCreator}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.utils.UIUtils

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 6/24/11
 * Time: 11:04 AM
 * To change this template use File | Settings | File Templates.
 */

class RorschachTestCreator extends PsychoTestCreator {
  private lazy val dialog = new TableSigningDialog

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