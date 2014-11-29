package sk.magiksoft.sodalis.core.printing

import net.sf.jasperreports.engine.{JRField, JRDataSource}
import sk.magiksoft.sodalis.core.locale.LocaleManager

/**
 * @author wladimiiir
 * @since 2011/1/26
 */
class LocaleDataSource extends JRDataSource {
  def getFieldValue(jrField: JRField) = LocaleManager.getString(jrField.getName) match {
    case "ERROR" => ""
    case string: String => string
  }

  def next = true
}
