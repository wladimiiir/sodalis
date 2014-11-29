package sk.magiksoft.sodalis.settings

import java.util.Collections
import sk.magiksoft.sodalis.core.settings.Settings
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2011/3/14
 */

object ServiceSettings extends Settings("ServiceSettings") {


  def getDefaultSettingsMap = {
    Map(
      Settings.O_SELECTED_CATEGORIES -> Collections.emptyList
    )
  }

}
