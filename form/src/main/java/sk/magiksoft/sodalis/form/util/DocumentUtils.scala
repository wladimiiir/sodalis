
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.form.util

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Aug 27, 2010
 * Time: 11:35:15 AM
 * To change this template use File | Settings | File Templates.
 */

object DocumentUtils {
  def toPix(mm: Double, dpi: Int): Int = math.round(mm * dpi / 25.4).toInt

  def toPix(mm: Double): Int = toPix(mm, 72)

  def toMM(pix: Int, dpi: Int): Double = pix.toDouble / (dpi / 25.4)

  def toMM(pix: Int): Double = toMM(pix, 72)
}