
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


/*
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 2/4/11
 * Time: 7:46 PM
 */
package sk.magiksoft.sodalis.core.logger

import org.dom4j.Element

trait LogInfo {
  def addLogInfoNode(parent: Element): Unit
}