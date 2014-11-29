package sk.magiksoft.sodalis.psyche.rorschach.entity

import scala.beans.BeanProperty


/**
 * @author wladimiiir
 * @since 2011/5/15
 */

class AnswerDeterminant extends Signing with QualitySignMixin {
  @BeanProperty var determinant: Determinant = _
}
