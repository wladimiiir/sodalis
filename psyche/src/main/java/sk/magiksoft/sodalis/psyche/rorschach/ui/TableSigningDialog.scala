/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.ui

import sk.magiksoft.sodalis.psyche.rorschach.event.TestResultChanged
import sk.magiksoft.sodalis.psyche.rorschach.entity.RorschachTest
import sk.magiksoft.sodalis.psyche.data.PsycheDataManager
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.ui.OkCancelDialog
import scala.swing.Swing

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 6/16/11
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */

class TableSigningDialog extends OkCancelDialog(LocaleManager.getString("rorschachTest")) {
  private val signingPanel = new TableSigningPanel()
  private var rorschachTest: RorschachTest = _

  setModal(true)
  setMainPanel(signingPanel.peer)
  setSize(1024, 768)
  setLocationRelativeTo(null)

  getOkButton.addActionListener(Swing.ActionListener(e => {
    PsycheDataManager.addOrUpdateEntity(rorschachTest)
  }))

  def init() {}

  def show(rorschachTest: RorschachTest) = {
    this.rorschachTest = rorschachTest
    signingPanel.publish(new TestResultChanged(rorschachTest.testResult))
    setVisible(true)
    resultAction match {
      case OkCancelDialog.ACTION_OK => Option(rorschachTest)
      case _ => None
    }
  }
}