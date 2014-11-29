package sk.magiksoft.sodalis.psyche.ui

import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.psyche.entity.PsychoTest
import com.toedter.calendar.{JDateChooser, JTextFieldDateEditor}
import sk.magiksoft.swing.DirtyLockable
import scala.swing.Swing._
import javax.swing._
import java.awt._
import sk.magiksoft.sodalis.person.ui.PersonalDataInfoPanel
import org.jdesktop.jxlayer.JXLayer

/**
 * @author wladimiiir
 * @since 2011/6/24
 */

class PsychoTestInfoPanel extends AbstractInfoPanel {
  private lazy val nameUI = new JTextField {
    setEditable(false)
    setBackground(Color.WHITE)
    setPreferredSize((200, 21))
    getDocument.addDocumentListener(documentListener)
  }
  private lazy val dateUI = new JDateChooser(new JTextFieldDateEditor("dd.MM.yyyy HH:mm", "##.##.#### ##:##", ' ')) {
    setPreferredSize((200, 21))
    addPropertyChangeListener("date", propertyChangeListener)
  }
  private lazy val lock = new DirtyLockable
  private lazy val personInfoPanel = new PersonalDataInfoPanel(false, LocaleManager.getString("testedPerson")) {
    initLayout()
  }

  private var psychoTest: Option[PsychoTest] = None

  protected def createLayout() = {
    val c = new GridBagConstraints()
    val panel = new JPanel(new BorderLayout)
    val rightPanel = new JPanel(new BorderLayout)
    val centerPanel = new JPanel(new GridBagLayout)

    c.gridx = 0
    c.gridy = 0
    c.anchor = GridBagConstraints.EAST
    c.insets = new Insets(10, 10, 0, 0)
    centerPanel.add(new JLabel(LocaleManager.getString("testName")), c)

    c.gridx += 1
    c.anchor = GridBagConstraints.WEST
    c.insets = new Insets(10, 3, 0, 10)
    centerPanel.add(nameUI, c)

    c.gridx = 0
    c.gridy += 1
    c.anchor = GridBagConstraints.EAST
    c.insets = new Insets(2, 10, 10, 0)
    centerPanel.add(new JLabel(LocaleManager.getString("date")), c)

    c.gridx += 1
    c.anchor = GridBagConstraints.WEST
    c.insets = new Insets(2, 3, 10, 10)
    centerPanel.add(dateUI, c)

    rightPanel.add(new JXLayer[JComponent](personInfoPanel, lock), BorderLayout.CENTER)
    lock.setLocked(true)
    lock.setLockedCursor(Cursor.getDefaultCursor)

    panel.add(centerPanel, BorderLayout.CENTER)
    panel.add(rightPanel, BorderLayout.EAST)
    panel
  }

  def initData() {
    psychoTest match {
      case Some(test) if !initialized => {
        nameUI.setText(test.name)
        dateUI.setCalendar(test.date)
        personInfoPanel.setupPanel(test.testedSubject.getPerson)
        personInfoPanel.initData()

        lock.setDirty(true)
        initialized = true
      }
      case _ =>
    }
  }

  def setupPanel(entity: AnyRef) {
    entity match {
      case test: PsychoTest => {
        psychoTest = Option(test)
        initialized = false
      }
      case _ =>
    }
  }

  def setupObject(entity: AnyRef) {
    entity match {
      case test: PsychoTest if initialized => {
        test.date = dateUI.getCalendar
      }
      case _ =>
    }
  }

  def getPanelName = LocaleManager.getString("basicInfo")
}
