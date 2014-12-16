package sk.magiksoft.sodalis.form

import java.util.ResourceBundle

import sk.magiksoft.sodalis.core.locale.LocaleManager
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.core.module.{DynamicModule, ModuleDescriptor, AbstractModule}
import sk.magiksoft.sodalis.core.factory.IconFactory

/**
 * @author wladimiiir
 * @since 2010/4/13
 */
@DynamicModule
class FormModule extends AbstractModule {
  private val bundleBaseName = "sk.magiksoft.sodalis.form.locale.form"
  private lazy val moduleDescriptor = new ModuleDescriptor(IconFactory.getInstance().getIcon("eventsModule") match {
    case e: ImageIcon => e
    case _ => null
  }, ResourceBundle.getBundle(bundleBaseName).getString("forms"))

  override def startUp(): Unit = {
    LocaleManager.registerBundleBaseName(bundleBaseName)
  }

  def getDataListener = null

  def getContextManager = FormContextManager

  def getModuleDescriptor = moduleDescriptor

}
