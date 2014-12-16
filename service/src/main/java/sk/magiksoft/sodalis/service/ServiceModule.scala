package sk.magiksoft.sodalis.service

import java.util.ResourceBundle

import entity.property.ServicePropertyTranslator
import entity.Service
import sk.magiksoft.sodalis.core.module.{DynamicModule, AbstractModule, ModuleDescriptor}
import sk.magiksoft.sodalis.core.factory.IconFactory
import sk.magiksoft.sodalis.core.locale.LocaleManager
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslatorManager

/**
 * @author wladimiiir
 * @since 2011/3/10
 */
@DynamicModule
class ServiceModule extends AbstractModule {
  private val bundleBaseName = "sk.magiksoft.sodalis.service.locale.service"
  private lazy val moduleDescriptor = new ModuleDescriptor(IconFactory.getInstance.getIcon("services").asInstanceOf[ImageIcon],
    ResourceBundle.getBundle(bundleBaseName).getString("services"))

  override def startUp(): Unit = {
    LocaleManager.registerBundleBaseName(bundleBaseName)
    EntityPropertyTranslatorManager.registerTranslator(classOf[Service], new ServicePropertyTranslator)
  }

  def getDataListener = ServiceContextManager

  def getContextManager = ServiceContextManager

  def getModuleDescriptor = moduleDescriptor
}
