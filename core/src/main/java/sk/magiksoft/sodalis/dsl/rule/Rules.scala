package sk.magiksoft.sodalis.dsl.rule

import collection.mutable.ListBuffer

/**
 * @author wladimiiir
 * @since 2011/5/31
 */

class Rules[A] {

  object rule {
    def complying(condition: (A => Condition)): Rule[A] = new Rule[A](condition)
  }

  private val rules = new ListBuffer[Rule[A]]

  def add(rule: Rule[A]) {
    rules += rule
  }

  def find(input: A) = rules.find(_.accepts(input)) match {
    case Some(rule) => rule.givingOption
    case None => None
  }
}

object Rules {
  implicit def condition2Boolean(condition: Condition) = condition.fulfilled

  implicit def boolean2Condition(bool: Boolean) = new Condition(bool)
}
