
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.folkensemble.inventory.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Calendar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.Borrowing;

/**
 *
 * @author wladimiiir
 */
public class BorrowingTableCellRenderer extends DefaultTableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Borrowing borrowing = ((BorrowingTableModel)table.getModel()).getObject(row);

        if(!borrowing.isReturned()){
            component.setFont(component.getFont().deriveFont(Font.BOLD));
            if (column == 2 && borrowing.getTo().before(Calendar.getInstance())) {
                component.setForeground(Color.RED);
            }else{
                component.setForeground(Color.BLACK);
            }
        }else{
            component.setForeground(Color.BLACK);
        }

        return component;
    }

}