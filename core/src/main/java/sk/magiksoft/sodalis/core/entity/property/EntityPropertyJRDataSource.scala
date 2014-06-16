
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.core.entity.property

import sk.magiksoft.sodalis.core.printing.ObjectDataSource
import net.sf.jasperreports.engine.JRField
import collection.JavaConversions
import sk.magiksoft.sodalis.core.entity.Entity

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Oct 17, 2010
 * Time: 12:54:00 PM
 * To change this template use File | Settings | File Templates.
 */

class EntityPropertyJRDataSource[A <: Entity](objects: List[A]) extends ObjectDataSource[A](JavaConversions.bufferAsJavaList(objects.toBuffer)) {
  def getFieldValue(jrField: JRField) = entity match {
    case entity: A => jrField.getName match {
      case "entityCount" => "1"
      case "index" => index.toString
      case "index+" => (index + 1).toString
      case key: String => EntityPropertyTranslatorManager.getValue(entity, key) match {
        case Some(obj) if obj.isInstanceOf[AnyRef] => obj.asInstanceOf[AnyRef]
        case Some(obj) => obj.toString
        case None => ""
      }
      case _ => ""
    }
    case _ => ""
  }
}