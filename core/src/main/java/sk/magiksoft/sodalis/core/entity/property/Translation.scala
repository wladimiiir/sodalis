
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.core.entity.property

import sk.magiksoft.sodalis.core.entity.{Entity, DatabaseEntity}
import sk.magiksoft.sodalis.core.printing.TableColumnWrapper.Alignment

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Oct 15, 2010
 * Time: 5:52:58 PM
 * To change this template use File | Settings | File Templates.
 */

class Translation[A <: Entity](val key: String, val name: String, value: Option[A] => Option[Any], val valueClass:Class[_] = classOf[String]) {
  def getValue(entity: Option[A] = None) = value.apply(entity)
  override def toString = name
}