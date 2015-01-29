package sk.magiksoft.sodalis.person.ui

import sk.magiksoft.sodalis.person.PersonModule
import sk.magiksoft.sodalis.core.ui.AbstractTableContext
import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.sodalis.person.entity.Person
import sk.magiksoft.swing.{HideableSplitPane, ISTable}
import sk.magiksoft.sodalis.core.controlpanel.ControlPanel
import sk.magiksoft.sodalis.core.factory.ColorList
import sk.magiksoft.sodalis.core.settings.Settings
import sk.magiksoft.sodalis.core.utils.UIUtils
import sk.magiksoft.sodalis.person.action.{PrintPersonAction, RemovePersonAbstractAction, AddPersonAbstractAction}
import javax.swing._
import java.awt.{BorderLayout, GridBagLayout, GridBagConstraints}
import sk.magiksoft.sodalis.core.context.ContextManager
import sk.magiksoft.sodalis.category.ui.CategoryTreeComboBox
import sk.magiksoft.sodalis.core.action.{MessageAction, DefaultExportAction, DefaultImportAction}
import sk.magiksoft.sodalis.category.CategoryDataManager
import java.beans.{PropertyChangeListener, PropertyChangeEvent}
import sk.magiksoft.sodalis.core.SodalisApplication

/**
 * @author wladimiiir
 * @since 2010/12/16
 */

abstract class AbstractPersonContext(contextManager: ContextManager, tableModel: ObjectTableModel[Person])
  extends AbstractTableContext(classOf[Person], new ISTable(tableModel))
  with PropertyChangeListener {

  initComponents()
  getSettings.addPropertyChangeListener(this)
  SodalisApplication.get.getStorageManager.registerComponent(contextManager.getClass.getSimpleName, this)

  private def initComponents() {
    val scrollPane = new JScrollPane(table)

    scrollPane.setBorder(BorderFactory.createEmptyBorder)
    scrollPane.getViewport.setBackground(ColorList.SCROLLPANE_BACKGROUND)

    initCategoryTreeComponent(getModule.getClass.asSubclass(classOf[PersonModule]), scrollPane)
    categoryTreeComboBox = new CategoryTreeComboBox(getModule.getClass.asInstanceOf[Class[PersonModule]])
    categoryTreeComboBox.addChangeListener(new CategoryTreeComboBoxChangeListener(getSettings, contextManager))

    setLayout(new BorderLayout)
    add(createToolbar, BorderLayout.NORTH)
    createControlPanel match {
      case Some(controlPanel) =>
        this.controlPanel = controlPanel
        add(new HideableSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, controlPanel.getControlComponent) {
          setDividerLocation(300)
        }, BorderLayout.CENTER)
      case None =>
        add(scrollPane, BorderLayout.CENTER)
    }

    if (contextManager.getFilterPanel != null) {
      add(contextManager.getFilterPanel, BorderLayout.EAST)
    }
  }

  def propertyChange(evt: PropertyChangeEvent) {
    if (evt.getPropertyName == Settings.O_SELECTED_CATEGORIES) {
      val ids = getSettings.getValue(Settings.O_SELECTED_CATEGORIES).asInstanceOf[java.util.List[java.lang.Long]]
      categoryTreeComboBox.setSelectedCategories(CategoryDataManager.getInstance.getCategories(ids))
    }
  }

  private def createToolbar = {
    val toolbar = UIUtils.createToolBar
    val c = new GridBagConstraints {
      gridx = 0
      gridy = 0
      anchor = GridBagConstraints.WEST
      weightx = 0.0
    }
    val categoryTreeButton = categoryTreeComponent.getShowCategoryTreeButton

    toolbar.setLayout(new GridBagLayout)
    toolbar.setFloatable(false)

    def initButton(button: AbstractButton) = initToolbarButton(button)
    def registerAction(action: MessageAction) = {
      registerMessageAction(action)
      action
    }
    def contextClass = this.contextClass

    initButton(categoryTreeButton)
    categoryTreeButton.setEnabled(true)
    toolbar.add(new JButton(registerAction(createAddPersonAction)) {
      initButton(this)
      setEnabled(true)
    }, c)
    c.gridx += 1
    toolbar.add(new JButton(registerAction(createRemovePersonAction)) {
      initButton(this)
    }, c)
    c.gridx += 1
    toolbar.add(new JButton(new PrintPersonAction(this)) {
      initButton(this)
      setEnabled(true)
    }, c)
    c.gridx += 1
    toolbar.add(new JButton(registerAction(new DefaultImportAction(contextClass))) {
      initButton(this)
      setEnabled(true)
    }, c)
    c.gridx += 1
    toolbar.add(new JButton(registerAction(new DefaultExportAction(this))) {
      initButton(this)
      setEnabled(true)
    }, c)
    c.gridx += 1
    toolbar.add(categoryTreeButton, c)
    c.gridx += 1
    c.weightx = 1.0
    toolbar.add(new JPanel {
      setOpaque(false)
    }, c)
    c.gridx += 1;
    c.weightx = 0.0
    toolbar.add(categoryTreeComboBox, c)

    toolbar
  }

  protected def createAddPersonAction: AddPersonAbstractAction

  protected def createRemovePersonAction: RemovePersonAbstractAction

  protected def getModule: PersonModule

  protected def createControlPanel: Option[ControlPanel]

  def getSettings: Settings
}
