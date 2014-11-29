package sk.magiksoft.sodalis.folkensemble.inventory

import entity.InventoryItem
import sk.magiksoft.sodalis.item.ui.DefaultItemContext
import sk.magiksoft.sodalis.folkensemble.inventory.data.InventoryDataManager
import java.net.URL
import sk.magiksoft.sodalis.core.utils.Utils
import sk.magiksoft.sodalis.item.factory.ItemPropertiesFactory
import ui.{InventoryItemContext, InventoryControlPanel}
import sk.magiksoft.sodalis.core.{SodalisApplication}
import sk.magiksoft.sodalis.core.context.AbstractContextManager
import sk.magiksoft.sodalis.item.entity.Item

/**
 * @author wladimiiir
 * @since 2010/5/20
 */

object InventoryContextManager extends AbstractContextManager {
  def getDataManager = InventoryDataManager.getInstance

  def getDefaultQuery = "from " + classOf[Item].getName

  def isFullTextActive = false

  override def getFilterConfigFileURL: URL = {
    return Utils.getURL("file:data/filter/InventoryFilter.xml")
  }

  def createContext = {
    val propertiesFactory: Option[ItemPropertiesFactory] = (SodalisApplication.get.getLicenseManager.getLicense.isRestricted("ItemDefinitionPanel")
      && !SodalisApplication.get.getLicenseManager.getLicense.isDebugMode) match {
      case true => None
      case false => Option(new ItemPropertiesFactory(Utils.getURL("file:config/InventoryItemProperties.xml")))
    }
    new InventoryItemContext(propertiesFactory)
  }
}
