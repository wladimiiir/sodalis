package sk.magiksoft.sodalis.core.security.ui;

import sk.magiksoft.sodalis.core.locale.LocaleManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author wladimiiir
 */
public class LoginPanel extends JPanel {

    /**
     * Creates new form LoginPanel
     */
    public LoginPanel() {
        initComponents();
        initListeners();
    }

    public void setLoginAction(Action action) {
        btnLogin.setAction(action);
    }

    public void setExitAction(Action action) {
        btnExit.setAction(action);
    }

    public String getUserName() {
        return tfdUsername.getText();
    }

    public char[] getPassword() {
        return pwfPassword.getPassword();
    }

    public void showConfirmPassword(boolean show) {
        lblConfirmPass.setVisible(show);
        pwfConfirmPass.setVisible(show);
        refreshButtons();
    }

    public void resetPassword() {
        pwfPassword.setText(null);
        pwfConfirmPass.setText(null);
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

        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        tfdUsername = new JTextField(System.getProperty("defaultUser", ""));
        jPanel1 = new JPanel();
        btnLogin = new JButton();
        btnExit = new JButton();
        pwfPassword = new JPasswordField(System.getProperty("defaultPassword", ""));
        lblConfirmPass = new JLabel();
        pwfConfirmPass = new JPasswordField();

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());

        jLabel1.setText(LocaleManager.getString("username"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(jLabel1, gridBagConstraints);

        jLabel2.setText(LocaleManager.getString("password"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(jLabel2, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(tfdUsername, gridBagConstraints);

        btnLogin.setText(LocaleManager.getString("login"));
        jPanel1.add(btnLogin);

        btnExit.setText(LocaleManager.getString("Exit"));
        jPanel1.add(btnExit);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 0);
        add(jPanel1, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(pwfPassword, gridBagConstraints);

        lblConfirmPass.setText(LocaleManager.getString("confirmPassword"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(0, 3, 3, 3);
        add(lblConfirmPass, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 3, 3, 3);
        add(pwfConfirmPass, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnExit;
    private JButton btnLogin;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JPanel jPanel1;
    private JLabel lblConfirmPass;
    private JPasswordField pwfConfirmPass;
    private JPasswordField pwfPassword;
    private JTextField tfdUsername;
    // End of variables declaration//GEN-END:variables

    private void initListeners() {
        final DocumentListener listener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                refreshButtons();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                refreshButtons();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                refreshButtons();
            }
        };
        tfdUsername.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                pwfPassword.grabFocus();
            }
        });
        pwfPassword.getDocument().addDocumentListener(listener);
        pwfConfirmPass.getDocument().addDocumentListener(listener);
        pwfPassword.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnLogin.doClick();
            }
        });
        pwfConfirmPass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnLogin.doClick();
                pwfPassword.requestFocus();
            }
        });
    }

    private boolean checkNewUser() {
        return !tfdUsername.getText().isEmpty() && pwfPassword.getPassword().length > 0
                && String.valueOf(pwfPassword.getPassword()).equals(String.valueOf(pwfConfirmPass.getPassword()));
    }

    private void refreshButtons() {
        btnLogin.setEnabled(!pwfConfirmPass.isVisible() || checkNewUser());
    }
}
