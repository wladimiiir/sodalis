
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.table;

import java.util.Comparator;

/**
 * @author wladimiiir
 */
public interface SortableTableModel {
    public Comparator getComparator(int column);

    public void sort(int column, boolean ascending);
}