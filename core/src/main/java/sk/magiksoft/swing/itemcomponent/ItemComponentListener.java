
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.swing.itemcomponent;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jun 25, 2010
 * Time: 9:53:46 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ItemComponentListener<T> extends EventListener {
    void itemAdded(T item);

    void itemRemoved(T item);

    void itemUpdated(T item);

    void selectionChanged();
}