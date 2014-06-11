
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.core.utils


/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jun 10, 2010
 * Time: 5:31:22 PM
 * To change this template use File | Settings | File Templates.
 */

object Functions {
  def transform[A, B](list: List[A], transformation: (A => B)): List[B] = {
    list match {
      case null => Nil
      case Nil => Nil
      case x :: xs => transformation(x) :: transform(xs, transformation)
    }
  }
}