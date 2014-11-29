package sk.magiksoft.sodalis.service

import data.ServiceDataManager
import entity.Service
import ui.ServiceContext
import sk.magiksoft.sodalis.core.context.AbstractContextManager

/**
 * @author wladimiiir
 * @since 2011/3/10
 */

object ServiceContextManager extends AbstractContextManager {
  def getDataManager = ServiceDataManager

  def getDefaultQuery = "from " + classOf[Service].getName

  def createContext = new ServiceContext

  def isFullTextActive = false
}
