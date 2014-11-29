package sk.magiksoft.sodalis.folkensemble.inventory.action;

import sk.magiksoft.sodalis.core.action.GoToEntityAction;
import sk.magiksoft.sodalis.core.action.ObjectAction;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.Borrowing;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.BorrowingInventoryItemData;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.InventoryItem;
import sk.magiksoft.sodalis.folkensemble.member.MemberModule;
import sk.magiksoft.sodalis.person.entity.Person;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class GoToBorrowerAction extends GoToEntityAction implements ObjectAction {

    private InventoryItem inventoryItem;

    public GoToBorrowerAction() {
        super(MemberModule.class);
        putValue(NAME, LocaleManager.getString("goToBorrower"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Person borrower = inventoryItem == null
                ? null
                : inventoryItem.getInventoryItemData(BorrowingInventoryItemData.class).getCurrentBorrowing() == null
                ? null
                : inventoryItem.getInventoryItemData(BorrowingInventoryItemData.class).getCurrentBorrowing().getBorrower();

        if (borrower == null) {
            return;
        }

        this.entity = borrower;
        super.actionPerformed(e);
    }

    private List<InventoryItem> getAcceptedInventoryItems(List objects) {
        List<InventoryItem> accepted = new ArrayList<InventoryItem>();

        for (Object object : objects) {
            if (!(object instanceof InventoryItem)) {
                continue;
            }
            InventoryItem item = (InventoryItem) object;
            Borrowing borrowing = item.getInventoryItemData(BorrowingInventoryItemData.class).getCurrentBorrowing();

            if (borrowing != null) {
                accepted.add(item);
            }
        }

        return accepted;
    }

    @Override
    public boolean isActionEnabled(List objects) {
        return isActionShown(objects);
    }

    @Override
    public boolean isActionShown(List objects) {
        List<InventoryItem> inventoryItems = getAcceptedInventoryItems(objects);

        if (inventoryItems.size() == 1) {
            inventoryItem = inventoryItems.get(0);
            return true;
        } else {
            inventoryItem = null;
            return false;
        }
    }

}
