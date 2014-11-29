package sk.magiksoft.sodalis.core.entity.property

import sk.magiksoft.sodalis.core.printing.ObjectDataSource
import net.sf.jasperreports.engine.JRField
import collection.JavaConversions
import sk.magiksoft.sodalis.core.entity.Entity

/**
 * @author wladimiiir
 * @since 2010/10/17
 */

class EntityPropertyJRDataSource[A <: Entity](objects: List[A]) extends ObjectDataSource[A](JavaConversions.bufferAsJavaList(objects.toBuffer)) {
  def getFieldValue(jrField: JRField) = jrField.getName match {
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
}
