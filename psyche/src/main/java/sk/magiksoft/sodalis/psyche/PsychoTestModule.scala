package sk.magiksoft.sodalis.psyche

import sk.magiksoft.sodalis.core.module.{DynamicModule, ModuleDescriptor, AbstractModule}
import sk.magiksoft.sodalis.core.locale.LocaleManager

/**
 * @author wladimiiir
 * @since 2011/5/13
 */
@DynamicModule
class PsychoTestModule extends AbstractModule {
  private lazy val descriptor = new ModuleDescriptor(null, LocaleManager.getString("psychoTests"))

  LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.psyche.locale.psyche")

  def getDataListener = PsycheContextManager

  def getContextManager = PsycheContextManager

  def getModuleDescriptor = descriptor
}
