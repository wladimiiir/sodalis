package sk.magiksoft.sodalis.psyche.rorschach.ui.interpretation

/*
 * Copyright (c) 2011
 */

import swing.GridBagPanel
import sk.magiksoft.sodalis.psyche.ui.LabeledGridBagPanelMixin
import sk.magiksoft.sodalis.psyche.rorschach.entity.TestResult

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/22/11
 * Time: 7:55 AM
 * To change this template use File | Settings | File Templates.
 */

class GeneralInterpretationPanel extends GridBagPanel with InterpretationPanel with LabeledGridBagPanelMixin {

  protected def setupValues(testResult: Option[TestResult]) {
    testResult match {
      case Some(result) =>
      case None =>
    }
  }
}