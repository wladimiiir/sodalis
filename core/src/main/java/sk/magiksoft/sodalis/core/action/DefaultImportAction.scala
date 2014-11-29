package sk.magiksoft.sodalis.core.action

import javax.swing.Action
import sk.magiksoft.sodalis.core.locale.LocaleManager
import java.util.List
import collection.JavaConversions._
import sk.magiksoft.sodalis.core.data.DefaultDataManager
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.entity.DatabaseEntity

/**
 * @author wladimiiir
 * @since 2010/12/16
 */

class DefaultImportAction(entityClass: Class[_ <: DatabaseEntity]) extends AbstractImportAction {
  putValue(Action.SHORT_DESCRIPTION, LocaleManager.getString("import"))

  def getActionMessage(objects: List[_]) = new ActionMessage(true, LocaleManager.getString("import"))

  def importObjects(objects: List[_]) {
    val entities = DefaultDataManager.getInstance.addOrUpdateEntities(objects.filter(o => o.getClass == entityClass).map {
      _.asInstanceOf[DatabaseEntity]
    }.toList).toList
    SodalisApplication.get.showMessage(LocaleManager.getString("entitiesImported"), new Integer(entities.size))
  }
}
