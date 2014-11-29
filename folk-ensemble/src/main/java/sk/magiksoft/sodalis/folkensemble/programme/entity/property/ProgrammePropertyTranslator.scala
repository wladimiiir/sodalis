package sk.magiksoft.sodalis.folkensemble.programme.entity.property

import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslator
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2010/10/22
 */

class ProgrammePropertyTranslator extends EntityPropertyTranslator[Programme] {
  def getTranslations = List(
    EntityTranslation("programmeName", programme => Option(programme.getName)),
    EntityTranslation("description", programme => Option(programme.getDescription)),
    EntityTranslation("programmeDuration", programme => Option(programme.getDurationString)),
    EntityTranslation("authors", programme => Option(programme.getAuthors.mkString(", "))),
    EntityTranslation("choreography", programme => Option(programme.getChoreographers.mkString(", "))),
    EntityTranslation("musicComposing", programme => Option(programme.getComposers.mkString(", ")))
  )
}
