/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.core.registry

import sk.magiksoft.sodalis.core.entity.Entity
import sk.magiksoft.sodalis.core.action.EntityAction

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 3/22/11
 * Time: 7:42 PM
 * To change this template use File | Settings | File Templates.
 */

object RegistryManager {
  def registerPopupAction[A <: Entity](clazz: Class[A], action: EntityAction[A]) =
    PopupRegistryManager.registerPopupAction(clazz, action)

  def getPopupActions[A <: Entity](entities: List[A]) =
    PopupRegistryManager.getPopupActions(entities)

}