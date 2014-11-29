package sk.magiksoft.sodalis.folkensemble.inventory.data;

import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.InventoryItem;

import java.util.List;

/**
 * @author wladimiiir
 */
public class InventoryDataManager extends ClientDataManager {

    private static InventoryDataManager instance;

    protected InventoryDataManager() {
    }

    public static InventoryDataManager getInstance() {
        if (instance == null) {
            instance = new InventoryDataManager();
        }

        return instance;
    }

    public List<InventoryItem> getInventoryItemsForMemberID(Long id) {
        List<InventoryItem> inventoryItems;

        inventoryItems = getDatabaseEntities("select distinct ii from InventoryItem as ii, BorrowingInventoryItemData as biid, Borrowing as b " +
                "where biid in elements(ii.inventoryItemDatas) and b in elements(biid.borrowings) and b.borrower.id=" + id);

        return inventoryItems;
    }
}
