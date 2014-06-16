/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.category.entity

import sk.magiksoft.sodalis.core.entity.DatabaseEntity
import sk.magiksoft.sodalis.core.data.DefaultDataManager
import java.util.{List => jList}
import collection.JavaConversions._
import collection.mutable.ListBuffer

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 4/22/11
 * Time: 10:06 AM
 * To change this template use File | Settings | File Templates.
 */

abstract class EntityDynamicCategory[E <: DatabaseEntity, C <: Categorized](name: String, query: String) extends AbstractDynamicCategory {
  setName(name)

  protected def acceptCategorized(entity: E, categorized: C): Boolean

  protected def getEntityString(entity: E) = entity.toString

  protected def getWrappedEntity(entity: E): DatabaseEntity = entity

  def createChildCategories() = {
    val entities = DefaultDataManager.getInstance().getDatabaseEntities(query).asInstanceOf[jList[E]]
    val wrappedIDs = new ListBuffer[java.lang.Long]
    val entityStrings = new ListBuffer[String]
    val dynamicCategories = new ListBuffer[DynamicCategory]

    for (entity <- entities
         if !wrappedIDs.contains(getWrappedEntity(entity).getId) && !entityStrings.contains(getEntityString(entity))) {

      dynamicCategories += new DynamicCategory(this, getEntityString(entity)) {
        def refresh() {}

        def acceptCategorized(categorized: Categorized) = EntityDynamicCategory.this.acceptCategorized(entity, categorized.asInstanceOf[C])
      }

      wrappedIDs += getWrappedEntity(entity).getId
      //TODO: check if strings should not be allowed
      entityStrings += getEntityString(entity)
    }

    dynamicCategories
  }
}