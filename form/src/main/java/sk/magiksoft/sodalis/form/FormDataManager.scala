package sk.magiksoft.sodalis.form

import entity.Form
import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager
import sk.magiksoft.sodalis.category.entity.Category

/**
 * @author wladimiiir
 * @since 2010/4/13
 */

object FormDataManager extends ClientDataManager {
  def getHeaderForms: java.util.List[_] = getDatabaseEntities("select f from " + classOf[Form].getName + " f, " + classOf[Category].getName + " c " +
    "where c in elements(f.categories) and c.internalID=" + Form.HEADER_INTERNAL_ID)

}
