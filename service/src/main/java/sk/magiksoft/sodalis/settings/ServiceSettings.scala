/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.settings

import sk.magiksoft.sodalis.core.settings.Settings
import collection.JavaConversions._
import java.util.Collections
import sk.magiksoft.sodalis.core.printing.{TableColumnWrapper, TablePrintSettings}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 3/14/11
 * Time: 9:40 PM
 * To change this template use File | Settings | File Templates.
 */

object ServiceSettings extends Settings("ServiceSettings"){
  

  def getDefaultSettingsMap = {
    asJavaMap(Map(
      Settings.O_SELECTED_CATEGORIES -> Collections.emptyList
    ))
  }

}