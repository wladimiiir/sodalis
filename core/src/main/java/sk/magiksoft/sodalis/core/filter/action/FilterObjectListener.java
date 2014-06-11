
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.core.filter.action;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 1/7/11
 * Time: 9:45 AM
 * To change this template use File | Settings | File Templates.
 */
public interface FilterObjectListener extends EventListener{
    void filterObjectChanged(FilterObject filterObject);
}