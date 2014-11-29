package sk.magiksoft.sodalis.service.action

import java.awt.event.ActionEvent
import java.util.{List => jList}
import sk.magiksoft.sodalis.service.ui.ServiceContext
import sk.magiksoft.sodalis.core.action.{ActionMessage, MessageAction}
import sk.magiksoft.sodalis.settings.ServiceSettings
import sk.magiksoft.sodalis.category.CategoryManager
import collection.JavaConversions._
import sk.magiksoft.sodalis.core.entity.property.{EntityPropertyJRDataSource, EntityPropertyTranslatorManager}
import sk.magiksoft.sodalis.service.entity.Service
import sk.magiksoft.sodalis.core.settings.Settings
import sk.magiksoft.sodalis.core.printing.{TablePrintSettings, DefaultSettingsTableSettingsListener, TablePrintDialog, TableColumnWrapper}
import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.sodalis.core.printing.TableColumnWrapper.Alignment
import sk.magiksoft.sodalis.category.report.CategoryWrapperDataSource
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.factory.IconFactory

/**
 * @author wladimiiir
 * @since 2011/3/11
 */

class ServicePrintAction(context: ServiceContext) extends MessageAction(IconFactory.getInstance.getIcon("print")) {
  def getActionMessage(objects: jList[_]) = new ActionMessage(true, LocaleManager.getString("print"))

  def actionPerformed(e: ActionEvent) {
    def createDefaultPrintSettings = {
      val alignmentMap = Map[Class[_], Alignment](
        classOf[String] -> Alignment.LEFT,
        classOf[java.lang.Double] -> Alignment.RIGHT
      )
      val settings = new TablePrintSettings("")
      settings.setShowPageHeader(false)
      settings.setTableColumnWrappers(context.getTable.getColumnModel.getColumns.map {
        column => {
          val key = context.getTable.getModel.asInstanceOf[ObjectTableModel[Service]].getColumnIdentificator(column.getModelIndex) match {
            case key: String => key
            case _ => ""
          }
          val valueClass = EntityPropertyTranslatorManager.getValueClass(classOf[Service], key)
          new TableColumnWrapper(
            key,
            column.getHeaderValue.toString, column.getWidth,
            valueClass match {
              case Some(clazz) => clazz
              case None => classOf[String]
            },
            valueClass match {
              case Some(clazz) => alignmentMap(clazz)
              case None => Alignment.LEFT
            }, false
          )
        }
      }.toList)
      settings
    }

    val categoryShown = context.getCategoryTreeComponent.isComponentShown
    val dataSource = categoryShown match {
      case true => new CategoryWrapperDataSource(CategoryManager.getInstance.getCategoryPathWrappers(context.getCategoryTreeComponent.getRoot),
        new EntityPropertyJRDataSource[Service](Nil))
      case false => new EntityPropertyJRDataSource[Service](asScalaBuffer(context.getEntities).toList.asInstanceOf[List[Service]])
    }

    ServiceSettings.setValue(Settings.O_DEFAULT_PRINT_SETTINGS, createDefaultPrintSettings)

    val dialog = new TablePrintDialog(ServiceSettings, EntityPropertyTranslatorManager.getTranslator(classOf[Service]), dataSource)
    if (categoryShown) {
      dialog.setGroups(context.getCategoryTreeComponent.getSelectedCategoryPath)
    }
    dialog.addTableSettingsListener(new DefaultSettingsTableSettingsListener(ServiceSettings))
    dialog.setVisible(true)
  }
}
