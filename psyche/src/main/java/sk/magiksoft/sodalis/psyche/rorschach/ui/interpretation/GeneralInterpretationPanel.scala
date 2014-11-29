package sk.magiksoft.sodalis.psyche.rorschach.ui.interpretation

/*
 * Copyright (c) 2011
 */

import swing.GridBagPanel
import sk.magiksoft.sodalis.psyche.ui.LabeledGridBagPanelMixin
import sk.magiksoft.sodalis.psyche.rorschach.entity.TestResult

/**
 * @author wladimiiir
 * @since 2011/5/22
 */

class GeneralInterpretationPanel extends GridBagPanel with InterpretationPanel with LabeledGridBagPanelMixin {

  protected def setupValues(testResult: Option[TestResult]) {
    testResult match {
      case Some(result) =>
      case None =>
    }
  }
}
