/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.category.entity

import collection.mutable.ListBuffer
import collection.JavaConversions._
import java.util.{List => jList}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 3/12/11
 * Time: 7:23 PM
 * To change this template use File | Settings | File Templates.
 */

trait CategorizedMixin extends Categorized {
  var categories = new ListBuffer[Category]

  def getCategories = bufferAsJavaList(categories)

  def setCategories(jCategories: jList[Category]) {
    categories = new ListBuffer[Category]
    categories ++= asScalaBuffer(jCategories)
  }
}