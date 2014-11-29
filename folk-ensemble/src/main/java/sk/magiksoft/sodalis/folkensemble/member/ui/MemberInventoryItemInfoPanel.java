package sk.magiksoft.sodalis.folkensemble.member.ui;

import sk.magiksoft.sodalis.core.action.ContextTransferAction;
import sk.magiksoft.sodalis.core.context.Context;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel;
import sk.magiksoft.sodalis.folkensemble.inventory.InventoryModule;
import sk.magiksoft.sodalis.folkensemble.inventory.data.InventoryDataManager;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.Borrowing;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.BorrowingInventoryItemData;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.BorrowingWrapper;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.InventoryItem;
import sk.magiksoft.sodalis.folkensemble.inventory.ui.BorrowingWrapperTableModel;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.swing.ISTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

/**
 * @author wladimiiir
 */
public class MemberInventoryItemInfoPanel extends AbstractInfoPanel {

    private BorrowingWrapperTableModel model = new BorrowingWrapperTableModel();
    private Person member;
    private List<InventoryItem> inventoryItems;

    @Override
    public boolean isWizardSupported() {
        return false;
    }

    @Override
    public void initData() {
        if (initialized) {
            return;
        }
        inventoryItems = InventoryDataManager.getInstance().getInventoryItemsForMemberID(member.getId());

        model.removeAllObjects();
        for (InventoryItem inventoryItem : inventoryItems) {
            for (Borrowing borrowing : ((BorrowingInventoryItemData) inventoryItem.getInventoryItemData(BorrowingInventoryItemData.class)).getBorrowings()) {
                if (borrowing.getBorrower() != null && borrowing.getBorrower().getId().equals(member.getId())) {
                    model.addObject(new BorrowingWrapper(borrowing, inventoryItem));
                }
            }
        }
        initialized = true;
    }

    @Override
    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel(new BorderLayout());
        final ISTable table = new ISTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row;
                if (e.getClickCount() != 2 || (row = table.rowAtPoint(e.getPoint())) < 0) {
                    return;
                }
                goToInventoryItem(model.getObject(row).getInventoryItem());
            }
        });
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(100, 50));
        layoutPanel.add(scrollPane, BorderLayout.CENTER);

        return layoutPanel;
    }

    private void goToInventoryItem(final InventoryItem inventoryItem) {
        if (inventoryItem == null) {
            return;
        }
        ContextTransferAction contextAction = new ContextTransferAction(null, InventoryModule.class) {

            @Override
            protected boolean initialize(Context context) {
                context.setSelectedEntities(Arrays.asList(inventoryItem));
                return true;
            }

            @Override
            protected void finalize(Context context) {
            }
        };
        contextAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "goToInventoryItem"));
    }

    @Override
    public String getPanelName() {
        return LocaleManager.getString("borrowings");
    }

    @Override
    public void setupObject(Object object) {
    }

    @Override
    public void setupPanel(Object object) {
        if (!(object instanceof Person)) {
            return;
        }
        member = (Person) object;
        initialized = false;
    }

    @Override
    public boolean isReloadNeeded(List<? extends DatabaseEntity> entities) {
        for (DatabaseEntity entity : entities) {
            if (entity instanceof InventoryItem) {
                for (InventoryItem inventoryItem : inventoryItems) {
                    if (entity.getId().equals(inventoryItem.getId())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
