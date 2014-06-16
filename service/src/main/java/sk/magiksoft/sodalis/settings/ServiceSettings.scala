/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.settings

import java.util.Collections
import sk.magiksoft.sodalis.core.printing.TableColumnWrapper

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 3/14/11
 * Time: 9:40 PM
 * To change this template use File | Settings | File Templates.
 */

object ServiceSettings extends Settings("ServiceSettings") {


  def getDefaultSettingsMap = {
    asJavaMap(Map(
      Settings.O_SELECTED_CATEGORIES -> Collections.emptyList
    ))
  }

}