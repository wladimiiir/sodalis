
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.core.function;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 1/21/11
 * Time: 3:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ResultFunction<R, O> {
    R apply(O object);
}