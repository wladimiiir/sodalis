/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.entity

import reflect.BeanProperty

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/15/11
 * Time: 10:09 AM
 * To change this template use File | Settings | File Templates.
 */

class AnswerDeterminant extends Signing with QualitySignMixin {
  @BeanProperty var determinant:Determinant = _
}
