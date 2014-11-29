package sk.magiksoft.sodalis.dsl.rule

import sk.magiksoft.sodalis.dsl.rule.Rules._

/**
 * @author wladimiiir
 * @since 2011/5/31
 */

class Condition(value: Boolean) {
  def and(condition: Condition) = new Condition(fulfilled) {
    override def fulfilled = value && condition
  }

  def or(condition: Condition) = new Condition(fulfilled) {
    override def fulfilled = value || condition
  }

  def fulfilled = value
}
