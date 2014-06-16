
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.core.entity.property

import sk.magiksoft.sodalis.core.entity.Entity

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Oct 16, 2010
 * Time: 4:05:36 PM
 * To change this template use File | Settings | File Templates.
 */

trait Translator[A <: Entity] {
  def getTranslations: List[Translation[A]]
}