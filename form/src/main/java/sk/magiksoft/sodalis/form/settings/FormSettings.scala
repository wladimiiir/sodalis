package sk.magiksoft.sodalis.form.settings

import java.util.ArrayList
import sk.magiksoft.sodalis.core.settings.Settings
import scala.collection.immutable.HashMap
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2010/8/6
 */

object FormSettings extends Settings(getClass.getName) {

  def getDefaultSettingsMap = HashMap[String, Object](
    Settings.O_SELECTED_CATEGORIES -> new ArrayList[Long](0)
  )
}
