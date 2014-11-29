package sk.magiksoft.sodalis.form

import entity.Form
import ui.FormContext
import sk.magiksoft.sodalis.core.context.AbstractContextManager
import org.jhotdraw.undo.UndoRedoManager

/**
 * @author wladimiiir
 * @since 2010/4/13
 */

object FormContextManager extends AbstractContextManager {
  var undoRedoManager = new UndoRedoManager

  def isFullTextActive = false

  def getDataManager = FormDataManager

  def getDefaultQuery = "from " + classOf[Form].getName

  def createContext = new FormContext
}
