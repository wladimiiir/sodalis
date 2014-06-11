
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.form

import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.factory.IconFactory
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.core.module.{AbstractModule, ModuleDescriptor}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Apr 13, 2010
 * Time: 8:56:23 PM
 * To change this template use File | Settings | File Templates.
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