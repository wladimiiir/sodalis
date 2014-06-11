
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 2/25/11
 * Time: 4:52 PM
 */
package sk.magiksoft.sodalis.event.filter

import sk.magiksoft.sodalis.core.filter.element.MultiselectComboboxColumnComponent
import sk.magiksoft.sodalis.event.data.EventDataManager
import sk.magiksoft.sodalis.core.utils.DatabaseEntityUpdatedList
import java.lang.StringBuilder
import sk.magiksoft.sodalis.event.entity.{EventType, Event}

class EventTypeColumnComponent extends MultiselectComboboxColumnComponent(new DatabaseEntityUpdatedList[EventType](classOf[EventType])){
  override def translateItem(where: StringBuilder, item: AnyRef) = item match {
    case eventType:EventType => where.append(eventType.getId)
    case _ => super.translateItem(where, item)
  }
}