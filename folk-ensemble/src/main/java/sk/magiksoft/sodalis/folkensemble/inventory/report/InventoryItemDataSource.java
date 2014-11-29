package sk.magiksoft.sodalis.folkensemble.inventory.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import sk.magiksoft.sodalis.core.printing.ObjectDataSource;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.Borrowing;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.BorrowingInventoryItemData;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.InventoryItem;

import java.util.List;

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
