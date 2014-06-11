
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.core.entity.property

import collection.immutable.Map
import collection.mutable.{HashMap, ListBuffer}
import sk.magiksoft.sodalis.person.entity.Person
import sk.magiksoft.sodalis.core.entity.{Entity, DatabaseEntity}
import sk.magiksoft.sodalis.core.printing.TableColumnWrapper.Alignment

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Oct 15, 2010
 * Time: 3:34:40 PM
 * To change this template use File | Settings | File Templates.
 */

object EntityPropertyTranslatorManager {
  private val translationMap = new HashMap[Class[_ <: Entity], ListBuffer[Translation[_ <: Entity]]]

  def registerTranslation[A <: Entity](clazz: Class[A], translation: Translation[A]): Unit = translationMap.get(clazz) match {
    case Some(translations) => translations += translation
    case None => translationMap += clazz -> ListBuffer(translation)
  }

  def registerTranslator[A <: Entity](clazz: Class[_ <: A], translator: Translator[A]){
    translationMap.get(clazz) match {
      case Some(translations) => translations ++= translator.getTranslations
      case None => translationMap += clazz -> (new ListBuffer ++ translator.getTranslations.toBuffer)
    }
  }

  def getName[A <: Entity](clazz: Class[A], key: String): Option[String] = translationMap.get(clazz) match {
    case Some(translations) => translations.reverse.find(t => t.key == key) match {
      case Some(translation) => Option(translation.name)
      case None => None
    }
    case None => None
  }

  def getValueClass[A <: Entity](clazz: Class[A], key: String):Option[Class[_]] = translationMap.get(clazz) match {
    case Some(translations) => translations.reverse.find(t => t.key == key) match {
      case Some(translation) => Option(translation.valueClass)
      case None => None
    }
    case None => None
  }

  def getValue[A <: Entity](entity: A, key: String): Option[Any] = translationMap.get(entity.getClass.asInstanceOf[Class[A]]) match {
    case Some(translations) => translations.reverse.find(t => t.key == key) match {
      case Some(translation) => translation.asInstanceOf[Translation[A]].getValue(Option(entity))
      case None => None
    }
    case None => None
  }

  def getTranslator[A <: Entity](clazz: Class[A]): Translator[A] = new Translator[A] {
    def getTranslations = translationMap.get(clazz) match {
      case Some(translations) => translations.toList.asInstanceOf[List[Translation[A]]]
      case None => Nil
    }
  }

}