package sk.magiksoft.sodalis.folkensemble.inventory.entity;

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.locale.LocaleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class BorrowingInventoryItemData extends AbstractDatabaseEntity implements InventoryItemData {

    public enum InventoryItemState {

        AVAILABLE, BORROWED, REPAIRING;

        @Override
        public String toString() {
            switch (this) {
                case AVAILABLE:
                    return LocaleManager.getString("inventoryItem.available");
                case BORROWED:
                    return LocaleManager.getString("inventoryItem.borrowed");
                case REPAIRING:
                    return LocaleManager.getString("inventoryItem.repairing");
                default:
                    return "";
            }
        }
    }

    ;
    private InventoryItemState state = InventoryItemState.AVAILABLE;
    private List<Borrowing> borrowings = new ArrayList<Borrowing>();

    public InventoryItemState getState() {
        return state;
    }

    public void setState(InventoryItemState state) {
        this.state = state;
    }

    public List<Borrowing> getBorrowings() {
        return borrowings;
    }

    public void setBorrowings(List<Borrowing> borrowings) {
        this.borrowings = borrowings;
    }

    public Borrowing getCurrentBorrowing() {
        if (getState() != InventoryItemState.BORROWED) {
            return null;
        }

        for (Borrowing borrowing : borrowings) {
            if (!borrowing.isReturned()) {
                return borrowing;
            }
        }

        return null;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof BorrowingInventoryItemData) || entity == this) {
            return;
        }
        BorrowingInventoryItemData data = (BorrowingInventoryItemData) entity;

        this.state = data.state;
        this.borrowings.clear();
        this.borrowings.addAll(data.borrowings);
    }
}
