package sk.magiksoft.sodalis.service.ui

import sk.magiksoft.sodalis.core.ui.controlpanel.{AbstractInfoPanel, InfoPanelPublisher}
import javax.swing.JScrollPane
import sk.magiksoft.sodalis.service.data.ServiceDataManager
import sk.magiksoft.sodalis.service.entity.{ServicePersonData, PersonService}
import swing.Swing._
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.table.ObjectTableModel
import sk.magiksoft.sodalis.person.entity.Person
import sk.magiksoft.sodalis.core.factory.ColorList
import sk.magiksoft.swing.ISTable
import org.hibernate.Hibernate

/**
 * @author wladimiiir
 * @since 2011/3/17
 */

class PersonServiceInfoPanel extends AbstractInfoPanel with InfoPanelPublisher {
  private lazy val model = new PersonServiceTableModel
  private var person: Option[Person] = None

  def createLayout = new JScrollPane(new ISTable(model)) {
    setPreferredSize((100, 100))
    getViewport.setBackground(ColorList.SCROLLPANE_BACKGROUND)
  }

  override def isWizardSupported = false

  def initData() {
    person match {
      case Some(person) if !initialized =>
        val data = person.getPersonData(classOf[ServicePersonData]) match {
          case spd: ServicePersonData if !Hibernate.isInitialized(spd.personServices) =>
            spd.personServices = ServiceDataManager.initialize(spd.personServices)
            spd
          case spd: ServicePersonData if Hibernate.isInitialized(spd.personServices) => spd
          case _ =>
            val newData = new ServicePersonData
            person.putPersonData(newData)
            ServiceDataManager.updateDatabaseEntity(person)
            newData
        }

        model.setObjects(data.personServices)
        initialized = true
      case _ =>
    }
  }

  def setupPanel(entity: Any) {
    entity match {
      case person: Person => {
        this.person = Option(person)
        initialized = false
      }
      case _ =>
    }
  }

  def setupObject(entity: Any) {}

  def getPanelName = LocaleManager.getString("performedServices")

  class PersonServiceTableModel extends ObjectTableModel[PersonService](Array(
    LocaleManager.getString("dateTime"),
    LocaleManager.getString("serviceName"),
    LocaleManager.getString("servicePrice")
  )) {

    def getValueAt(rowIndex: Int, columnIndex: Int) = {
      val service = getObject(rowIndex)
      columnIndex match {
        case 0 => ObjectTableModel.DATE_TIME_FORMAT.format(service.date)
        case 1 => service.service.name
        case 2 => service.service.price.formattedPrice(true)
      }
    }
  }

}
