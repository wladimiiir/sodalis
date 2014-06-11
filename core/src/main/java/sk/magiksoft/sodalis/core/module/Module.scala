/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.core.module

import sk.magiksoft.sodalis.core.context.ContextManager
import sk.magiksoft.sodalis.core.data.DataListener
import sk.magiksoft.sodalis.category.entity.Category

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 4/22/11
 * Time: 9:29 AM
 * To change this template use File | Settings | File Templates.
 */

trait Module {
  def getModuleDescriptor: ModuleDescriptor

  def getContextManager: ContextManager

  def getDataListener: DataListener

  def postInitialization()

  def getDynamicCategories: List[Category]

  def registerDynamicCategory(dynamicCategory: Category)
}