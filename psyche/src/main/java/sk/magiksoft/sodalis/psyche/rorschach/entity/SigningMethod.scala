package sk.magiksoft.sodalis.psyche.rorschach.entity

import sk.magiksoft.sodalis.core.locale.LocaleManager


/**
 * @author wladimiiir
 * @since 2011/5/13
 */

object SigningMethod extends Enumeration {
  val General = Value(LocaleManager.getString("general"))
  val Aperception = Value(LocaleManager.getString("aperception"))
  val Determinants = Value(LocaleManager.getString("determinants"))
  val Contents = Value(LocaleManager.getString("contents"))
  val AnswerFrequency = Value(LocaleManager.getString("answerFrequency"))
  val SpecialSigns = Value(LocaleManager.getString("specialSigns"))

  val valueList = List(General, Aperception, Determinants, Contents, AnswerFrequency, SpecialSigns)
}
