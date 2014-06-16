
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.form.settings

import java.util.ArrayList

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Aug 6, 2010
 * Time: 9:38:30 AM
 * To change this template use File | Settings | File Templates.
 */

object FormSettings extends Settings(getClass.getName) {

  def getDefaultSettingsMap = HashMap[String, Object](
    Settings.O_SELECTED_CATEGORIES -> new ArrayList[Long](0)
  )
}