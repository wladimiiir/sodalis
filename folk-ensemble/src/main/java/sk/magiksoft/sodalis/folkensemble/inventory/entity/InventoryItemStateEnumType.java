package sk.magiksoft.sodalis.folkensemble.inventory.entity;

import sk.magiksoft.hibernate.IntEnumUserType;

/**
 * @author wladimiiir
 */
public class InventoryItemStateEnumType extends IntEnumUserType<BorrowingInventoryItemData.InventoryItemState> {

    public InventoryItemStateEnumType() {
        super(BorrowingInventoryItemData.InventoryItemState.class, BorrowingInventoryItemData.InventoryItemState.values());
    }
}
