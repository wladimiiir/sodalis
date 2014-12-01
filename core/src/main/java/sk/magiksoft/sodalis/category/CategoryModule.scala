package sk.magiksoft.sodalis.category

import entity.{CategoryHistoryData, Category}
import imex.CategoryImportResolver
import sk.magiksoft.sodalis.core.imex.ImExManager
import sk.magiksoft.sodalis.core.module.{VisibleModule, ModuleDescriptor, AbstractModule}
import sk.magiksoft.sodalis.core.factory.{IconFactory, EntityFactory}
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.core.locale.LocaleManager

/**
 * @author wladimiiir
 * @since 2011/4/17
 */

@VisibleModule
class CategoryModule extends AbstractModule {

  EntityFactory.getInstance.registerEntityProperties(classOf[Category], classOf[CategoryHistoryData])
  ImExManager.registerImportProcessor(classOf[Category], new CategoryImportResolver)

  def getDataListener = CategoryManager.getInstance

  def getContextManager = CategoryManager.getInstance

  def getModuleDescriptor = new ModuleDescriptor(IconFactory.getInstance.getIcon("categoryModule").asInstanceOf[ImageIcon], LocaleManager.getString("categorization"))
}
