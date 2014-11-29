package sk.magiksoft.sodalis.core.security.ui;

import sk.magiksoft.sodalis.core.ui.controlpanel.DefaultControlPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author wladimiiir
 */
public class UserControlPanel extends DefaultControlPanel {

    public UserControlPanel() {
        super("user");
        initComponents();
    }

    private void initComponents() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        removeAll();
        add(tbpControlPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
    }


}
