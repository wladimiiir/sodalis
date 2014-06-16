
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.folkensemble.programme.entity.property

import sk.magiksoft.sodalis.folkensemble.programme.entity.Programme

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 10/22/10
 * Time: 3:19 PM
 * To change this template use File | Settings | File Templates.
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