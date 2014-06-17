/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.service

import entity.property.ServicePropertyTranslator
import entity.Service
import sk.magiksoft.sodalis.core.module.{AbstractModule, ModuleDescriptor}
import sk.magiksoft.sodalis.core.factory.IconFactory
import sk.magiksoft.sodalis.core.locale.LocaleManager
import javax.swing.ImageIcon
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslatorManager

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 3/10/11
 * Time: 9:30 PM
 * To change this template use File | Settings | File Templates.
 */

class ServiceModule extends AbstractModule {
  LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.service.locale.service")
  EntityPropertyTranslatorManager.registerTranslator(classOf[Service], new ServicePropertyTranslator)

  def getDataListener = ServiceContextManager

  def getContextManager = ServiceContextManager

  def getModuleDescriptor = new ModuleDescriptor(IconFactory.getInstance.getIcon("services").asInstanceOf[ImageIcon], LocaleManager.getString("services"))
}