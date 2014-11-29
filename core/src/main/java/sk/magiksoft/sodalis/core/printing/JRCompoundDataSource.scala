package sk.magiksoft.sodalis.core.printing

import net.sf.jasperreports.engine.{JRField, JRDataSource}

/**
 * @author wladimiiir
 * @since 2011/1/26
 */
class JRCompoundDataSource(fieldDataSources: Array[JRDataSource], objectsDataSource: Option[JRDataSource]) extends JRDataSource {
  private var hasNext = true;

  for (fieldDataSource <- fieldDataSources) {
    fieldDataSource.next
  }

  def getFieldValue(jrField: JRField) = jrField.getName match {
    case "index" => objectsDataSource match {
      case Some(dataSource) => dataSource.getFieldValue(jrField)
      case None => ""
    }
    case "index+" => objectsDataSource match {
      case Some(dataSource) => dataSource.getFieldValue(jrField)
      case None => ""
    }
    case _ => fieldDataSources.find(ds => ds.getFieldValue(jrField) != null
      && (!ds.getFieldValue(jrField).isInstanceOf[String] || !ds.getFieldValue(jrField).asInstanceOf[String].isEmpty)) match {
      case Some(dataSource) => dataSource.getFieldValue(jrField)
      case None => objectsDataSource match {
        case Some(dataSource) => dataSource.getFieldValue(jrField)
        case None => null
      }
    }
  }

  def next = objectsDataSource match {
    case Some(dataSource) => dataSource.next
    case None => {
      hasNext = !hasNext
      !hasNext
    }
  }
}
