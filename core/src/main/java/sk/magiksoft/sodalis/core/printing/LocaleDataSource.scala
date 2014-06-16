
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


/*
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 1/26/11
 * Time: 11:04 PM
 */
package sk.magiksoft.sodalis.core.printing

import net.sf.jasperreports.engine.{JRField, JRDataSource}
import sk.magiksoft.sodalis.core.locale.LocaleManager

class LocaleDataSource extends JRDataSource {
  def getFieldValue(jrField: JRField) = LocaleManager.getString(jrField.getName) match {
    case "ERROR" => ""
    case string: String => string
  }

  def next = true
}