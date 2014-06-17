/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.entity

import scala.beans.BeanProperty


/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/20/11
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */

class AnswerOriginalAnswer extends Signing with QualitySignMixin {
  @BeanProperty var originalAnswer: OriginalAnswer = _

}