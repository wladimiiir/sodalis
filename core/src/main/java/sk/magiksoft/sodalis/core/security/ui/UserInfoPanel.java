package sk.magiksoft.sodalis.core.security.ui;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.security.CryptoUtils;
import sk.magiksoft.sodalis.core.security.entity.SodalisUser;
import sk.magiksoft.sodalis.core.ui.Validator;
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Arrays;

/**
 * @author wladimiiir
 */
public class UserInfoPanel extends AbstractInfoPanel {

    private SodalisUser sodalisUser;
    private Validator passwordValidator;

    @Override
    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        lblUserName = new JLabel();
        lblPassword = new JLabel();
        lblPasswordAgain = new JLabel();
        tfdUserName = new JTextField();
        pfdPassword = new JPasswordField();
        pfdPasswordAgain = new JPasswordField();

        lblUserName.setText(LocaleManager.getString("username"));
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        c.weightx = 1.0;
        c.insets = new Insets(2, 3, 0, 0);
        layoutPanel.add(lblUserName, c);

        lblPassword.setText(LocaleManager.getString("password"));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.EAST;
        c.weightx = 1.0;
        c.insets = new Insets(2, 3, 0, 0);
        layoutPanel.add(lblPassword, c);

        lblPasswordAgain.setText(LocaleManager.getString("confirmPassword"));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.EAST;
        c.weightx = 1.0;
        c.insets = new Insets(2, 3, 0, 0);
        layoutPanel.add(lblPasswordAgain, c);

        tfdUserName.setPreferredSize(new Dimension(130, 19));
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.insets = new Insets(2, 3, 0, 0);
        layoutPanel.add(tfdUserName, c);

        pfdPassword.setPreferredSize(new Dimension(130, 19));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.insets = new Insets(2, 3, 0, 0);
        layoutPanel.add(pfdPassword, c);

        pfdPasswordAgain.setPreferredSize(new Dimension(130, 19));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.insets = new Insets(2, 3, 0, 0);
        layoutPanel.add(pfdPasswordAgain, c);

        initComponents2();

        return layoutPanel;
    }

    private void initComponents2() {
        passwordValidator = new Validator(pfdPasswordAgain) {

            @Override
            public boolean isValid(Object object) {
                return Arrays.equals(pfdPassword.getPassword(), pfdPasswordAgain.getPassword());
            }
        };

        final DocumentListener passwordDocListener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                passwordValidator.validate(null);
                fireEditing();
                fireValid(passwordValidator.isValid(null));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                passwordValidator.validate(null);
                fireEditing();
                fireValid(passwordValidator.isValid(null));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                passwordValidator.validate(null);
                fireEditing();
                fireValid(passwordValidator.isValid(null));
            }
        };

        pfdPassword.getDocument().addDocumentListener(passwordDocListener);
        pfdPasswordAgain.getDocument().addDocumentListener(passwordDocListener);
        tfdUserName.getDocument().addDocumentListener(documentListener);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel lblPassword;
    private JLabel lblPasswordAgain;
    private JLabel lblUserName;
    private JPasswordField pfdPassword;
    private JPasswordField pfdPasswordAgain;
    private JTextField tfdUserName;
    // End of variables declaration//GEN-END:variables

    @Override
    public String getPanelName() {
        return LocaleManager.getString("userData");
    }

    @Override
    public void setupObject(Object object) {
        SodalisUser user = getNormalizedObject(SodalisUser.class, object);

        if (user == null) {
            return;
        }

        user.setUserName(tfdUserName.getText());
        if (pfdPassword.getPassword().length > 0 || user.getPassword() == null) {
            user.setPassword(CryptoUtils.makeDigest(String.valueOf(pfdPassword.getPassword()), user.getUserName()));
        }
    }

    @Override
    public void setupPanel(Object object) {
        sodalisUser = getNormalizedObject(SodalisUser.class, object);
        if (sodalisUser == null) {
            return;
        }

        initialized = false;
    }

    @Override
    public void initData() {
        if (initialized) {
            return;
        }

        tfdUserName.setText(sodalisUser.getUserName().trim());
        pfdPassword.setText(null);
        pfdPasswordAgain.setText(null);

        initialized = true;
    }
}
