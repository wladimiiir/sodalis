/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.entity


/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/13/11
 * Time: 10:14 AM
 * To change this template use File | Settings | File Templates.
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
