
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


/*
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 2/26/11
 * Time: 12:25 PM
 */
package sk.magiksoft.sodalis.event

import entity.property.EventPropertyTranslator
import entity.{Event, EventEntityData, EventHistoryData}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.AbstractModule
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.core.factory.EntityFactory

class EventModule extends AbstractModule {
  val moduleDescriptor = new ModuleDescriptor(IconFactory.getInstance.getIcon("eventsModule").asInstanceOf[ImageIcon], LocaleManager.getString("events"))

  LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.event.locale.event")
  EntityFactory.getInstance.registerEntityProperties(classOf[Event], classOf[EventHistoryData])
  EntityFactory.getInstance.registerEntityProperties(classOf[Person], classOf[EventEntityData])
  EntityPropertyTranslatorManager.registerTranslator(classOf[Event], new EventPropertyTranslator)

  def getModuleDescriptor = moduleDescriptor

  def getContextManager = EventContextManager

  def getDataListener = EventContextManager
}