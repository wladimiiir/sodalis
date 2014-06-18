
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.form.ui.tool

import scala.swing.event.MouseEvent


/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Apr 23, 2010
 * Time: 10:33:42 PM
 * To change this template use File | Settings | File Templates.
 */

trait MouseHandler {
  def handleMousePressed(e: MouseEvent)

  def handleMouseClicked(e: MouseEvent)
}