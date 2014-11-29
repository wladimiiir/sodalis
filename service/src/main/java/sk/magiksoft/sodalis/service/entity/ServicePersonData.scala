package sk.magiksoft.sodalis.service.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import sk.magiksoft.sodalis.person.entity.PersonData
import collection.mutable.ListBuffer
import collection.JavaConversions._
import java.util.{LinkedList, List => jList}
import scala.beans.BeanProperty
import java.util

/**
 * @author wladimiiir
 * @since 2011/3/17
 */

class ServicePersonData extends AbstractDatabaseEntity with PersonData {
  @BeanProperty var personServices: jList[PersonService] = new util.LinkedList[PersonService]

  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case spd: ServicePersonData if spd ne this => {
        personServices = spd.personServices
      }
      case _ =>
    }
  }
}
