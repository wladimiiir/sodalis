
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.event.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import scala.beans.BeanProperty
import sk.magiksoft.sodalis.person.entity.PersonWrapper
import sk.magiksoft.sodalis.core.locale.LocaleManager

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 12/2/10
 * Time: 10:10 PM
 * To change this template use File | Settings | File Templates.
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