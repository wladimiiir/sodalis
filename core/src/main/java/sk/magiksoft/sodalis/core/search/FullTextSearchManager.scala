package sk.magiksoft.sodalis.core.search

import sk.magiksoft.sodalis.core.entity.Entity
import java.lang.reflect.Field
import collection.immutable.List
import collection.JavaConversions._
import collection.mutable.HashMap
import sk.magiksoft.utils.StringUtils

/**
 * @author wladimiiir
 * @since 2011/1/12
 */

object FullTextSearchManager {
  private val fieldMap = new HashMap[Class[Entity], List[Field]]

  def find(entity: Entity, fullText: String): Boolean = {
    var fields = fieldMap.get(entity.getClass.asInstanceOf[Class[Entity]])
    if (fields.isEmpty) {
      fields = Option(findFields(entity))
      fieldMap += entity.getClass.asInstanceOf[Class[Entity]] -> fields.get
    }

    for (field <- fields.get) {
      field.setAccessible(true)
      if (classOf[java.lang.Iterable[Entity]].isAssignableFrom(field.getType)) {
        val list = field.get(entity).asInstanceOf[java.lang.Iterable[Entity]]
        for (item <- list) {
          if (find(item, fullText)) return true
        }
      } else if (classOf[Iterable[Entity]].isAssignableFrom(field.getType)) {
        val list = field.get(entity).asInstanceOf[Iterable[Entity]]
        for (item <- list) {
          if (find(item, fullText)) return true
        }
      } else if (classOf[java.util.Map[_, Entity]].isAssignableFrom(field.getType)) {
        val map = field.get(entity).asInstanceOf[java.util.Map[_, Entity]]
        for (item <- map.values) {
          if (find(item, fullText)) return true
        }
      } else if (classOf[Iterable[(_, Entity)]].isAssignableFrom(field.getType)) {
        val map = field.get(entity).asInstanceOf[Iterable[(_, Entity)]]
        for (tuple <- map) {
          if (find(tuple._2, fullText)) return true
        }
      } else {
        val value = field.get(entity).toString
        if (StringUtils.removeDiacritics(value.toLowerCase).contains(fullText.toLowerCase)) return true
      }
    }

    false
  }

  def find(entities: List[Entity], fullText: String): List[Entity] = entities.filter(e => find(e, fullText))

  private def findFields(entity: Entity) = entity.getClass.getDeclaredFields.filter(_.isAnnotationPresent(classOf[FullText])).toList
}
