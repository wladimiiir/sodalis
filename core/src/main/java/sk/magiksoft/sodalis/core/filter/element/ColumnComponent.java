
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.filter.element;

import javax.swing.*;

/**
 * @author wladimiiir
 */
public interface ColumnComponent {
    String getLabelText();

    JComponent getComponent();

    void setSelect(String select);

    void setFrom(String from);

    void setWhere(String where);

    void setLabelText(String labelText);

    void addItem(Object object);

    String getSelectQuery();

    String getFromQuery();

    String getWhereQuery();

    boolean isIncluded();
}