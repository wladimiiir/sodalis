
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.category.imex

import sk.magiksoft.sodalis.category.entity.Category
import sk.magiksoft.sodalis.core.imex.{ImExManager, ImportProcessor}
import sk.magiksoft.sodalis.core.data.DefaultDataManager

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 11/21/10
 * Time: 12:00 PM
 * To change this template use File | Settings | File Templates.
 */

class CategoryImportResolver extends ImportProcessor[Category] {
  def findSimilarEntity(entity: Category) = {
    val parentWhere = new StringBuilder
    val parent = new StringBuilder
    var parentCategory = entity.getParentCategory

    while (parentCategory != null) {
      parentWhere ++= " and " + parent + "parentCategory.name='" + parentCategory.getName + "'"
      parent ++= "parentCategory."
      parentCategory = parentCategory.getParentCategory
    }
    if (parentWhere.isEmpty) {
      parentWhere ++= " and parentCategory is null"
    }
    DefaultDataManager.getInstance.getDatabaseEntity(classOf[Category], "name='" + entity.getName + "'" + parentWhere)
  }

  def processImport(entity: Category) = {
    findSimilarEntity(entity) match {
      case category: Category => {
        entity.setChildCategories(category.getChildCategories)
        entity.setId(category.getId)
        entity.setCategoryDatas(category.getCategoryDatas)
        DefaultDataManager.getInstance.updateDatabaseEntity(entity)
        entity
      }
      case _ => {
        entity.getParentCategory match {
          case parent: Category => {
            entity.setParentCategory(ImExManager.processEntity(parent))
          }
          case _ =>
        }
        DefaultDataManager.getInstance.addDatabaseEntity(entity)
      }
    }
  }
}