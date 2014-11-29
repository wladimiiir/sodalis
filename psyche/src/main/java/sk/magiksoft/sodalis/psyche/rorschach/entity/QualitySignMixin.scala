package sk.magiksoft.sodalis.psyche.rorschach.entity

/**
 * @author wladimiiir
 * @since 2011/5/17
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
