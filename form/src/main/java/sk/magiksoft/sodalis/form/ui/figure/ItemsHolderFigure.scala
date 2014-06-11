
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.form.ui.figure

import org.jhotdraw.draw.TextHolderFigure

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Apr 28, 2010
 * Time: 2:31:11 PM
 * To change this template use File | Settings | File Templates.
 */

trait ItemsHolderFigure extends TextHolderFigure {
  var items: List[String] = List[String]("abc", "fdfsd", "fsdfs", "rewrwer")
  var selectedItem: String = null
}