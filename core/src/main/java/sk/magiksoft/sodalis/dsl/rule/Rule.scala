/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.dsl.rule

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/31/11
 * Time: 8:56 PM
 * To change this template use File | Settings | File Templates.
 */

class Rule[A](mainCondition: A => Condition) {
  var givingOption: Option[String] = None

  def giving(result: String) = {
    givingOption = Option(result)
    this
  }

  def accepts(input: A) = mainCondition(input).fulfilled
}

object Rule {
  def complying[A](rule: A => Condition): Rule[A] = new Rule[A](rule)
}