
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.item

import java.util.ArrayList

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 10/26/10
 * Time: 9:55 PM
 * To change this template use File | Settings | File Templates.
 */

object ItemSettings extends Settings("itemSettings") {
  def getDefaultSettingsMap = asMap(Map(
    Settings.O_USER_PRINT_SETTINGS -> new ArrayList[TablePrintSettings]
  ))
}