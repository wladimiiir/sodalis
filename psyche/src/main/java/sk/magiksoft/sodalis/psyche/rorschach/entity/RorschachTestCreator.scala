/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.entity

import sk.magiksoft.sodalis.person.entity.Person
import javax.swing.SwingUtilities
import sk.magiksoft.sodalis.core.utils.UIUtils
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.psyche.rorschach.ui.TableSigningDialog
import sk.magiksoft.sodalis.psyche.entity.{PsychoTest, PsychoTestCreator}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 6/24/11
 * Time: 11:04 AM
 * To change this template use File | Settings | File Templates.
 */

class RorschachTestCreator extends PsychoTestCreator {
  def createPsychoTest(generalPsychoTest:PsychoTest) = {
    UIUtils.doWithProgress(LocaleManager.getString("initializingUI"), new Runnable {
      def run() {
        TableSigningDialog
      }
    })

    val rorschachTest = new RorschachTest
    rorschachTest.date = generalPsychoTest.date
    rorschachTest.testedSubject = generalPsychoTest.testedSubject
    TableSigningDialog.show(rorschachTest)
  }

  def getPsychoTestName = LocaleManager.getString("rorschachTest")
}