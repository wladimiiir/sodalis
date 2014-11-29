package sk.magiksoft.sodalis.service.ui

import sk.magiksoft.sodalis.core.ui.AbstractTableContext
import sk.magiksoft.sodalis.service.entity.Service
import sk.magiksoft.sodalis.core.ui.controlpanel.DefaultControlPanel
import sk.magiksoft.swing.{HideableSplitPane, ISTable}
import sk.magiksoft.sodalis.service.{ServiceContextManager, ServiceModule}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import java.awt.{GridBagLayout, GridBagConstraints, BorderLayout}
import swing.Action
import sk.magiksoft.sodalis.core.factory.{IconFactory, ColorList}
import sk.magiksoft.sodalis.service.action.{ServicePrintAction, RemoveServiceAction, AddServiceAction}
import javax.swing._
import sk.magiksoft.sodalis.category.ui.CategoryTreeComboBox
import sk.magiksoft.sodalis.core.action.{MessageAction, DefaultExportAction, DefaultImportAction}
import sk.magiksoft.sodalis.core.utils.UIUtils
import sk.magiksoft.sodalis.settings.ServiceSettings
import sk.magiksoft.sodalis.core.settings.Settings
import sk.magiksoft.sodalis.category.CategoryDataManager
import java.beans.{PropertyChangeListener, PropertyChangeEvent}
import sk.magiksoft.sodalis.core.SodalisApplication

/*
* Copyright (c) 2011
*/

/**
 * @author wladimiiir
 * @since 2011/3/10
 */

class ServiceContext extends AbstractTableContext(classOf[Service], new ISTable(new ServiceTableModel)) with PropertyChangeListener {

  initComponents()
  ServiceSettings.addPropertyChangeListener(this)
  SodalisApplication.get.getStorageManager.registerComponent("serviceContext", this)

  private def controlPanelPrivate = controlPanel

  private def initComponents() {
    val scrollPane = new JScrollPane(table)

    scrollPane.getViewport.setBackground(ColorList.SCROLLPANE_BACKGROUND)
    initCategoryTreeComponent(classOf[ServiceModule], scrollPane)
    controlPanel = new DefaultControlPanel("service")
    categoryTreeComboBox = new CategoryTreeComboBox(classOf[ServiceModule])
    categoryTreeComboBox.addChangeListener(new CategoryTreeComboBoxChangeListener(ServiceSettings, ServiceContextManager))

    setLayout(new BorderLayout)
    add(new HideableSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, controlPanelPrivate.getControlComponent) {
      setOpaque(false)
      setLeftText(LocaleManager.getString("serviceList"))
      setRightText(LocaleManager.getString("details"))
      setDividerLocation(300)
    }, BorderLayout.CENTER)
    add(createToolBar, BorderLayout.NORTH)
    if (ServiceContextManager.getFilterPanel ne null) add(ServiceContextManager.getFilterPanel, BorderLayout.EAST)

    currentObjectChanged()

    def createToolBar = {
      val toolBar = UIUtils.createToolBar
      val c = new GridBagConstraints {
        gridx = 0
        gridy = 0
      }

      def createButton(action: MessageAction) = {
        val button = new JButton(action)
        registerMessageAction(action)
        initToolbarButton(button)
        button.setEnabled(true)
        button
      }
      toolBar.setLayout(new GridBagLayout)
      toolBar.add(createButton(new AddServiceAction), c)
      c.gridx += 1
      toolBar.add(createButton(new RemoveServiceAction), c)
      c.gridx += 1
      toolBar.add(createButton(new ServicePrintAction(this)), c)
      c.gridx += 1
      toolBar.add(createButton(new DefaultImportAction(classOf[Service])), c)
      c.gridx += 1
      toolBar.add(createButton(new DefaultExportAction(this)), c)
      c.gridx += 1
      toolBar.add(categoryTreeComponent.getShowCategoryTreeButton, c)
      initToolbarButton(categoryTreeComponent.getShowCategoryTreeButton)
      categoryTreeComponent.getShowCategoryTreeButton.setEnabled(true)
      c.gridx += 1
      c.weightx = 1.0
      toolBar.add(new JPanel {
        setOpaque(false)
      }, c)
      c.gridx += 1
      c.weightx = 0.0
      toolBar.add(categoryTreeComboBox, c)

      toolBar
    }
  }

  def propertyChange(evt: PropertyChangeEvent) {
    if (evt.getPropertyName == Settings.O_SELECTED_CATEGORIES) {
      val ids = ServiceSettings.getValue(Settings.O_SELECTED_CATEGORIES).asInstanceOf[java.util.List[java.lang.Long]]
      categoryTreeComboBox.setSelectedCategories(CategoryDataManager.getInstance.getCategories(ids))
    }
  }
}
