package sk.magiksoft.sodalis.folkensemble.inventory.ui;

import com.toedter.calendar.JDateChooser;
import sk.magiksoft.sodalis.core.action.ContextTransferAction;
import sk.magiksoft.sodalis.core.context.Context;
import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.folkensemble.inventory.InventoryModule;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.Borrowing;
import sk.magiksoft.sodalis.folkensemble.inventory.entity.InventoryItem;
import sk.magiksoft.sodalis.folkensemble.inventory.settings.InventorySettings;
import sk.magiksoft.sodalis.folkensemble.member.MemberModule;
import sk.magiksoft.sodalis.folkensemble.member.data.MemberDataManager;
import sk.magiksoft.sodalis.folkensemble.member.entity.MemberData;
import sk.magiksoft.sodalis.person.entity.Person;
import sk.magiksoft.sodalis.person.entity.PrivatePersonData;
import sk.magiksoft.swing.PopupTextField;
import sk.magiksoft.swing.event.PopupTextFieldListener;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.util.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public class BorrowingCreationPanel extends JPanel {

    private JDialog dialog;
    private javax.swing.JLabel lblFrom;
    private JLabel lblMemberId;
    private javax.swing.JLabel lblInventoryItem;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblTo;
    private javax.swing.JPanel pnlInventoryItem;
    private javax.swing.JPanel pnlMember;
    private javax.swing.JScrollPane scpMoreInfo;
    private JDateChooser dateFrom;
    private JDateChooser dateTo;
    private PopupTextField<String> tfdMemberId;
    private PopupTextField<String> tfdName;
    private javax.swing.JTextArea txaMoreInfo;
    private JButton btnChooseMember = new JButton(new GetMemberContextAction());
    private Map<String, BorrowerWrapper> borrowerNameMap = new HashMap<String, BorrowerWrapper>();
    private Map<String, BorrowerWrapper> borrowerMemberIDMap = new HashMap<String, BorrowerWrapper>();

    public BorrowingCreationPanel(InventoryItem inventoryItem, JDialog dialog) {
        this.dialog = dialog;
        initComponents();
        initMembers();
        lblInventoryItem.setText(inventoryItem == null ? "" : inventoryItem.toString());
        lblInventoryItem.setToolTipText(inventoryItem == null ? null : inventoryItem.toString());
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        pnlMember = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel();
        lblMemberId = new JLabel();
        lblFrom = new javax.swing.JLabel();
        dateFrom = new JDateChooser("dd.MM.yyyy", "##.##.####", '-');
        lblTo = new javax.swing.JLabel();
        dateTo = new JDateChooser("dd.MM.yyyy", "##.##.####", '-');
        scpMoreInfo = new javax.swing.JScrollPane();
        txaMoreInfo = new javax.swing.JTextArea();
        pnlInventoryItem = new javax.swing.JPanel();
        lblInventoryItem = new javax.swing.JLabel();
        tfdMemberId = new PopupTextField();
        tfdName = new PopupTextField();

        setLayout(new java.awt.BorderLayout());
        setBorder(null);

        pnlMember.setBorder(javax.swing.BorderFactory.createTitledBorder(LocaleManager.getString("borrower")));
        pnlMember.setLayout(new java.awt.GridBagLayout());

        lblMemberId.setText(LocaleManager.getString("memberid"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 5);
        pnlMember.add(lblMemberId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 6.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 0);
        pnlMember.add(tfdMemberId, gridBagConstraints);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridwidth = 1;
        pnlMember.add(btnChooseMember, gridBagConstraints);

        lblName.setText(LocaleManager.getString("memberName"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 5);
        pnlMember.add(lblName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 6.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 0);
        pnlMember.add(tfdName, gridBagConstraints);

        lblFrom.setText(LocaleManager.getString("from"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 5);
        pnlMember.add(lblFrom, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 6.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 4);
        pnlMember.add(dateFrom, gridBagConstraints);

        lblTo.setText(LocaleManager.getString("to"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 5);
        pnlMember.add(lblTo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 6.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 0);
        pnlMember.add(dateTo, gridBagConstraints);

        scpMoreInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(LocaleManager.getString("moreInfo")), scpMoreInfo.getBorder()));

        txaMoreInfo.setColumns(8);
        txaMoreInfo.setRows(5);
        scpMoreInfo.setViewportView(txaMoreInfo);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        pnlMember.add(scpMoreInfo, gridBagConstraints);

        add(pnlMember, java.awt.BorderLayout.CENTER);

        pnlInventoryItem.setBorder(javax.swing.BorderFactory.createTitledBorder(LocaleManager.getString("inventoryItem")));
        pnlInventoryItem.setLayout(new BorderLayout());

        pnlInventoryItem.add(lblInventoryItem, BorderLayout.CENTER);

        add(pnlInventoryItem, java.awt.BorderLayout.NORTH);

        setToDate();
    }

    private void initMembers() {
        List<Person> borrowers = MemberDataManager.getActiveMembers();

        for (Person borrower : borrowers) {
            if (borrower.isDeleted()) {
                continue;
            }
            BorrowerWrapper borrowerWrapper = new BorrowerWrapper(borrower);
            addMemberName(borrowerWrapper.getBorrowerName(), borrowerWrapper);
            addMemberMemberID(borrowerWrapper.getMemberID(), borrowerWrapper);
        }
        tfdName.setPopupObjects(new ArrayList<String>(borrowerNameMap.keySet()));
        tfdMemberId.setPopupObjects(new ArrayList<String>(borrowerMemberIDMap.keySet()));

        tfdName.addPopupTextFieldListener(new PopupTextFieldListener() {

            @Override
            public void itemChanged(Object object) {
                BorrowerWrapper wrapper = borrowerNameMap.get(object.toString());
                if (wrapper == null) {
                    return;
                }

                tfdMemberId.setText(wrapper.getMemberID());
            }
        });
        tfdMemberId.addPopupTextFieldListener(new PopupTextFieldListener() {

            @Override
            public void itemChanged(Object object) {
                BorrowerWrapper wrapper = borrowerMemberIDMap.get(object.toString());
                if (wrapper == null) {
                    return;
                }

                tfdName.setText(wrapper.getBorrowerName());
            }
        });
    }

    private void addMemberName(String memberName, BorrowerWrapper wrapper) {
        borrowerNameMap.put(memberName, wrapper);
    }

    private void addMemberMemberID(String memberID, BorrowerWrapper wrapper) {
        borrowerMemberIDMap.put(memberID, wrapper);
    }

    public Borrowing getBorrowing() {
        final BorrowerWrapper wrapper = tfdMemberId.getText().trim().isEmpty()
                ? borrowerNameMap.get(tfdName.getText())
                : borrowerMemberIDMap.get(tfdMemberId.getText());

        if (wrapper == null && tfdName.getText().trim().isEmpty()) {
            return null;
        }
        Calendar from = (Calendar) dateFrom.getCalendar().clone();
        Calendar to = (Calendar) dateTo.getCalendar().clone();

        return wrapper == null ? new Borrowing(from, to, tfdName.getText()) : new Borrowing(from, to, wrapper.getBorrower());
    }

    private void setToDate() {
        Calendar c = Calendar.getInstance();

        c.add(Calendar.DATE, InventorySettings.getInstance().getInt(InventorySettings.I_BORROWING_DAYS));
        dateFrom.setCalendar(Calendar.getInstance());
        dateTo.setCalendar(c);
    }

    private class GetMemberContextAction extends ContextTransferAction {

        public GetMemberContextAction() {
            super(InventoryModule.class, MemberModule.class);
            this.putValue(Action.NAME, LocaleManager.getString("choose"));
            this.putValue(Action.SMALL_ICON, IconFactory.getInstance().getIcon("pickUp"));
        }

        @Override
        protected boolean initialize(Context context) {
            dialog.setVisible(false);
            return true;
        }

        @Override
        protected void finalize(Context context) {
            List selectedMembers = context == null ? null : context.getSelectedEntities();

            try {
                if (selectedMembers == null || selectedMembers.size() == 0) {
                    return;
                }
                final Person member = (Person) selectedMembers.get(0);
                final BorrowerWrapper wrapper = new BorrowerWrapper(member);
                tfdName.setText(wrapper.getBorrowerName());
                tfdMemberId.setText(member.getPersonData(MemberData.class).getMemberID());
            } finally {
                dialog.setVisible(true);
            }
        }
    }

    private class BorrowerWrapper {

        private Person borrower;

        public BorrowerWrapper(Person borrower) {
            this.borrower = borrower;
        }

        public String getBorrowerName() {
            return borrower.getFullName(true) + " (" + DateFormat.getDateInstance(DateFormat.SHORT).format(borrower.getPersonData(PrivatePersonData.class).getBirthDate().getTime()) + ")";
        }

        public String getMemberID() {
            return borrower.getPersonData(MemberData.class).getMemberID();
        }

        public Person getBorrower() {
            return borrower;
        }
    }
}
