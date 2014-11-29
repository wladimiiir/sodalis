package sk.magiksoft.sodalis.event.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import scala.beans.BeanProperty
import sk.magiksoft.sodalis.person.entity.PersonWrapper
import sk.magiksoft.sodalis.core.locale.LocaleManager

/**
 * @author wladimiiir
 * @since 2010/12/2
 */

class Attendee extends AbstractDatabaseEntity {
  @BeanProperty var personWrapper = new PersonWrapper
  @BeanProperty var attendeeType = LocaleManager.getString("participant")

  def updateFrom(entity: DatabaseEntity) = entity match {
    case attendee: Attendee => {
      personWrapper.updateFrom(attendee.personWrapper)
      attendeeType = attendee.attendeeType
    }
    case _ =>
  }
}
