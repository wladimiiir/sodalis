/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.entity

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/17/11
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */

trait QualitySignMixin {
  var qualitySign: Option[QualitySign.Value] = None

  def getQualitySign = qualitySign match {
    case Some(sign) => sign.id
    case None => -1
  }

  def setQualitySign(value: Int) {
    qualitySign = QualitySign.values.find(_.id == value)
  }
}