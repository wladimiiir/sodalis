package sk.magiksoft.sodalis.category.entity

import collection.mutable.ListBuffer
import collection.JavaConversions._
import java.util.{List => jList}

/**
 * @author wladimiiir
 * @since 2011/3/12
 */

trait CategorizedMixin extends Categorized {
  var categories = new ListBuffer[Category]

  def getCategories = bufferAsJavaList(categories)

  def setCategories(jCategories: jList[Category]) {
    categories = new ListBuffer[Category]
    categories ++= asScalaBuffer(jCategories)
  }
}
