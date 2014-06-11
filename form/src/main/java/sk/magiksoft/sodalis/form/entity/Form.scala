
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.form.entity

import reflect.BeanProperty
import sk.magiksoft.sodalis.category.entity.{Category, Categorized}
import collection.JavaConversions._
import java.util.List
import collection.mutable.ListBuffer
import org.jhotdraw.draw.DrawingView
import sk.magiksoft.sodalis.core.entity.{PostCreation, DatabaseEntity, AbstractDatabaseEntity}
import sk.magiksoft.sodalis.category.{CategoryDataManager, CategoryManager}
import sk.magiksoft.sodalis.form.settings.FormSettings
import sk.magiksoft.sodalis.form.ui.FormDrawing
import sk.magiksoft.sodalis.form.util.DocumentUtils
import sk.magiksoft.sodalis.core.settings.Settings

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Apr 13, 2010
 * Time: 9:14:58 PM
 * To change this template use File | Settings | File Templates.
 */

class Form(pageFormat: Format.Format, widthMM: Double, heightMM: Double) extends AbstractDatabaseEntity with Categorized {
  @BeanProperty var name = ""
  @BeanProperty var description = ""
  @BeanProperty var format = pageFormat
  @BeanProperty var pageWidthMM: Double = widthMM //Format.getWidthMM(Format.A4)
  @BeanProperty var pageHeightMM: Double = heightMM //Format.getHeightMM(Format.A4)
  @BeanProperty var pages = ListBuffer[FormDrawing](new FormDrawing(DocumentUtils.toPix(pageWidthMM), DocumentUtils.toPix(pageHeightMM)))
  var categories: ListBuffer[Category] = new ListBuffer[Category]

  def this() = this (Format.A4, Format.getWidthMM(Format.A4), Format.getHeightMM(Format.A4))

  @PostCreation def postCreation = {
    setCategories(CategoryDataManager.getInstance.getCategories(FormSettings.getValue(Settings.O_SELECTED_CATEGORIES).asInstanceOf[List[java.lang.Long]]))
  }

  def setCategories(categories: List[Category]) = {
    this.categories = new ListBuffer[Category] ++ categories
  }

  def addTranslator(translation: (String => String)) = {

  }

  def getCategories = asList(categories)

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