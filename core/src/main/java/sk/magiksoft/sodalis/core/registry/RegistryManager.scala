package sk.magiksoft.sodalis.core.registry

import sk.magiksoft.sodalis.core.entity.Entity
import sk.magiksoft.sodalis.core.action.EntityAction

/**
 * Created by IntelliJ IDEA.drdfdsf213
 * @author wladimiiir
 * @since 2011/3/22
 */

object RegistryManager {
  def registerPopupAction[A <: Entity](clazz: Class[A], action: EntityAction[A]) =
    PopupRegistryManager.registerPopupAction(clazz, action)

  def getPopupActions[A <: Entity](entities: List[A]) =
    PopupRegistryManager.getPopupActions(entities)

}
