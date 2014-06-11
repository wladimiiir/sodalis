
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 1/26/11
 * Time: 7:22 PM
 */
package sk.magiksoft.sodalis.core.printing

import sk.magiksoft.sodalis.core.context.Context
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import collection.JavaConversions._

class ContextPrintAction(context:Context, printDocument:PrintDocument) extends AbstractAction(printDocument.name){

  def actionPerformed(e: ActionEvent) = {
    context.getSelectedEntities.headOption match {
      case Some(entity) => printDocument.print(entity)
      case None => //todo: no entity selected message
    }
  }
}