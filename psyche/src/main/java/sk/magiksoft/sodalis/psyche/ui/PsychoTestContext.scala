package sk.magiksoft.sodalis.psyche.ui

import sk.magiksoft.sodalis.core.ui.AbstractTableContext
import sk.magiksoft.sodalis.psyche.entity.PsychoTest
import sk.magiksoft.sodalis.core.SodalisApplication
import javax.swing.JSplitPane._
import java.awt.GridBagConstraints._
import java.awt.{GridBagLayout, GridBagConstraints, BorderLayout}
import sk.magiksoft.sodalis.core.action.{MessageAction, DefaultExportAction, DefaultImportAction}
import java.beans.{PropertyChangeEvent, PropertyChangeListener}
import sk.magiksoft.sodalis.psyche.settings.PsychoTestSettings
import sk.magiksoft.sodalis.psyche.{PsycheContextManager, PsychoTestModule}
import java.awt.event.{MouseEvent, MouseAdapter}
import javax.swing._
import sk.magiksoft.sodalis.psyche.rorschach.ui.BlotSigningDialog
import sk.magiksoft.sodalis.psyche.rorschach.entity.RorschachTest
import sk.magiksoft.swing.{HideableSplitPane, ISTable}
import sk.magiksoft.sodalis.core.factory.{IconFactory, ColorList}
import sk.magiksoft.sodalis.psyche.action.{RemovePsychoTestAction, AddPsychoTestAction}
import sk.magiksoft.sodalis.core.utils.UIUtils
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.category.ui.CategoryTreeComboBox
import sk.magiksoft.sodalis.core.ui.controlpanel.DefaultControlPanel
import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.sodalis.core.settings.Settings
import sk.magiksoft.sodalis.category.CategoryDataManager

/**
 * @author wladimiiir
 * @since 2011/5/13
 */

class PsychoTestContext extends AbstractTableContext(classOf[PsychoTest], new ISTable(new PsychoTestTableModel)) with PropertyChangeListener {
  private lazy val dialog = new BlotSigningDialog

  initComponents()
  PsychoTestSettings.addPropertyChangeListener(this)
  SodalisApplication.get.getStorageManager.registerComponent("psychoTestContext", this)

  private def initComponents() {
    val scrollPane = new JScrollPane(table)

    table.addMouseListener(new MouseAdapter {
      override def mouseClicked(e: MouseEvent) {
        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount == 2 && table.getSelectedRow >= 0) {
          UIUtils.doWithProgress(LocaleManager.getString("initializingUI"), new Runnable {
            def run() {
              dialog.init()
            }
          })
          dialog.show(table.getModel.asInstanceOf[ObjectTableModel[PsychoTest]].getObject(table.getSelectedRow).asInstanceOf[RorschachTest])
        }
      }
    })
    scrollPane.getViewport.setBackground(ColorList.SCROLLPANE_BACKGROUND)
    initCategoryTreeComponent(classOf[PsychoTestModule], scrollPane)
    controlPanel = new DefaultControlPanel("psychoTest")
    categoryTreeComboBox = new CategoryTreeComboBox(classOf[PsychoTestModule])
    categoryTreeComboBox.addChangeListener(new CategoryTreeComboBoxChangeListener(PsychoTestSettings, PsycheContextManager))

    setLayout(new BorderLayout)
    add(new HideableSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, controlPanelPrivate.getControlComponent) {
      setOpaque(false)
      setLeftText(LocaleManager.getString("psychoTestList"))
      setRightText(LocaleManager.getString("details"))
      setDividerLocation(300)
    }, BorderLayout.CENTER)
    add(createToolBar, BorderLayout.NORTH)
    if (PsycheContextManager.getFilterPanel ne null) add(PsycheContextManager.getFilterPanel, BorderLayout.EAST)

    currentObjectChanged()

    def controlPanelPrivate = controlPanel

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
      toolBar.add(createButton(new AddPsychoTestAction), c)
      c.gridx += 1
      toolBar.add(createButton(new RemovePsychoTestAction), c)
      c.gridx += 1
      toolBar.add(createButton(new DefaultImportAction(classOf[PsychoTest])), c)
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
      val ids = PsychoTestSettings.getValue(Settings.O_SELECTED_CATEGORIES).asInstanceOf[java.util.List[java.lang.Long]]
      categoryTreeComboBox.setSelectedCategories(CategoryDataManager.getInstance.getCategories(ids))
    }
  }
}
