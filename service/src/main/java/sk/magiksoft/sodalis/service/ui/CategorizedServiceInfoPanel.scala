/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.service.ui

import sk.magiksoft.sodalis.category.ui.CategorizedEntityInfoPanel
import sk.magiksoft.sodalis.service.ServiceModule

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 3/17/11
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */

class CategorizedServiceInfoPanel extends CategorizedEntityInfoPanel{
  def getModuleClass = classOf[ServiceModule]
}