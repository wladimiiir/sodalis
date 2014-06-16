
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CategoryInfoPanel.java
 *
 * Created on 21.3.2009, 20:14:58
 */

package sk.magiksoft.sodalis.category.ui;

import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.ui.controlpanel.AbstractInfoPanel;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;

/**
 * @author wladimiiir
 */
public class CategoryInfoPanel extends AbstractInfoPanel {

    private Category category;

    /**
     * Creates new form CategoryInfoPanel
     */
    public CategoryInfoPanel() {
        super(Category.class);
    }

    @Override
    protected Component createLayout() {
        final JPanel layoutPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        jLabel1 = new JLabel();
        tfdName = new JTextField();
        jScrollPane1 = new JScrollPane();
        txaDescription = new JTextArea();
        jLabel2 = new JLabel();
        jPanel1 = new JPanel();

        jLabel1.setText(LocaleManager.getString("name"));
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(3, 3, 0, 0);
        layoutPanel.add(jLabel1, c);

        tfdName.getDocument().addDocumentListener(documentListener);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(3, 3, 0, 3);
        layoutPanel.add(tfdName, c);

        txaDescription.setColumns(20);
        txaDescription.setRows(5);
        txaDescription.getDocument().addDocumentListener(documentListener);
        jScrollPane1.setViewportView(txaDescription);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.insets = new Insets(3, 3, 3, 3);
        layoutPanel.add(jScrollPane1, c);

        jLabel2.setText(LocaleManager.getString("description"));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new Insets(3, 3, 3, 0);
        layoutPanel.add(jLabel2, c);

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        layoutPanel.add(jPanel1, c);

        return layoutPanel;
    }

    @Override
    public String getPanelName() {
        return LocaleManager.getString("category");
    }

    @Override
    public void setupObject(Object object) {
        if (!(object instanceof Category)) {
            return;
        }
        Category c = (Category) object;

        c.setName(tfdName.getText());
        c.setDescription(txaDescription.getText());
    }

    @Override
    public void setupPanel(Object object) {
        if (!(object instanceof Category)) {
            return;
        }

        this.category = (Category) object;
        initialized = false;
    }

    @Override
    public void initData() {
        if (initialized || category == null) {
            return;
        }

        tfdName.setText(category.getName());
        txaDescription.setText(category.getDescription());
        tfdName.setEditable(category.getParentCategory() != null);
        tfdName.setBackground(Color.WHITE);
        txaDescription.setEditable(category.getParentCategory() != null);
        txaDescription.setBackground(Color.WHITE);

        initialized = true;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTextField tfdName;
    private JTextArea txaDescription;
    // End of variables declaration//GEN-END:variables

}