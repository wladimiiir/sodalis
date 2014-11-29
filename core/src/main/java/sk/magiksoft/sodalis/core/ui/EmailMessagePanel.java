package sk.magiksoft.sodalis.core.ui;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import sk.magiksoft.sodalis.core.locale.LocaleManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author wladimiiir
 */
public class EmailMessagePanel extends javax.swing.JPanel {

    /**
     * Creates new form EmailMessagePanel
     */
    public EmailMessagePanel() {
        initComponents();
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
        tfdSubject = new JTextField();
        jLabel2 = new JLabel();
        jScrollPane1 = new JScrollPane();
        txaMessage = new JTextArea();

        setName("Form"); // NOI18N
        setLayout(new GridBagLayout());

        jLabel1.setText(LocaleManager.getString("subject"));
        jLabel1.setName("jLabel1"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(10, 10, 2, 0);
        add(jLabel1, gridBagConstraints);

        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(EmailMessagePanel.class);
        tfdSubject.setText(LocaleManager.getString("subject")); // NOI18N
        tfdSubject.setName("tfdSubject"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(0, 10, 2, 10);
        add(tfdSubject, gridBagConstraints);

        jLabel2.setText(LocaleManager.getString("message"));
        jLabel2.setName("jLabel2"); // NOI18N
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(5, 10, 2, 0);
        add(jLabel2, gridBagConstraints);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        txaMessage.setColumns(20);
        txaMessage.setRows(5);
        txaMessage.setName("txaMessage"); // NOI18N
        jScrollPane1.setViewportView(txaMessage);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(0, 10, 10, 10);
        add(jScrollPane1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void requestFocus() {
        tfdSubject.selectAll();
        tfdSubject.requestFocus();
    }

    public String getSubject() {
        return tfdSubject.getText();
    }

    public String getMessage() {
        return txaMessage.getText();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JScrollPane jScrollPane1;
    private JTextField tfdSubject;
    private JTextArea txaMessage;
    // End of variables declaration//GEN-END:variables

}
