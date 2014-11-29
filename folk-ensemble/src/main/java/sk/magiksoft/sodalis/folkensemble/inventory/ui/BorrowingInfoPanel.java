package sk.magiksoft.sodalis.folkensemble.inventory.ui;

import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;
import sk.magiksoft.sodalis.core.action.ContextTransferAction;
import sk.magiksoft.sodalis.core.context.Context;
import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.sodalis.core.ui.OkCancelDialog;
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel;
import sk.magiksoft.sodalis.core.utils.UIUtils;
import sk.magiksoft.sodalis.folkensemble.inventory.data.InventoryDataManager;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.Borrowing;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.BorrowingInventoryItemData;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.InventoryItem;
import sk.magiksoft.sodalis.folkensemble.member.MemberModule;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.ui.PersonalDataInfoPanel;
import sk.magiksoft.swing.DateSpinner;
import sk.magiksoft.swing.ISTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * @author wladimiiir
 */
public class BorrowingInfoPanel extends AbstractInfoPanel implements DataListener {

    private BorrowingTableModel model;
    private ISTable tblBorrowings;
    private PersonalDataInfoPanel personalDataPanel;
    private InventoryItem currentInventoryItem;
    private JButton btnNewBorrowing;
    private JButton btnReturnBorrowing;
    private Borrowing currentBorrowing;
    private List<AbstractButton> controlPanelButtons;

    public BorrowingInfoPanel() {
        InventoryDataManager.getInstance().addDataListener(this);
    }

    @Override
    public String getPanelName() {
        return LocaleManager.getString("borrowings");
    }

    @Override
    public void setupObject(Object object) {
        if (!(object instanceof InventoryItem)) {
            return;
        }

        InventoryItem inventoryItem = (InventoryItem) object;
        inventoryItem.getInventoryItemData(BorrowingInventoryItemData.class).setBorrowings(model.getObjects());
    }

    @Override
    public void setupPanel(Object object) {
        if (!(object instanceof InventoryItem)) {
            return;
        }

        currentInventoryItem = (InventoryItem) object;
        initialized = false;
    }

    @Override
    public List<AbstractButton> getControlPanelButtons() {
        btnReturnBorrowing.setEnabled(false);
        return controlPanelButtons;
    }

    @Override
    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel(new GridLayout(1, 2));
        final JScrollPane scrollPane = new JScrollPane(tblBorrowings = new ISTable(model = new BorrowingTableModel()));
        final JXLayer<JComponent> layer = new JXLayer<JComponent>(personalDataPanel = new PersonalDataInfoPanel(false));
        final class MyLock extends LockableUI {
            @Override
            public void setDirty(boolean b) {
                super.setDirty(b);
            }
        }
        final MyLock lock = new MyLock();

