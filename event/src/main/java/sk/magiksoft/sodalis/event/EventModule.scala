package sk.magiksoft.sodalis.event

import entity.property.EventPropertyTranslator
import entity.{Event, EventEntityData, EventHistoryData}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.{ModuleDescriptor, AbstractModule}
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.core.factory.{IconFactory, EntityFactory}
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslatorManager
import sk.magiksoft.sodalis.person.entity.Person

 /**
  * @author wladimiiir
  * @since 2011/2/26
  */
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
