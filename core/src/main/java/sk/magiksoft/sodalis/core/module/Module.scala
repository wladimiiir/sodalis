package sk.magiksoft.sodalis.core.module

import sk.magiksoft.sodalis.core.context.ContextManager
import sk.magiksoft.sodalis.core.data.DataListener
import sk.magiksoft.sodalis.category.entity.Category

/**
 * @author wladimiiir
 * @since 2011/4/22
 */

trait Module {
  def getModuleDescriptor: ModuleDescriptor

  def getContextManager: ContextManager

  def getDataListener: DataListener

  def postInitialization()

  def getDynamicCategories: List[Category]

  def registerDynamicCategory(dynamicCategory: Category)
}
