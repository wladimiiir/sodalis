package sk.magiksoft.sodalis.form.entity

import sk.magiksoft.sodalis.category.entity.{Category, Categorized}
import java.util.List
import sk.magiksoft.sodalis.core.entity._
import sk.magiksoft.sodalis.category.CategoryDataManager
import sk.magiksoft.sodalis.form.settings.FormSettings
import sk.magiksoft.sodalis.form.ui.FormDrawing
import sk.magiksoft.sodalis.form.util.DocumentUtils
import scala.beans.BeanProperty
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._
import sk.magiksoft.sodalis.core.settings.Settings

/**
 * @author wladimiiir
 * @since 2010/4/13
 */

class Form(pageFormat: Format.Format, widthMM: Double, heightMM: Double) extends AbstractDatabaseEntity with Categorized {
  @BeanProperty var name = ""
  @BeanProperty var description = ""
  @BeanProperty var format = pageFormat
  @BeanProperty var pageWidthMM: Double = widthMM
  //Format.getWidthMM(Format.A4)
  @BeanProperty var pageHeightMM: Double = heightMM
  //Format.getHeightMM(Format.A4)
  @BeanProperty var pages = ListBuffer[FormDrawing](new FormDrawing(DocumentUtils.toPix(pageWidthMM), DocumentUtils.toPix(pageHeightMM)))
  var categories: ListBuffer[Category] = new ListBuffer[Category]

  def this() = this(Format.A4, Format.getWidthMM(Format.A4), Format.getHeightMM(Format.A4))

  @PostCreation def postCreation = {
    setCategories(CategoryDataManager.getInstance.getCategories(FormSettings.getValue(Settings.O_SELECTED_CATEGORIES).asInstanceOf[List[java.lang.Long]]))
  }

  def setCategories(categories: List[Category]) = {
    this.categories = new ListBuffer[Category] ++ categories
  }

  def addTranslator(translation: (String => String)) = {

  }

  def getCategories = categories

  def getFormatID = format.id

  def setFormatID(formatID: Int) = format = Format.values.find(f => f.id == formatID) match {
    case Some(format) => format
    case None => Format.A4
  }

  def updateFrom(entity: DatabaseEntity) = {
    entity match {
      case form: Form => {
        name = form.name
        description = form.description
      }
    }
  }

  def clearPages = for (drawing <- pages) {
    drawing.clear
  }

  override def toString = name
}


object Form {
  val HEADER_INTERNAL_ID = 1l
}
