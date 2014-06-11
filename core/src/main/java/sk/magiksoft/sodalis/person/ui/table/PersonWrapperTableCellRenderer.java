
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.person.ui.table;

import java.awt.Component;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import sk.magiksoft.sodalis.person.entity.PersonWrapper;
import sk.magiksoft.sodalis.person.utils.PersonUtils;

/**
 *
 * @author wladimiiir
 */
public class PersonWrapperTableCellRenderer extends DefaultTableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if(c instanceof JLabel){
            if(value instanceof PersonWrapper){
                ((JLabel)c).setText(((PersonWrapper)value).getPersonName());
            }else if(value instanceof List && !((List)value).isEmpty() && ((List)value).get(0) instanceof PersonWrapper){
                ((JLabel)c).setText(PersonUtils.personWrappersToString((List)value));
            }
        }

        return c;
    }



}