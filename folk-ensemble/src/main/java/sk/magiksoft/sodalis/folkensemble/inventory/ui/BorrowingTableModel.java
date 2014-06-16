
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

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.Borrowing;

import java.text.DateFormat;

/**
 * @author wladimiiir
 */
public class BorrowingTableModel extends ObjectTableModel<Borrowing> {

    public BorrowingTableModel() {
        super(new String[]{
                LocaleManager.getString("borrower"),
                LocaleManager.getString("from"),
                LocaleManager.getString("to")
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Borrowing borrowing = getObject(rowIndex);

        if (borrowing == null) {
            return "";
        }

        switch (columnIndex) {
            case 0:
                return borrowing.getBorrowerName();
            case 1:
                return DateFormat.getDateInstance(DateFormat.SHORT).format(borrowing.getFrom().getTime());
            case 2:
                return DateFormat.getDateInstance(DateFormat.SHORT).format(borrowing.getTo().getTime());
        }
        return "";
    }

}