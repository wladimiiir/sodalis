package sk.magiksoft.sodalis.person.action

import java.awt.event.ActionEvent
import javax.swing.{Action, AbstractAction}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.icon.IconManager
import sk.magiksoft.sodalis.person.ui.AbstractPersonContext
import sk.magiksoft.sodalis.person.entity.Person
import sk.magiksoft.sodalis.category.CategoryManager
import sk.magiksoft.sodalis.core.printing.{DefaultSettingsTableSettingsListener, TablePrintDialog}
import sk.magiksoft.sodalis.category.report.CategoryWrapperDataSource
import collection.JavaConversions._
import sk.magiksoft.sodalis.core.entity.property.{EntityPropertyTranslatorManager, EntityPropertyJRDataSource}

/**
 * @author wladimiiir
 * @since 2010/12/16
 */

class PrintPersonAction(personContext: AbstractPersonContext) extends AbstractAction("", IconManager.getInstance.getIcon("print")) {

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
