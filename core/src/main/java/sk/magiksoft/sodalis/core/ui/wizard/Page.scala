
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


/*
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 2/18/11
 * Time: 3:56 PM
 */
package sk.magiksoft.sodalis.core.ui.wizard

import swing.Component

trait Page {
  def getPreviousPage: Option[Page]

  def getNextPage: Option[Page]

  def getComponent: Component
}