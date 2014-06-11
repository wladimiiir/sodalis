/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.core.module

import collection.JavaConversions
import sk.magiksoft.sodalis.category.entity.{DynamicCategory, Category}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 4/17/11
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */

abstract class AbstractModule extends Module {
  private var dynamicCategories = List[Category]()

  def registerDynamicCategory(dynamicCategory: Category) {
    dynamicCategories ::= dynamicCategory
  }

  def getDynamicCategories = {
    dynamicCategories.foreach{_.asInstanceOf[DynamicCategory].refresh()}
    dynamicCategories
  }

  def postInitialization() {}
}