        personalDataPanel.initLayout();
        tblBorrowings.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                currentBorrowing = model.getObject(tblBorrowings.getSelectedRow());
                if (currentBorrowing == null) {
                    layer.setVisible(false);
                    btnReturnBorrowing.setEnabled(false);
                } else {
                    if (currentBorrowing.getBorrower() != null) {
                        layer.setVisible(true);
                        personalDataPanel.setupPanel(currentBorrowing.getBorrower());
                        personalDataPanel.initData();
                        lock.setDirty(true);
                    } else {
                        layer.setVisible(false);
                    }
                    btnReturnBorrowing.setEnabled(!currentBorrowing.isReturned());
                }
            }
        });
        scrollPane.setPreferredSize(new Dimension(100, 50));
        tblBorrowings.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row;
                final Person member;
                if (e.getClickCount() != 2 || (row = tblBorrowings.rowAtPoint(e.getPoint())) < 0) {
                    return;
                }

                final Borrowing borrowing = model.getObject(row);
                member = borrowing.getBorrowerWrapper() == null ? borrowing.getBorrower() : borrowing.getBorrowerWrapper().getPerson();
                if (member == null) {
                    return;
                }
                new ContextTransferAction(null, MemberModule.class) {

                    @Override
                    protected boolean initialize(Context context) {
                        context.setSelectedEntities(Arrays.asList(member));
                        return true;
                    }

                    @Override
                    protected void finalize(Context context) {
                    }
                }.actionPerformed(new ActionEvent(tblBorrowings, ActionEvent.ACTION_PERFORMED, "goToMember"));
            }
        });
        tblBorrowings.addMouseListener(new MouseAdapter() {
            private DeleteBorrowingAction deleteBorrowingAction;
            private JPopupMenu popupMenu;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 1 || !SwingUtilities.isRightMouseButton(e)) {
                    return;
                }
                if (popupMenu == null) {
                    popupMenu = new JPopupMenu();
                    popupMenu.add(deleteBorrowingAction = new DeleteBorrowingAction());
                }

                int row = tblBorrowings.rowAtPoint(e.getPoint());
                if (row != tblBorrowings.getSelectedRow()) {
                    return;
                }
                deleteBorrowingAction.setEnabled(currentBorrowing != null && currentBorrowing.isReturned());
                popupMenu.show(tblBorrowings, e.getX(), e.getY());
            }
        });

        //setting cell renderer
        TableCellRenderer renderer = new BorrowingTableCellRenderer();
        for (int i = 0; i < tblBorrowings.getColumnModel().getColumnCount(); i++) {
            TableColumn column = tblBorrowings.getColumnModel().getColumn(i);
            column.setCellRenderer(renderer);
        }

        scrollPane.getViewport().setBackground(Color.WHITE);
        layoutPanel.add(scrollPane);

        layer.setVisible(false);
        layer.setUI(lock);
        lock.setLocked(true);
        lock.setLockedCursor(Cursor.getDefaultCursor());
        layoutPanel.add(layer);

        btnNewBorrowing = new JButton(new CreateBorrowingAction());
        btnReturnBorrowing = new JButton(new ReturnBorrowingAction());
        controlPanelButtons = new ArrayList<AbstractButton>(Arrays.asList(btnNewBorrowing, btnReturnBorrowing));

        return layoutPanel;
    }

    @Override
    public void initData() {
        if (initialized) {
            return;
        }
        final BorrowingInventoryItemData inventoryItemData = currentInventoryItem.getInventoryItemData(BorrowingInventoryItemData.class);
        setBorrowings(inventoryItemData.getBorrowings());
        btnNewBorrowing.setEnabled(inventoryItemData.getState() == BorrowingInventoryItemData.InventoryItemState.AVAILABLE);
        btnReturnBorrowing.setEnabled(false);
        initialized = true;
    }

    private void setBorrowings(List<Borrowing> borrowings) {
        int selectedIndex = -1;
        Calendar today = Calendar.getInstance();

        model.removeAllObjects();
        int index = 0;
        for (Borrowing borrowing : borrowings) {
            model.addObject(borrowing);
            if (borrowing.getFrom().before(today) && borrowing.getTo().after(today)) {
                selectedIndex = index;
            }
            index++;
        }
        //selection with only one borrower does not refresh Return button
//        if (selectedIndex < 0) {
//            tblBorrowings.getSelectionModel().clearSelection();
//        } else {
//            tblBorrowings.getSelectionModel().setSelectionInterval(selectedIndex, selectedIndex);
//        }
    }

    @Override
    public void entitiesAdded(List<? extends DatabaseEntity> entities) {
    }

    @Override
    public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
        if (!initialized || currentInventoryItem == null) {
            return;
        }
        for (DatabaseEntity entity : entities) {
            if (entity instanceof InventoryItem && entity.getId().equals(currentInventoryItem.getId())) {
                setupPanel(entity);
                initData();
                break;
            }
        }
    }

    @Override
    public void entitiesRemoved(List<? extends DatabaseEntity> entities) {
    }

    private class CreateBorrowingAction extends AbstractAction {

        private BorrowingCreationPanel borrowingCreationPanel;

        public CreateBorrowingAction() {
            super(LocaleManager.getString("createBorrowing"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            OkCancelDialog okCancelDialog = new OkCancelDialog();
            okCancelDialog.getOkButton().addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    createBorrowing();
                }
            });
            borrowingCreationPanel = new BorrowingCreationPanel(currentInventoryItem, okCancelDialog);
            okCancelDialog.setTitle(LocaleManager.getString("newBorrowing"));
            okCancelDialog.setModal(true);
            okCancelDialog.setMainPanel(borrowingCreationPanel);
            UIUtils.makeISDialog(okCancelDialog);
            okCancelDialog.setSize(450, 350);
            okCancelDialog.setLocationRelativeTo(null);
            okCancelDialog.setVisible(true);
        }

        private void createBorrowing() {
            if (borrowingCreationPanel == null || currentInventoryItem == null) {
                return;
            }

            Borrowing borrowing = borrowingCreationPanel.getBorrowing();
            if (borrowing == null) {
                //TODO: dialog with no borrower message
                return;
            }
            currentInventoryItem.getInventoryItemData(BorrowingInventoryItemData.class).getBorrowings().add(borrowing);
            currentInventoryItem.getInventoryItemData(BorrowingInventoryItemData.class).setState(BorrowingInventoryItemData.InventoryItemState.BORROWED);
            InventoryDataManager.getInstance().updateDatabaseEntity(currentInventoryItem);
        }
    }

    private class DeleteBorrowingAction extends AbstractAction {
        public DeleteBorrowingAction() {
            super(LocaleManager.getString("delete"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (ISOptionPane.showConfirmDialog(tblBorrowings, LocaleManager.getString("deleteBorrowingConfirm"),
                    currentBorrowing.toString(), JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                return;
            }

            final BorrowingInventoryItemData inventoryItemData = currentInventoryItem.getInventoryItemData(BorrowingInventoryItemData.class);
            final List<Borrowing> borrowings = inventoryItemData.getBorrowings();
            for (int i = 0, borrowingsSize = borrowings.size(); i < borrowingsSize; i++) {
                Borrowing borrowing = borrowings.get(i);
                if (borrowing.getId().equals(currentBorrowing.getId())) {
                    borrowings.remove(i);
                    break;
                }
            }
            InventoryDataManager.getInstance().updateDatabaseEntity(currentInventoryItem);
        }
    }

    private class ReturnBorrowingAction extends AbstractAction {

        public ReturnBorrowingAction() {
            super(LocaleManager.getString("returnBorrowing"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentBorrowing == null) {
                return;
            }

            DateSpinner dateSpinner = new DateSpinner("d.M.yyyy");
            int result = ISOptionPane.showConfirmDialog(null, new Object[]{LocaleManager.getString("returnToDay") + ":", dateSpinner},
                    LocaleManager.getString("returnBorrowing"), ISOptionPane.OK_CANCEL_OPTION);

            if (result != ISOptionPane.OK_OPTION) {
                return;
            }
            currentBorrowing.setReturned(true);
            currentBorrowing.setTo(dateSpinner.getValue());
            currentInventoryItem.getInventoryItemData(BorrowingInventoryItemData.class).setState(BorrowingInventoryItemData.InventoryItemState.AVAILABLE);
            InventoryDataManager.getInstance().updateDatabaseEntity(currentInventoryItem);
        }
    }
}
