package sk.magiksoft.sodalis.folkensemble.member.ui

import sk.magiksoft.sodalis.folkensemble.member.table.MemberTableModel
import sk.magiksoft.sodalis.folkensemble.member.settings.MemberSettings
import sk.magiksoft.sodalis.folkensemble.member.{MemberModule, MemberContextManager}
import sk.magiksoft.sodalis.folkensemble.member.action.{RemoveMemberAction, AddMemberAction}
import java.util.{List => jList}
import javax.swing.{AbstractAction, JPopupMenu}
import java.awt.event.ActionEvent
import sk.magiksoft.sodalis.core.entity.Entity
import sk.magiksoft.sodalis.core.registry.RegistryManager
import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.person.ui.AbstractPersonContext
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2011/3/28
 */

class MemberContext extends AbstractPersonContext(MemberContextManager, new MemberTableModel) {
  def getSettings = MemberSettings.getInstance

  protected def createControlPanel = Option(new MemberControlPanel)

  protected def getModule = SodalisApplication.get.getModuleManager.getModuleByClass(classOf[MemberModule])

  protected def createRemovePersonAction = new RemoveMemberAction

  protected def createAddPersonAction = new AddMemberAction(this)

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
