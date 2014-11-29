package sk.magiksoft.sodalis.core.printing

import sk.magiksoft.sodalis.core.context.Context
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2011/1/26
 */
class ContextPrintAction(context: Context, printDocument: PrintDocument) extends AbstractAction(printDocument.name) {

  def actionPerformed(e: ActionEvent) = {
    context.getSelectedEntities.headOption match {
      case Some(entity) => printDocument.print(entity)
      case None => //todo: no entity selected message
    }
  }
}
