
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.folkensemble.inventory

import entity.InventoryItem
import sk.magiksoft.sodalis.item.ui.DefaultItemContext
import sk.magiksoft.sodalis.folkensemble.inventory.data.InventoryDataManager
import java.net.URL
import sk.magiksoft.sodalis.core.utils.Utils
import sk.magiksoft.sodalis.item.factory.ItemPropertiesFactory
import ui.{InventoryItemContext, InventoryControlPanel}
import sk.magiksoft.sodalis.core.{SodalisApplication}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: May 20, 2010
 * Time: 1:02:49 PM
 * To change this template use File | Settings | File Templates.
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