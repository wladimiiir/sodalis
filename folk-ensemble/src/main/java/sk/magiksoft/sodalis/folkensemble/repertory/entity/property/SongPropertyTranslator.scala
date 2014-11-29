package sk.magiksoft.sodalis.folkensemble.repertory.entity.property

import sk.magiksoft.sodalis.core.entity.property.{EntityPropertyTranslator, Translator}
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song
import scala.collection.JavaConversions._


/**
 * @author wladimiiir
 * @since 2010/10/20
 */

class SongPropertyTranslator extends EntityPropertyTranslator[Song] {
  def getTranslations = List(
    EntityTranslation("songName", song => Option(song.getName)),
    EntityTranslation("description", song => Option(song.getDescription)),
    EntityTranslation("songGenre", song => Option(song.getGenre)),
    EntityTranslation("region", song => Option(song.getRegion)),
    EntityTranslation("songDuration", song => Option(song.getDurationString)),
    EntityTranslation("choreography", song => Option(song.getChoreographers.mkString(", "))),
    EntityTranslation("pedagogists", song => Option(song.getPedagogists.mkString(", "))),
    EntityTranslation("musicComposing", song => Option(song.getComposers.mkString(", ")))
  )
}
