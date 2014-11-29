package sk.magiksoft.sodalis.category.entity

import collection.mutable.ListBuffer
import collection.JavaConversions._
import sk.magiksoft.sodalis.core.data.DefaultDataManager
import java.util.List
import sk.magiksoft.sodalis.core.entity.DatabaseEntity

/**
 * @author wladimiiir
 * @since 2011/5/7
 */

abstract class PropertyDynamicCategory[A <: DatabaseEntity, B](clazz: Class[_ <: DatabaseEntity], property: String, name: String) extends AbstractDynamicCategory {
  setName(name)

  def createChildCategories() = {
    val values = DefaultDataManager.getInstance().getDatabaseEntities("select distinct " + property + " from " + clazz.getName + " order by 1").asInstanceOf[List[B]]
    val dynamicCategories = new ListBuffer[DynamicCategory]

    for (value <- values) {
      dynamicCategories += new DynamicCategory(this, value.toString()) {
        def refresh() {}

        def acceptCategorized(categorized: Categorized) = acceptProperty(categorized.asInstanceOf[A], value)
      }
    }

    dynamicCategories
  }

  protected def acceptProperty(entity: A, value: B): Boolean
}
