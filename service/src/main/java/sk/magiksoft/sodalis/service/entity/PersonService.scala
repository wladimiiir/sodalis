/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.service.entity

import sk.magiksoft.sodalis.core.entity.{DatabaseEntity, AbstractDatabaseEntity}
import java.util.Calendar
import scala.beans.BeanProperty

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 3/17/11
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */

class PersonService(@BeanProperty var service: Service = null, @BeanProperty var date: Calendar = Calendar.getInstance) extends AbstractDatabaseEntity {
  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case ps: PersonService if ps ne this => {
        service = ps.service
        date = ps.date
      }
      case _ =>
    }
  }
}