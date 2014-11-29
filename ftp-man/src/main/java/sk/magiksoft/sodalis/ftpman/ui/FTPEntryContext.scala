package sk.magiksoft.sodalis.ftpman.ui

import sk.magiksoft.sodalis.core.ui.AbstractTableContext
import sk.magiksoft.sodalis.ftpman.entity.FTPEntry
import sk.magiksoft.swing.ISTable
import java.awt.{GridBagLayout, GridBagConstraints, BorderLayout}
import sk.magiksoft.sodalis.person.action.PrintPersonAction
import sk.magiksoft.sodalis.core.action.{MessageAction, DefaultExportAction}
import javax.swing._
import sk.magiksoft.sodalis.ftpman.action.ScanFTPServersAction
import sk.magiksoft.sodalis.person.PersonModule
import sk.magiksoft.sodalis.ftpman.FTPManagerModule
import java.util.{List => jList}
import java.awt.event.ActionEvent
import collection.JavaConversions._
import sk.magiksoft.sodalis.core.entity.Entity
import sk.magiksoft.sodalis.core.registry.RegistryManager
import sk.magiksoft.sodalis.core.utils.UIUtils
import sk.magiksoft.sodalis.core.factory.ColorList
import sk.magiksoft.sodalis.core.SodalisApplication

/**
 * @author wladimiiir
 * @since 2011/5/6
 */

class FTPEntryContext extends AbstractTableContext(classOf[FTPEntry], new ISTable(new FTPEntryTableModel)) {
  initComponents()
  SodalisApplication.get.getStorageManager.registerComponent("ftpContext", this)

  private def initComponents() {
    val scrollPane = new JScrollPane(table)

    table.setName("ftpContext.table")
    scrollPane.setBorder(BorderFactory.createEmptyBorder)
    scrollPane.getViewport.setBackground(ColorList.SCROLLPANE_BACKGROUND)

    initCategoryTreeComponent(classOf[FTPManagerModule], scrollPane)

    setLayout(new BorderLayout)
    add(scrollPane, BorderLayout.CENTER)
    add(createToolBar, BorderLayout.NORTH)

    def createToolBar = {
      val toolbar = UIUtils.createToolBar
      val c = new GridBagConstraints {
        gridx = 0
        gridy = 0
        anchor = GridBagConstraints.WEST
        weightx = 0.0
      }

      toolbar.setLayout(new GridBagLayout)
      toolbar.setFloatable(false)

      def initButton(button: AbstractButton) = initToolbarButton(button)
      def registerAction(action: MessageAction) = {
        registerMessageAction(action)
        action
      }
      def contextClass = this.contextClass

      val categoryTreeButton = categoryTreeComponent.getShowCategoryTreeButton
      initButton(categoryTreeButton)
      categoryTreeButton.setEnabled(true)

      toolbar.add(new JButton(new ScanFTPServersAction) {
        initButton(this)
        setEnabled(true)
      }, c)
      c.gridx += 1
      toolbar.add(categoryTreeButton, c)

      c.gridx += 1
      c.weightx = 1
      toolbar.add(new JPanel {
        setOpaque(false)
      }, c)

      toolbar
    }
  }

  override def preparePopupMenu(entities: jList[_ <: Entity]) {
    val popupMenu = this.popupMenu match {
      case menu: JPopupMenu => {
        menu.removeAll
        menu
      }
      case _ => {
        this.popupMenu = new JPopupMenu
        this.popupMenu
      }
    }
    val entityList = entities.toList
    val actions = RegistryManager.getPopupActions(entityList)
    for (action <- actions) {
      popupMenu.add(new AbstractAction(action.getName(entityList)) {
        def actionPerformed(e: ActionEvent) {
          action(entityList)
        }
      })
    }
  }
}
