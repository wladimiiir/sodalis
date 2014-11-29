package sk.magiksoft.sodalis.core.ui;

import sk.magiksoft.sodalis.core.locale.LocaleManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author wladimiiir
 */
public class PasswordDialog extends OkCancelDialog {

    private JPasswordField passwordField;

    public PasswordDialog() {
        initComponents(null);
    }

    public PasswordDialog(Window owner) {
        super(owner);
        initComponents(null);
    }

    public PasswordDialog(String title) {
        initComponents(title);
    }

    private void initComponents(String title) {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        passwordField = new JPasswordField();

        c.gridx = c.gridy = 0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(3, 7, 0, 7);
        mainPanel.add(passwordField, c);

        setTitle(title == null ? LocaleManager.getString("typeName") : title);
        setMainPanel(mainPanel);
        setModal(true);
        setSize(175, 100);
        setResizable(false);
        setLocationRelativeTo(null);

        passwordField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                resultAction = ACTION_OK;
                setVisible(false);
            }
        });
    }

    public String showDialog() {
        setVisible(true);

        return resultAction == ACTION_OK ? new String(passwordField.getPassword()) : null;
    }
}
