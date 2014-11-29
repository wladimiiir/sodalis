package sk.magiksoft.sodalis.item

import java.util.ArrayList
import sk.magiksoft.sodalis.core.settings.Settings
import sk.magiksoft.sodalis.core.printing.TablePrintSettings
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2010/10/26
 */

object ItemSettings extends Settings("itemSettings") {
  def getDefaultSettingsMap = Map(
    Settings.O_USER_PRINT_SETTINGS -> new ArrayList[TablePrintSettings]
  )
}
