
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.folkensemble.inventory.ui;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.BorrowingWrapper;
import sk.magiksoft.sodalis.item.entity.ItemPropertyValue;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jul 1, 2010
 * Time: 4:21:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class BorrowingWrapperTableModel extends ObjectTableModel<BorrowingWrapper> {
    public BorrowingWrapperTableModel() {
        super(new Object[]{
                LocaleManager.getString("inventoryItem"),
                LocaleManager.getString("evidenceNumber"),
                LocaleManager.getString("properties"),
                LocaleManager.getString("from"),
                LocaleManager.getString("to"),
                LocaleManager.getString("inventoryItem.state")
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final BorrowingWrapper wrapper = getObject(rowIndex);

        switch (columnIndex) {
            case 0:
                return wrapper.getInventoryItem().getItemType().getName();
            case 1:
                final List<ItemPropertyValue> evidenceNumbers = scala.collection.JavaConversions.asJavaList(wrapper.getInventoryItem().getPropertyTypeValues("EvidenceNumber"));
                return evidenceNumbers.isEmpty() ? "" : evidenceNumbers.get(0).getValue().toString();
            case 2:
                return wrapper.getInventoryItem().getHTMLInfoString();
            case 3:
                return DATE_FORMAT.format(wrapper.getBorrowing().getFrom().getTime());
            case 4:
                return DATE_FORMAT.format(wrapper.getBorrowing().getTo().getTime());
            case 5:
                return wrapper.getBorrowing().isReturned()
                        ? LocaleManager.getString("inventoryItem.returned")
                        : LocaleManager.getString("inventoryItem.borrowed");
            default:
                return "";
        }
    }
}