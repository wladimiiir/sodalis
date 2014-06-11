
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.swing;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import sk.magiksoft.swing.tree.CheckBoxTreeComponent;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 * @author wladimiiir
 */
public class TreeComboBox extends JComboBox {

    private DefaultTreeComboBoxModel treeComboBoxModel;
    private JTree tree;

    public TreeComboBox() {
        this(new DefaultTreeComboBoxModel(new DefaultMutableTreeNode()));
    }

    public TreeComboBox(DefaultTreeComboBoxModel treeComboBoxModel) {
        this.treeComboBoxModel = treeComboBoxModel;
        initTree();
        initComboBox();
    }

    public JTree getTree() {
        return tree;
    }

    public DefaultTreeComboBoxModel getTreeComboBoxModel() {
        return treeComboBoxModel;
    }

    public void addTreeSelectionListener(TreeSelectionListener listener) {
        tree.addTreeSelectionListener(listener);
    }

    protected ComboBoxUI createComboBoxUI() {
        return new PlasticTreeComboBoxUI();
    }

    private void initTree() {
        tree = new JTree(treeComboBoxModel);
        tree.setShowsRootHandles(true);
        tree.setRootVisible(false);
        tree.setCellEditor(createCellEditor());
        tree.setCellRenderer(createCellRenderer());
        tree.setEditable(true);
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                repaint();
            }
        });
    }

    protected TreeCellRenderer createCellRenderer() {
        return new CheckBoxTreeComponent();
    }

    protected TreeCellEditor createCellEditor() {
        return new CheckBoxTreeComponent();
    }

    private void initComboBox() {
        setUI(createComboBoxUI());
        setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                ((JLabel) c).setText(getSelectionText());

                return c;
            }
        });
    }


    private String getSelectionText() {
        StringBuilder selectionText = new StringBuilder();
        TreePath[] selected = tree.getSelectionPaths();

        if (selected == null) {
            return selectionText.toString();
        }
        for (TreePath treePath : selected) {
            if (selectionText.length() > 0) {
                selectionText.append(", ");
            }

            selectionText.append(treePath.getLastPathComponent().toString());
        }

        return selectionText.toString();

    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        TreeComboBox comboBox = new TreeComboBox();
        JFrame frame = new JFrame();

        comboBox.getTreeComboBoxModel().setRoot(createRoot());
        comboBox.setPreferredSize(new Dimension(320, 20));
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.getContentPane().add(comboBox);
        frame.setSize(640, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static DefaultMutableTreeNode createRoot() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");

        root.add(new DefaultMutableTreeNode("prvy"));
        ((DefaultMutableTreeNode) root.getChildAt(0)).add(new DefaultMutableTreeNode("prvy"));
        ((DefaultMutableTreeNode) root.getChildAt(0)).add(new DefaultMutableTreeNode("prvy"));
        ((DefaultMutableTreeNode) root.getChildAt(0)).add(new DefaultMutableTreeNode("prvy"));
        root.add(new DefaultMutableTreeNode("druhy"));
        ((DefaultMutableTreeNode) root.getChildAt(1)).add(new DefaultMutableTreeNode("druhy"));
        ((DefaultMutableTreeNode) root.getChildAt(1)).add(new DefaultMutableTreeNode("druhy"));
        ((DefaultMutableTreeNode) root.getChildAt(1)).add(new DefaultMutableTreeNode("druhy"));
        root.add(new DefaultMutableTreeNode("treti"));
        ((DefaultMutableTreeNode) root.getChildAt(2)).add(new DefaultMutableTreeNode("treti"));
        ((DefaultMutableTreeNode) root.getChildAt(2)).add(new DefaultMutableTreeNode("treti"));
        ((DefaultMutableTreeNode) root.getChildAt(2)).add(new DefaultMutableTreeNode("treti"));
        root.add(new DefaultMutableTreeNode("stvrty"));
        ((DefaultMutableTreeNode) root.getChildAt(3)).add(new DefaultMutableTreeNode("stvrty"));
        ((DefaultMutableTreeNode) root.getChildAt(3)).add(new DefaultMutableTreeNode("stvrty"));
        ((DefaultMutableTreeNode) root.getChildAt(3)).add(new DefaultMutableTreeNode("stvrty"));
        ((DefaultMutableTreeNode) root.getChildAt(3)).add(new DefaultMutableTreeNode("stvrty"));
        ((DefaultMutableTreeNode) root.getChildAt(3)).add(new DefaultMutableTreeNode("stvrty"));
        ((DefaultMutableTreeNode) root.getChildAt(3)).add(new DefaultMutableTreeNode("stvrty"));

        return root;
    }
}