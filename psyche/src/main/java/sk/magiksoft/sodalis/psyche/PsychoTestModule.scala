/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche

import data.PsycheDataManager
import rorschach.entity.RorschachTestCreator
import sk.magiksoft.sodalis.core.module.{ModuleDescriptor, AbstractModule}
import sk.magiksoft.sodalis.core.locale.LocaleManager

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/13/11
 * Time: 11:14 AM
 * To change this template use File | Settings | File Templates.
 */

class PsychoTestModule extends AbstractModule {
  private lazy val descriptor = new ModuleDescriptor(null, LocaleManager.getString("psychoTests"))

  LocaleManager.registerBundleBaseName("sk.magiksoft.sodalis.psyche.locale.psyche")

  def getDataListener = PsycheContextManager

  def getContextManager = PsycheContextManager

  def getModuleDescriptor = descriptor
}