
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.item.presenter.model

import java.io.Serializable

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jun 19, 2010
 * Time: 1:11:18 PM
 * To change this template use File | Settings | File Templates.
 */

class ComboBoxPresenterModel extends Serializable {
  var items: List[String] = Nil
  var enumerationKey: String = null
}