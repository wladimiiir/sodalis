package sk.magiksoft.sodalis.category.imex

import sk.magiksoft.sodalis.category.entity.Category
import sk.magiksoft.sodalis.core.imex.{ImExManager, ImportProcessor}
import sk.magiksoft.sodalis.core.data.DefaultDataManager

/**
 * @author wladimiiir
 * @since 2010/11/21
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
