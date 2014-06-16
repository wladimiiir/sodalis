/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.core.action

import sk.magiksoft.sodalis.core.entity.Entity

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 3/28/11
 * Time: 6:35 PM
 * To change this template use File | Settings | File Templates.
 */

trait EntityAction[A <: Entity] {
  def isAllowed(entities: List[A]): Boolean

  def getName(entities: List[A]): String

  def apply(entities: List[A]): Unit
}