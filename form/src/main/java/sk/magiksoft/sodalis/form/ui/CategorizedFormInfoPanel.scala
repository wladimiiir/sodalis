
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.form.ui

import sk.magiksoft.sodalis.category.ui.CategorizedEntityInfoPanel
import sk.magiksoft.sodalis.form.FormModule

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Aug 11, 2010
 * Time: 8:26:48 PM
 * To change this template use File | Settings | File Templates.
 */

class CategorizedFormInfoPanel extends CategorizedEntityInfoPanel {
  def getModuleClass = classOf[FormModule]
}