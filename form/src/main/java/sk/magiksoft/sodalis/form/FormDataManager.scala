
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.form

import entity.Form
import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager
import sk.magiksoft.sodalis.category.entity.Category

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Apr 13, 2010
 * Time: 9:01:37 PM
 * To change this template use File | Settings | File Templates.
 */

object FormDataManager extends ClientDataManager {
  def getHeaderForms: java.util.List[_] = getDatabaseEntities("select f from " + classOf[Form].getName + " f, " + classOf[Category].getName + " c " +
    "where c in elements(f.categories) and c.internalID=" + Form.HEADER_INTERNAL_ID)

}