package sk.magiksoft.sodalis.category

import javax.swing.ImageIcon

import sk.magiksoft.sodalis.category.entity.Category
import sk.magiksoft.sodalis.category.imex.CategoryImportResolver
import sk.magiksoft.sodalis.core.data.DBManager
import sk.magiksoft.sodalis.core.imex.ImExManager
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.{AbstractModule, ModuleDescriptor, VisibleModule}
import sk.magiksoft.sodalis.icon.IconManager

/**
 * @author wladimiiir
 * @since 2011/4/17
 */

@VisibleModule
class CategoryModule extends AbstractModule {
  private lazy val moduleDescriptor = new ModuleDescriptor(IconManager.getInstance.getIcon("categoryModule").asInstanceOf[ImageIcon],
    LocaleManager.getString("categorization"))

  override def startUp(): Unit = {
    ImExManager.registerImportProcessor(classOf[Category], new CategoryImportResolver)
  }

  def getDataListener = CategoryManager.getInstance

  def getContextManager = CategoryManager.getInstance

  def getModuleDescriptor = moduleDescriptor

  override def prepareDB(dbManager: DBManager): Unit = {
    //schema already created -> skip
  }
}
