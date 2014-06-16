
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.folkensemble.repertory.entity.property

import sk.magiksoft.sodalis.core.entity.property.Translator
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song
import scala.collection.JavaConversions._


/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 10/20/10
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
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