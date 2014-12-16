package sk.magiksoft.sodalis.category

import entity.{CategoryHistoryData, Category}
import imex.CategoryImportResolver
import sk.magiksoft.sodalis.core.imex.ImExManager
import sk.magiksoft.sodalis.core.module.{DynamicModule, ModuleDescriptor, AbstractModule}
import sk.magiksoft.sodalis.core.factory.{IconFactory, EntityFactory}
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.core.locale.LocaleManager

/**
 * @author wladimiiir
 * @since 2011/4/17
 */

@DynamicModule
class CategoryModule extends AbstractModule {
  private lazy val moduleDescriptor = new ModuleDescriptor(IconFactory.getInstance.getIcon("categoryModule").asInstanceOf[ImageIcon],
    LocaleManager.getString("categorization"))

  override def startUp(): Unit = {
    EntityFactory.getInstance.registerEntityProperties(classOf[Category], classOf[CategoryHistoryData])
    ImExManager.registerImportProcessor(classOf[Category], new CategoryImportResolver)
  }

  def getDataListener = CategoryManager.getInstance

  def getContextManager = CategoryManager.getInstance

  def getModuleDescriptor = moduleDescriptor
}
