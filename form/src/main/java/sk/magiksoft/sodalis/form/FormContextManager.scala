
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.form

import entity.Form
import ui.FormContext
import sk.magiksoft.sodalis.core.context.AbstractContextManager
import org.jhotdraw.undo.UndoRedoManager

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Apr 13, 2010
 * Time: 8:58:35 PM
 * To change this template use File | Settings | File Templates.
 */

object FormContextManager extends AbstractContextManager {
  var undoRedoManager = new UndoRedoManager

  def isFullTextActive = false

  def getDataManager = FormDataManager

  def getDefaultQuery = "from " + classOf[Form].getName

  def createContext = new FormContext
}