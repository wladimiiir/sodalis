package sk.magiksoft.sodalis.form.ui

import sk.magiksoft.sodalis.form.entity.Form
import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, Entity}
import java.util.List
import sk.magiksoft.sodalis.core.ui.{AbstractTableContext, AbstractContext}
import sk.magiksoft.swing.{HideableSplitPane, ISTable}
import swing.{ScrollPane, Swing, Button}
import sk.magiksoft.sodalis.core.factory.ColorList
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.utils.UIUtils
import sk.magiksoft.sodalis.core.utils.Conversions._
import java.awt.{GridBagLayout, GridBagConstraints, BorderLayout}
import javax.swing.{JPanel, BorderFactory, JSplitPane, JScrollPane}
import sk.magiksoft.sodalis.form.settings.FormSettings
import sk.magiksoft.sodalis.form.{FormContextManager, FormModule}
import javax.swing.event.{TreeSelectionListener, TreeSelectionEvent}
import sk.magiksoft.sodalis.category.ui.{CategoryTreeComponent, CategoryTreeComboBox}
import sk.magiksoft.sodalis.core.SodalisApplication
import java.beans.{PropertyChangeEvent, PropertyChangeListener}
import sk.magiksoft.sodalis.category.CategoryDataManager
import sk.magiksoft.sodalis.form.action.{RemoveFormAction, AddFormAction}
import sk.magiksoft.sodalis.core.action.MessageAction
import sk.magiksoft.sodalis.core.settings.Settings

/**
 * @author wladimiiir
 * @since 2010/8/5
 */

class FormContext extends AbstractTableContext(classOf[Form], new ISTable(new FormTableModel)) with PropertyChangeListener {
  private val btnAdd = new Button(new AddFormAction)
  private val btnRemove = new Button(new RemoveFormAction)

  initComponents()
  SodalisApplication.get.getStorageManager.registerComponent("formContext", this)
  FormSettings.addPropertyChangeListener(this)
  setupButtons()

  private def initComponents() {
    val scrollPane = new JScrollPane(table)
    scrollPane.getViewport.setBackground(ColorList.SCROLLPANE_BACKGROUND)
    scrollPane.setBorder(BorderFactory.createEmptyBorder)

    table.setName("formContext.table")
    controlPanel = new FormControlPanel

    val controlPanelComponent = controlPanel.getControlComponent
    val splitPane = new HideableSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, controlPanelComponent) {
      setName("formContext.splitPane")
      setOpaque(false)
      setLeftText(LocaleManager.getString("documents"))
      setRightText(LocaleManager.getString("details"))
      setDividerLocation(300)
    }

    categoryTreeComboBox = new CategoryTreeComboBox(classOf[FormModule])
    categoryTreeComboBox.addChangeListener(new CategoryTreeComboBoxChangeListener(FormSettings, FormContextManager))
    initCategoryTreeComponent(classOf[FormModule], scrollPane)

    setLayout(new BorderLayout)

    add(createToolBar, BorderLayout.NORTH)
    add(splitPane, BorderLayout.CENTER)
  }

  private def createToolBar = {
    val c = new GridBagConstraints
    val toolBar = UIUtils.createToolBar

    toolBar.setLayout(new GridBagLayout)
    toolBar.setFloatable(false)

    initToolbarButton(btnAdd.peer)
    initToolbarButton(btnRemove.peer)
    initToolbarButton(categoryTreeComponent.getShowCategoryTreeButton)
    categoryTreeComponent.getShowCategoryTreeButton.setEnabled(true)

    c.gridx = 0
    c.gridy = 0
    c.anchor = GridBagConstraints.WEST
    toolBar.add(btnAdd, c)
    c.gridx += 1
    toolBar.add(btnRemove, c)
    c.gridx += 1
    toolBar.add(categoryTreeComponent.getShowCategoryTreeButton, c)
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


  override def currentObjectChanged() {
    setupButtons
  }

  private def setupButtons() {
    val selectedObjects: List[_] = getSelectedEntities
    var actionMessage = (btnAdd.peer.getAction.asInstanceOf[MessageAction]).getActionMessage(selectedObjects)
    btnAdd.setEnabled(actionMessage.isAllowed)
    btnAdd.setToolTipText(actionMessage.getMessage)
    actionMessage = (btnRemove.peer.getAction.asInstanceOf[MessageAction]).getActionMessage(selectedObjects)
    btnRemove.setEnabled(actionMessage.isAllowed)
    btnRemove.setToolTipText(actionMessage.getMessage)
    //    actionMessage = (btnPrintMembers.getAction.asInstanceOf[MessageAction]).getActionMessage(selectedObjects)
    //    btnPrintMembers.setEnabled(actionMessage.isAllowed)
    //    btnPrintMembers.setToolTipText(actionMessage.getMessage)
    //    actionMessage = (btnExportMembers.getAction.asInstanceOf[MessageAction]).getActionMessage(selectedObjects)
    //    btnExportMembers.setEnabled(actionMessage.isAllowed)
    //    btnExportMembers.setToolTipText(actionMessage.getMessage)
    //    actionMessage = (btnImportMembers.getAction.asInstanceOf[MessageAction]).getActionMessage(selectedObjects)
    //    btnImportMembers.setEnabled(actionMessage.isAllowed)
    //    btnImportMembers.setToolTipText(actionMessage.getMessage)
  }

  def propertyChange(evt: PropertyChangeEvent) = {
    if (evt.getPropertyName.equals(Settings.O_SELECTED_CATEGORIES)) {
      categoryTreeComboBox.setSelectedCategories(CategoryDataManager.getInstance.getCategories(FormSettings.getValue(Settings.O_SELECTED_CATEGORIES).asInstanceOf[List[java.lang.Long]]))
    }
  }
}
