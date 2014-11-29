package sk.magiksoft.sodalis.core.module

import sk.magiksoft.sodalis.category.entity.{DynamicCategory, Category}

/**
 * @author wladimiiir
 * @since 2011/4/17
 */

abstract class AbstractModule extends Module {
  private var dynamicCategories = List[Category]()

  def registerDynamicCategory(dynamicCategory: Category) {
    dynamicCategories ::= dynamicCategory
  }

  def getDynamicCategories = {
    dynamicCategories.foreach {
      _.asInstanceOf[DynamicCategory].refresh()
    }
    dynamicCategories
  }

  def postInitialization() {}
}
