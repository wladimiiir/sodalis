package sk.magiksoft.sodalis.folkensemble.inventory.entity;

/**
 * @author wladimiiir
 * @since 2010/7/1
 */
public class BorrowingWrapper {
    private Borrowing borrowing;
    private InventoryItem inventoryItem;

    public BorrowingWrapper(Borrowing borrowing, InventoryItem inventoryItem) {
        this.borrowing = borrowing;
        this.inventoryItem = inventoryItem;
    }

    public Borrowing getBorrowing() {
        return borrowing;
    }

    public InventoryItem getInventoryItem() {
        return inventoryItem;
    }
}
