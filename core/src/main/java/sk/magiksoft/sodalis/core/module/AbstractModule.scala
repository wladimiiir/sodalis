package sk.magiksoft.sodalis.core.module

import sk.magiksoft.sodalis.category.entity.{DynamicCategory, Category}

/**
 * @author wladimiiir
 * @since 2011/4/17
 */

abstract class AbstractModule extends Module {
  private var dynamicCategories = List[Category]()

  override def registerDynamicCategory(dynamicCategory: Category) {
    dynamicCategories ::= dynamicCategory
  }

  override def getDynamicCategories = {
    dynamicCategories.foreach {
      _.asInstanceOf[DynamicCategory].refresh()
    }
    dynamicCategories
  }

  override def postStartUp() {}

  override def plugInModule(classLoader: ClassLoader) {}
}
