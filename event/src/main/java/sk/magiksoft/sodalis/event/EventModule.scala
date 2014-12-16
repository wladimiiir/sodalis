package sk.magiksoft.sodalis.event

import java.util.ResourceBundle

import entity.property.EventPropertyTranslator
import entity.{Event, EventEntityData, EventHistoryData}
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.module.{DynamicModule, ModuleDescriptor, AbstractModule}
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.core.factory.{IconFactory, EntityFactory}
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslatorManager
import sk.magiksoft.sodalis.person.entity.Person

/**
 * @author wladimiiir
 * @since 2011/2/26
 */
@DynamicModule
class EventModule extends AbstractModule {
  private val bundleBaseName = "sk.magiksoft.sodalis.event.locale.event"
  private lazy val moduleDescriptor = new ModuleDescriptor(IconFactory.getInstance.getIcon("eventsModule").asInstanceOf[ImageIcon],
    ResourceBundle.getBundle(bundleBaseName).getString("events"))

  override def startUp(): Unit = {
    LocaleManager.registerBundleBaseName(bundleBaseName)
    EntityFactory.getInstance.registerEntityProperties(classOf[Event], classOf[EventHistoryData])
    EntityFactory.getInstance.registerEntityProperties(classOf[Person], classOf[EventEntityData])
    EntityPropertyTranslatorManager.registerTranslator(classOf[Event], new EventPropertyTranslator)
  }

  def getModuleDescriptor = moduleDescriptor

  def getContextManager = EventContextManager

  def getDataListener = EventContextManager
}
