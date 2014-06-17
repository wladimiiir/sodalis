/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.service.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import sk.magiksoft.sodalis.person.entity.PersonData
import collection.mutable.ListBuffer
import collection.JavaConversions._
import java.util.{LinkedList, List => jList}
import scala.beans.BeanProperty

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 3/17/11
 * Time: 10:32 PM
 * To change this template use File | Settings | File Templates.
 */

class ServicePersonData extends AbstractDatabaseEntity with PersonData {
  @BeanProperty var personServices: jList[PersonService] = new LinkedList[PersonService]

  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case spd: ServicePersonData if spd ne this => {
        personServices = spd.personServices
      }
      case _ =>
    }
  }
}