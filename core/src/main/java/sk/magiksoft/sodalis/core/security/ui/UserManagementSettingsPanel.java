package sk.magiksoft.sodalis.core.security.ui;

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager;
import sk.magiksoft.sodalis.core.exception.VetoException;
import sk.magiksoft.sodalis.core.factory.EntityFactory;
import sk.magiksoft.sodalis.icon.IconManager;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.security.LoginManagerService;
import sk.magiksoft.sodalis.core.security.SecurityDataManager;
import sk.magiksoft.sodalis.core.security.entity.SodalisUser;
import sk.magiksoft.sodalis.core.security.entity.User;
import sk.magiksoft.sodalis.core.security.util.SecurityUtils;
import sk.magiksoft.sodalis.core.settings.SettingsPanel;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.sodalis.core.ui.controlpanel.ControlPanelAdapter;

import javax.security.auth.Subject;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class UserManagementSettingsPanel extends javax.swing.JPanel implements SettingsPanel {

    private DefaultListModel userListModel = new DefaultListModel();
    private UserControlPanel userControlPanel = new UserControlPanel();
    private List<SodalisUser> usersToRemove = new ArrayList<SodalisUser>();
    private List<SodalisUser> usersToAdd = new ArrayList<SodalisUser>();
    private SodalisUser currentUser;
    private boolean adminMode = false;

    /**
     * Creates new form UserManagementSettingsPanel
     */
    public UserManagementSettingsPanel() {
        initComponents();
        initComponents2();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        scpUsers = new JScrollPane();
        lstUsers = new JList();
        pnlControlPanel = new JPanel();
        pnlButtons = new JPanel();
        btnAddUser = new JButton();
        btnRemoveUser = new JButton();

        setLayout(new GridBagLayout());

        lstUsers.setModel(userListModel);
        lstUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstUsers.setMinimumSize(new Dimension(200, 0));
        lstUsers.setPreferredSize(new Dimension(120, 85));
        lstUsers.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                lstUsersValueChanged(evt);
            }
        });
        scpUsers.setViewportView(lstUsers);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(3, 3, 0, 3);
        add(scpUsers, gridBagConstraints);

        pnlControlPanel.setLayout(new BorderLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(3, 0, 3, 3);
        add(pnlControlPanel, gridBagConstraints);
        pnlControlPanel.add(userControlPanel, BorderLayout.CENTER);

        pnlButtons.setLayout(new GridLayout(1, 0, 3, 0));

        btnAddUser.setIcon(IconManager.getInstance().getIcon("add"));
        btnAddUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnAddUserActionPerformed(evt);
            }
        });
        pnlButtons.add(btnAddUser);

        btnRemoveUser.setIcon(IconManager.getInstance().getIcon("remove"));
        btnRemoveUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnRemoveUserActionPerformed(evt);
            }
        });
        pnlButtons.add(btnRemoveUser);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(0, 3, 3, 3);
        add(pnlButtons, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void lstUsersValueChanged(ListSelectionEvent evt) {//GEN-FIRST:event_lstUsersValueChanged
        SodalisUser sodalisUser = (SodalisUser) lstUsers.getSelectedValue();

        if (sodalisUser != null) {
            userControlPanel.setVisible(true);
            userControlPanel.setupControlPanel(sodalisUser);
        } else {
            userControlPanel.setVisible(false);
        }
    }//GEN-LAST:event_lstUsersValueChanged

    private void btnAddUserActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnAddUserActionPerformed
        SodalisUser sodalisUser = EntityFactory.getInstance().createEntity(SodalisUser.class);

        usersToAdd.add(sodalisUser);
        userListModel.addElement(sodalisUser);
        lstUsers.setSelectedIndex(userListModel.indexOf(sodalisUser));
    }//GEN-LAST:event_btnAddUserActionPerformed

    private void btnRemoveUserActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnRemoveUserActionPerformed
        SodalisUser user = (SodalisUser) lstUsers.getSelectedValue();

        if (user == null) {
            return;
        }

        if (ISOptionPane.showConfirmDialog(this, LocaleManager.getString("userRemoveConfirm"),
                user.getUserName(), ISOptionPane.YES_NO_OPTION) != ISOptionPane.YES_OPTION) {
            return;
        }

        usersToRemove.remove(user);
        userListModel.removeElement(user);
    }//GEN-LAST:event_btnRemoveUserActionPerformed

    private void initComponents2() {
        lstUsers.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (c instanceof JLabel && value instanceof SodalisUser) {
                    ((JLabel) c).setText(((SodalisUser) value).getUserName());
                    if (((SodalisUser) value).isAdmin()) {
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    }
                }

                return c;
            }
        });
        userControlPanel.addControlPanelListener(new ControlPanelAdapter() {

            @Override
            public void saved() {
                lstUsers.repaint();
            }
        });
    }

    @Override
    public String getSettingsPanelName() {
        return LocaleManager.getString("userManagement");
    }

    @Override
    public JComponent getSwingComponent() {
        return this;
    }

    @Override
    public void reloadSettings() {
        List<SodalisUser> users;


        usersToAdd.clear();
        usersToRemove.clear();
        userListModel.clear();
        if (adminMode) {
            users = SecurityDataManager.getInstance().getDatabaseEntities(SodalisUser.class);
            for (SodalisUser sodalisUser : users) {
                if (sodalisUser.isAdmin()) {
                    userListModel.add(0, sodalisUser);
                } else {
                    userListModel.addElement(sodalisUser);
                }
            }
            lstUsers.setSelectedIndex(-1);
        } else if (currentUser != null) {
            userListModel.addElement(currentUser);
            lstUsers.setSelectedIndex(0);
        }
    }

    @Override
    public boolean saveSettings() {
        final ClientDataManager manager = SecurityDataManager.getInstance();
        final String sessionID = manager.registerNewSession();

        for (int i = 0; i < userListModel.getSize(); i++) {
            SodalisUser sodalisUser = (SodalisUser) userListModel.getElementAt(i);
            if (sodalisUser.getId() == null) {
                continue;
            }
            manager.updateDatabaseEntity(sessionID, sodalisUser);
        }
        for (SodalisUser sodalisUser : usersToRemove) {
            if (sodalisUser.getId() == null) {
                continue;
            }
            manager.removeDatabaseEntity(sessionID, sodalisUser);
        }

        for (SodalisUser sodalisUser : usersToAdd) {
            if (sodalisUser.getUserName() == null || sodalisUser.getUserName().trim().isEmpty()) {
                continue;
            }
            manager.addDatabaseEntity(sessionID, sodalisUser);
        }
        manager.closeSession(sessionID);

        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnAddUser;
    private JButton btnRemoveUser;
    private JList lstUsers;
    private JPanel pnlButtons;
    private JPanel pnlControlPanel;
    private JScrollPane scpUsers;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setup(Subject subject) throws VetoException {
        currentUser = SodalisApplication.get().getService(LoginManagerService.class, LoginManagerService.SERVICE_NAME).getUserForUID((String) SecurityUtils.
                getCredential(subject, User.CREDENTIAL_USER_UID));
        adminMode = currentUser != null && currentUser.isAdmin();
        scpUsers.setVisible(adminMode);
        pnlButtons.setVisible(adminMode);
    }
}
