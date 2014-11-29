package sk.magiksoft.sodalis.form

import sk.magiksoft.sodalis.core.locale.LocaleManager
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.core.module.{ModuleDescriptor, AbstractModule}
import sk.magiksoft.sodalis.core.factory.IconFactory

/**
 * @author wladimiiir
 * @since 2010/4/13
 */

class FormModule extends AbstractModule {
  LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.form.locale.form")

  def getDataListener = null

  def getContextManager = FormContextManager

  def getModuleDescriptor = new ModuleDescriptor(IconFactory.getInstance().getIcon("eventsModule") match {
    case e: ImageIcon => e
    case _ => null
  }, LocaleManager.getString("forms"))

}
