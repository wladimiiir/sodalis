
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.person.action

import org.jhotdraw.draw.AbstractAttributedCompositeFigure
import java.awt.event.ActionEvent
import sk.magiksoft.sodalis.core.factory.IconFactory
import javax.swing.{Action, AbstractAction}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.person.ui.AbstractPersonContext
import sk.magiksoft.sodalis.person.entity.Person
import sk.magiksoft.sodalis.category.CategoryManager
import sk.magiksoft.sodalis.core.printing.{DefaultSettingsTableSettingsListener, TablePrintDialog, JRExtendedDataSource}
import sk.magiksoft.sodalis.category.report.CategoryWrapperDataSource
import collection.JavaConversions._
import net.sf.jasperreports.engine.JRDataSource
import sk.magiksoft.sodalis.core.entity.property.{EntityPropertyTranslatorManager, EntityPropertyJRDataSource}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 12/16/10
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */

class PrintPersonAction(personContext:AbstractPersonContext) extends AbstractAction("", IconFactory.getInstance.getIcon("print")){

  putValue(Action.SHORT_DESCRIPTION, LocaleManager.getString("print"))

  def actionPerformed(e: ActionEvent) = {
    val categoryShown = personContext.getCategoryTreeComponent.isComponentShown
    val objects = categoryShown match {
      case true =>
      case false => personContext.getEntities
    }
    val dataSource = categoryShown match {
      case true => new CategoryWrapperDataSource(CategoryManager.getInstance.getCategoryPathWrappers(personContext.getCategoryTreeComponent.getRoot),
        new EntityPropertyJRDataSource[Person](Nil))
      case false => new EntityPropertyJRDataSource[Person](asScalaBuffer(personContext.getEntities).toList.asInstanceOf[List[Person]])
    }

    val dialog = new TablePrintDialog(personContext.getSettings, EntityPropertyTranslatorManager.getTranslator(classOf[Person]), dataSource)
    if (categoryShown) {
      dialog.setGroups(personContext.getCategoryTreeComponent.getSelectedCategoryPath)
    }
    dialog.addTableSettingsListener(new DefaultSettingsTableSettingsListener(personContext.getSettings))
    dialog.setVisible(true)
  }
}