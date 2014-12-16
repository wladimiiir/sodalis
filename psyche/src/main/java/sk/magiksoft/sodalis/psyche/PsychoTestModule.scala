package sk.magiksoft.sodalis.psyche

import java.util.ResourceBundle

import sk.magiksoft.sodalis.core.module.{DynamicModule, ModuleDescriptor, AbstractModule}
import sk.magiksoft.sodalis.core.locale.LocaleManager

/**
 * @author wladimiiir
 * @since 2011/5/13
 */
@DynamicModule
class PsychoTestModule extends AbstractModule {
  private val bundleBaseName = "sk.magiksoft.sodalis.psyche.locale.psyche"
  private lazy val descriptor = new ModuleDescriptor(null, ResourceBundle.getBundle(bundleBaseName).getString("psychoTests"))

  override def getDataListener = PsycheContextManager

  override def getContextManager = PsycheContextManager


  override def getModuleDescriptor = descriptor

  override def startUp(): Unit = {
    LocaleManager.registerBundleBaseName(bundleBaseName)
  }

  override def plugInModule(classLoader: ClassLoader): Unit = {
    
  }
}
