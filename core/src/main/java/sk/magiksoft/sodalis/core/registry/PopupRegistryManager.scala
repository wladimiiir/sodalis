/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.core.registry

import collection._
import mutable.ListBuffer
import sk.magiksoft.sodalis.core.entity.Entity
import sk.magiksoft.sodalis.core.action.EntityAction
import reflect.Manifest

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 3/22/11
 * Time: 9:38 PM
 * To change this template use File | Settings | File Templates.
 */

private[registry] object PopupRegistryManager {

  private val actionMap = mutable.Map.empty[Class[_], ListBuffer[EntityAction[_ <: Entity]]]

  def registerPopupAction[A <: Entity](clazz: Class[A], action: EntityAction[A]) {
    actionMap.get(clazz) match {
      case Some(buffer) => buffer += action
      case None => actionMap += clazz -> ListBuffer(action)
    }
  }

  def getPopupActions[A <: Entity](entities: List[A]) = entities.headOption match {
    case Some(entity) => actionMap.get(entity.getClass) match {
      case Some(buffer) => buffer.asInstanceOf[ListBuffer[EntityAction[A]]].filter {
        _.isAllowed(entities)
      }.toList
      case None => Nil
    }
    case None => Nil
  }
}