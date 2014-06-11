
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.core.pattern

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: May 16, 2010
 * Time: 12:44:47 PM
 * To change this template use File | Settings | File Templates.
 */

trait PatternResolver {
  def resolvePattern(pattern: String): String

  def getAvailablePatterns: Array[Pattern]
}