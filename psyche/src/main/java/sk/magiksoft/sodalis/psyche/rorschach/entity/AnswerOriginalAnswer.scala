package sk.magiksoft.sodalis.psyche.rorschach.entity

import scala.beans.BeanProperty


/**
 * @author wladimiiir
 * @since 2011/5/20
 */

class AnswerOriginalAnswer extends Signing with QualitySignMixin {
  @BeanProperty var originalAnswer: OriginalAnswer = _

}
