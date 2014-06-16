
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.form.entity


/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Aug 27, 2010
 * Time: 10:55:36 AM
 * To change this template use File | Settings | File Templates.
 */

object Format extends Enumeration() {
  type Format = Value

  val A5 = Value("A5")
  val A4 = Value("A4")
  val A3 = Value("A3")
  val Custom = Value(LocaleManager.getString("custom"))

  def getHeightMM(format: Format): Double = format match {
    case A5 => 210d
    case A4 => 297d
    case A3 => 420d
    case _ => 100d
  }

  def getWidthMM(format: Format): Double = format match {
    case A5 => 148d
    case A4 => 210d
    case A3 => 297d
    case _ => 100d
  }
}