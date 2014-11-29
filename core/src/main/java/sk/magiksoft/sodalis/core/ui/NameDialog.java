package sk.magiksoft.sodalis.core.ui;

import sk.magiksoft.sodalis.core.locale.LocaleManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author wladimiiir
 */
public class NameDialog extends OkCancelDialog {

    private JTextField nameTextField;

    public NameDialog() {
        initComponents();
    }

    public NameDialog(Window owner) {
        super(owner, LocaleManager.getString("name"));
        initComponents();
    }

    private void initComponents() {
        final JPanel mainPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();

        nameTextField = new JTextField();

        c.gridx = c.gridy = 0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(3, 7, 0, 7);
        mainPanel.add(nameTextField, c);

        setTitle(LocaleManager.getString("typeName"));
        setMainPanel(mainPanel);
        setModal(true);
        setSize(175, 100);
        setResizable(false);
        setLocationRelativeTo(null);

        nameTextField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                resultAction = ACTION_OK;
                setVisible(false);
            }
        });
    }

    public String showDialog(String name) {
        nameTextField.setText(name);
        nameTextField.selectAll();
        nameTextField.requestFocusInWindow();
        setVisible(true);

        return resultAction == ACTION_OK ? nameTextField.getText() : null;
    }

    public String showDialog() {
        return showDialog(null);
    }
}
