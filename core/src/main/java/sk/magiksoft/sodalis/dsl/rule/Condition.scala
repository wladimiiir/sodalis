/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.dsl.rule

import sk.magiksoft.sodalis.dsl.rule.Rules._

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/31/11
 * Time: 8:56 PM
 * To change this template use File | Settings | File Templates.
 */

class Condition(value:Boolean) {
  def and(condition: Condition) = new Condition(fulfilled) {
    override def fulfilled = value && condition
  }

  def or(condition: Condition) = new Condition(fulfilled) {
    override def fulfilled = value || condition
  }

  def fulfilled = value
}