package sk.magiksoft.sodalis.core.printing

import sk.magiksoft.sodalis.core.entity.Entity
import net.sf.jasperreports.engine.data.JRMapArrayDataSource
import net.sf.jasperreports.engine.JRDataSource
import scala.collection.JavaConversions

/**
 * @author wladimiiir
 * @since 2011/1/26
 */
abstract class PrintDocument(var name: String, jrxmlDocument: String) {

  protected def initEntity(entity: Entity): Boolean

  protected def getFieldDataSources(entity: Entity): Array[JRDataSource]

  protected def createFieldMap(entity: Entity): Option[Map[String, String]]

  protected def getObjectsDataSource(entity: Entity): Option[JRDataSource]

  def print(entity: Entity) = {
    if (initEntity(entity)) {
      var fieldDataSources = getFieldDataSources(entity)

      createFieldMap(entity) match {
        case Some(map) => fieldDataSources :+= new JRMapArrayDataSource(Array(JavaConversions.mapAsJavaMap(map)))
        case None =>
      }
      fieldDataSources :+= new JRMapArrayDataSource(Array(PrintingManager.getInstance.getCommonFieldValues))

      PrintingManager.getInstance.viewReport(jrxmlDocument, new JRCompoundDataSource(fieldDataSources, getObjectsDataSource(entity)))
    }
  }
}
