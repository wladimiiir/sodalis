package sk.magiksoft.sodalis.core.module

import org.hibernate.cfg.Configuration
import sk.magiksoft.sodalis.category.entity.{DynamicCategory, Category}
import sk.magiksoft.sodalis.core.data.DBManager

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

  override def startUp() {}

  override def initConfiguration(configuration: Configuration) {}

  override def prepareDB(dbManager: DBManager): Unit = {
    dbManager.createDBSchema(this)
  }

  override def install(dBManager: DBManager): Unit = {
  }
}
