
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.swing.itemcomponent;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jun 25, 2010
 * Time: 9:57:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class ItemComponentAdapter<T> implements ItemComponentListener<T> {
    @Override
    public void itemAdded(T item) {
    }

    @Override
    public void itemRemoved(T item) {
    }

    @Override
    public void itemUpdated(T item) {
    }

    @Override
    public void selectionChanged() {
    }
}