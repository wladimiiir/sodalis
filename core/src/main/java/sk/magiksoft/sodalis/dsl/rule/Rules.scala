/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.dsl.rule

import collection.mutable.ListBuffer

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/31/11
 * Time: 8:54 PM
 * To change this template use File | Settings | File Templates.
 */

class Rules[A] {
  object rule {
    def complying(condition:(A => Condition)):Rule[A] = new Rule[A](condition)
  }
  private val rules = new ListBuffer[Rule[A]]

  def add(rule:Rule[A]) {
    rules += rule
  }

  def find(input:A) = rules.find(_.accepts(input)) match {
    case Some(rule) => rule.givingOption
    case None => None
  }
}

object Rules {
  implicit def condition2Boolean(condition: Condition) = condition.fulfilled

  implicit def boolean2Condition(bool: Boolean) = new Condition(bool)
}
