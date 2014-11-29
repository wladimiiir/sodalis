package sk.magiksoft.sodalis.core.entity.property

import java.text.SimpleDateFormat
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.entity.Entity

/**
 * @author wladimiiir
 * @since 2010/10/17
 */

abstract class EntityPropertyTranslator[A <: Entity] extends Translator[A] {
  protected val DateFormat = new SimpleDateFormat("dd.MM.yyyy")
  protected val DateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm")
  protected val TimeFormat = new SimpleDateFormat("HH:mm")

  protected object EntityTranslation {
    def apply(key: String, name: String, value: A => Option[Any]) = new Translation[A](key, name, entity => entity match {
      case Some(entity) => value(entity)
      case None => None
    })

    def apply(key: String, value: A => Option[Any], valueClass: Class[_] = classOf[String]) = new Translation[A](key, LocaleManager.getString(key), entity => entity match {
      case Some(entity) => value(entity)
      case None => None
    }, valueClass)
  }

  protected class EntityTranslation[A <: Entity](key: String, name: String, value: A => Option[Any]) extends Translation[A](key, name, entity => entity match {
    case Some(entity) => value(entity)
    case None => None
  })

}
