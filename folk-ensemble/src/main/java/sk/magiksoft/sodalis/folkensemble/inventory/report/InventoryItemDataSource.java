
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.folkensemble.inventory.report;

import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import sk.magiksoft.sodalis.core.printing.ObjectDataSource;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.Borrowing;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.BorrowingInventoryItemData;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.InventoryItem;

/**
 * @author wladimiiir
 */
public class InventoryItemDataSource extends ObjectDataSource<InventoryItem> {

    public InventoryItemDataSource(List<InventoryItem> inventoryItems) {
        super(inventoryItems);
    }

    @Override
    public Object getFieldValue(JRField field) throws JRException {
        final String fieldName = field.getName();

        if (entity == null) {
            return null;
        }
        if (fieldName.equals("name")) {
            return "";
        } else if (fieldName.equals("evidenceNumber")) {
            return "";
        } else if (fieldName.equals("description")) {
            return "";
        } else if (fieldName.equals("inventoryItem.state")) {
            final Borrowing currentBorrowing = entity.getInventoryItemData(BorrowingInventoryItemData.class).getCurrentBorrowing();
            return entity.getInventoryItemData(BorrowingInventoryItemData.class).getState().toString() + (currentBorrowing == null
                    ? ""
                    : " (" + DATE_FORMAT.format(currentBorrowing.getTo().getTime()) + ", " + currentBorrowing.getBorrower().getFullName(true) + ")");
        }

        return "";
    }
}