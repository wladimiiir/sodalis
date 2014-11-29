package sk.magiksoft.sodalis.service

import entity.property.ServicePropertyTranslator
import entity.Service
import sk.magiksoft.sodalis.core.module.{AbstractModule, ModuleDescriptor}
import sk.magiksoft.sodalis.core.factory.IconFactory
import sk.magiksoft.sodalis.core.locale.LocaleManager
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslatorManager

/**
 * @author wladimiiir
 * @since 2011/3/10
 */

class ServiceModule extends AbstractModule {
  LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.service.locale.service")
  EntityPropertyTranslatorManager.registerTranslator(classOf[Service], new ServicePropertyTranslator)

  def getDataListener = ServiceContextManager

  def getContextManager = ServiceContextManager

  def getModuleDescriptor = new ModuleDescriptor(IconFactory.getInstance.getIcon("services").asInstanceOf[ImageIcon], LocaleManager.getString("services"))
}
