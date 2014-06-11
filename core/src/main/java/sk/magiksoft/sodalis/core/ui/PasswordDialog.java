
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import sk.magiksoft.sodalis.core.locale.LocaleManager;

/**
 *
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