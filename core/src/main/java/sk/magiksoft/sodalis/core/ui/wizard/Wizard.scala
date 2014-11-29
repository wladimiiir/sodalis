package sk.magiksoft.sodalis.core.ui.wizard

import java.awt.{BorderLayout, Frame}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.factory.IconFactory
import javax.swing.{JPanel, JDialog}
import sk.magiksoft.sodalis.core.utils.UIUtils
import swing._
import event.Event

/**
 * @author wladimiiir
 * @since 2011/2/18
 */
class Wizard(owner: Frame, title: String, page: Page) extends JDialog(owner, title, true) with Publisher {
  private var currentPage = page
  private val previousButton = new Button(previousAction)
  private val nextButton = new Button
  private val centerPanel = new JPanel(new BorderLayout)

  initComponents
  UIUtils.makeISDialog(this)

  private def initComponents = {
    val buttonPanel = new FlowPanel(FlowPanel.Alignment.Right)(
      previousButton,
      nextButton,
      new Separator,
      new Button(new Action(LocaleManager.getString("cancelAction")) {
        icon = IconFactory.getInstance.getIcon("cancel")

        def apply() = {
          setVisible(false)
          publish(new WizardCancelled(Wizard.this))
        }
      })
    )

    setLayout(new BorderLayout)
    add(centerPanel, BorderLayout.CENTER)
    add(buttonPanel.peer, BorderLayout.SOUTH)
  }

  private def refresh = {
    centerPanel.removeAll
    centerPanel.add(currentPage.getComponent.peer, BorderLayout.CENTER)
    centerPanel.revalidate
    centerPanel.repaint()
    previousButton.enabled = currentPage.getPreviousPage.isDefined
    nextButton.action = currentPage.getNextPage match {
      case Some(_) => nextAction
      case None => finishAction
    }
  }

  def showWizard {
    currentPage = page
    refresh
    setVisible(true)
  }

  object previousAction extends Action(LocaleManager.getString("previous")) {
    icon = IconFactory.getInstance.getIcon("previous")

    def apply = {
      currentPage = currentPage.getPreviousPage.get
      refresh
    }
  }

  object nextAction extends Action(LocaleManager.getString("next")) {
    icon = IconFactory.getInstance.getIcon("next")

    def apply = {
      currentPage = currentPage.getNextPage.get
      refresh
    }
  }

  object finishAction extends Action(LocaleManager.getString("finish")) {
    icon = IconFactory.getInstance.getIcon("finish")

    def apply = {
      setVisible(false)
      publish(new WizardFinished(Wizard.this))
    }
  }

}

case class WizardFinished(wizard: Wizard) extends Event

case class WizardCancelled(wizard: Wizard) extends Event
