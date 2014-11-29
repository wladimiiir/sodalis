package sk.magiksoft.sodalis.service.ui

import sk.magiksoft.sodalis.service.ServiceModule
import sk.magiksoft.sodalis.category.ui.CategorizedEntityInfoPanel

/**
 * @author wladimiiir
 * @since 2011/3/17
 */

class CategorizedServiceInfoPanel extends CategorizedEntityInfoPanel {
  def getModuleClass = classOf[ServiceModule]
}
