package sk.magiksoft.sodalis.form.ui

import sk.magiksoft.sodalis.form.FormModule
import sk.magiksoft.sodalis.category.ui.CategorizedEntityInfoPanel

/**
 * @author wladimiiir
 * @since 2010/8/11
 */

class CategorizedFormInfoPanel extends CategorizedEntityInfoPanel {
  def getModuleClass = classOf[FormModule]
}
