
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.person.entity.property

import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslator
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.person.entity.{PrivatePersonData, Person}
import collection.JavaConversions
import sk.magiksoft.sodalis.core.logger.LogInfoIgnored

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Oct 17, 2010
 * Time: 1:15:10 PM
 * To change this template use File | Settings | File Templates.
 */

class PersonPropertyTranslator extends EntityPropertyTranslator[Person] {
  def getTranslations = List(
    new EntityTranslation[Person]("firstName", LocaleManager.getString("firstName"), p => Option(p.getFirstName)) with LogInfoIgnored,
    new EntityTranslation[Person]("lastName", LocaleManager.getString("lastName"), p => Option(p.getLastName)) with LogInfoIgnored,
    EntityTranslation("fullName", LocaleManager.getString("lastNameFirstName"), p => Option(p.getFullName(false))),
    EntityTranslation("titles", LocaleManager.getString("titles"), p => Option(p.getTitles)),
    EntityTranslation("sex", LocaleManager.getString("sex"), p => Option(p.getSex.toString)),
    EntityTranslation("birthDate", LocaleManager.getString("birthDate"),
      p => Option(DateFormat.format(p.getPersonData(classOf[PrivatePersonData]).getBirthDate.getTime))),
    EntityTranslation("address", LocaleManager.getString("address"),
      p => Option(p.getPersonData(classOf[PrivatePersonData]).getAddresses.get(0).toString)),
    EntityTranslation("contacts", LocaleManager.getString("contacts"),
      p => Option(JavaConversions.asScalaBuffer(p.getPersonData(classOf[PrivatePersonData]).getContacts)
        .map {
        c => c.getContactType + ": " + c.getContact
      }.mkString(", ")))
  )
}