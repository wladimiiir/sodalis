package sk.magiksoft.sodalis.core.controlpanel

import sk.magiksoft.sodalis.core.entity.Entity

import scala.collection.mutable

/**
 * @author Y12370
 * @since  2015/01/23
 */
object ControlPanelRegistry {
  private val entityInfoPanelClassesMap = new mutable.HashMap[String, List[Class[_ <: InfoPanel]]]() {
    override def default(key: String): List[Class[_ <: InfoPanel]] = List()
  }

  def registerInfoPanels(key: String, infoPanelClasses: List[Class[_ <: InfoPanel]]): Unit = {
    entityInfoPanelClassesMap += key -> {
      infoPanelClasses ++ entityInfoPanelClassesMap(key)
    }
  }
  
  def getEntityInfoPanelClasses(key: String) = entityInfoPanelClassesMap(key)
}
