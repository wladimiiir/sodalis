
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.item.event

import sk.magiksoft.sodalis.item.entity.Item
import scala.swing.event.Event

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jun 25, 2010
 * Time: 3:05:31 PM
 * To change this template use File | Settings | File Templates.
 */

case class ItemSelectionChanged(item: Option[Item]) extends Event