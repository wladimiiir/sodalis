package sk.magiksoft.sodalis.psyche

import data.PsycheDataManager
import rorschach.entity.RorschachTestCreator
import ui.PsychoTestContext
import sk.magiksoft.sodalis.core.context.AbstractContextManager

/**
 * @author wladimiiir
 * @since 2011/5/13
 */

object PsycheContextManager extends AbstractContextManager {

  PsycheDataManager.registerPsychoTestCreator(new RorschachTestCreator)

  def getDataManager = PsycheDataManager

  def getDefaultQuery = "from PsychoTest"

  def createContext() = new PsychoTestContext


  def isFullTextActive = false
}
