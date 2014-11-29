package sk.magiksoft.sodalis.category.ui;

import sk.magiksoft.sodalis.category.CategoryDataManager;
import sk.magiksoft.sodalis.category.CategoryManager;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.module.Module;
import sk.magiksoft.swing.DefaultTreeComboBoxModel;
import sk.magiksoft.swing.TreeComboBox;
import sk.magiksoft.swing.tree.CheckBoxTreeComponent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author wladimiiir
 */
public class CategoryTreeComboBox extends JComponent implements DataListener, TreeSelectionListener, ActionListener {

    private TreeComboBox tcbCategory;
    private JCheckBox chbEnabled;
    private Class<? extends Module> moduleClass;
    private boolean adjusting = false;

    public CategoryTreeComboBox(Class<? extends Module> moduleClass) {
        this.moduleClass = moduleClass;
        initComponents();
        reloadCategoryTree();
        CategoryDataManager.getInstance().addDataListener(this);
    }

    private void reloadCategoryTree() {
        adjusting = true;
        final List<Category> selectedCategories = getSelectedCategories();
        getTreeComboBoxModel().setRoot(new CategoryTreeNode(CategoryManager.getInstance().getRootCategory(moduleClass, false)));
        setSelectedCategories(selectedCategories);
        adjusting = false;
    }

    private DefaultTreeComboBoxModel getTreeComboBoxModel() {
        return tcbCategory.getTreeComboBoxModel();
    }

    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    private void fireStateChanged() {
        ChangeListener[] listeners = listenerList.getListeners(ChangeListener.class);
        ChangeEvent e = new ChangeEvent(this);

        for (ChangeListener changeListener : listeners) {
            changeListener.stateChanged(e);
        }
    }

    public List<Category> getSelectedCategories() {
        final List<Category> categories = new ArrayList<Category>();
        final TreePath[] treePaths = tcbCategory.getTree().getSelectionPaths();

        if (/*!chbEnabled.isSelected() ||*/ treePaths == null) {
            return categories;
        }
        for (TreePath treePath : treePaths) {
            categories.add(((CategoryTreeNode) treePath.getLastPathComponent()).getDatabaseEntity());
        }

        return categories;
    }

    public void setSelectedCategories(List<Category> categories) {
        tcbCategory.getTree().clearSelection();
        chbEnabled.setSelected(!categories.isEmpty());

        for (Category category : categories) {
            TreePath treePath = getTreePathForCategory(category);

            if (treePath == null) {
                continue;
            }
            tcbCategory.getTree().addSelectionPath(treePath);
        }
    }

    private TreePath getTreePathForCategory(Category category) {
        CategoryTreeNode treeNode = getNodeForCategory((CategoryTreeNode) getTreeComboBoxModel().getRoot(), category);

        if (treeNode == null) {
            return null;
        }

        return treeNode.getTreePath();
    }

    private CategoryTreeNode getNodeForCategory(CategoryTreeNode node, Category category) {
        if (node.getDatabaseEntity().id() != null && category.id() != null
                && node.getDatabaseEntity().id().equals(category.id())) {
            return node;
        }

        final Enumeration<CategoryTreeNode> children = node.children();
        while (children.hasMoreElements()) {
            CategoryTreeNode categoryTreeNode = children.nextElement();
            if ((node = getNodeForCategory(categoryTreeNode, category)) != null) {
                return node;
            }
        }

        return null;
    }

    @Override
    public void entitiesAdded(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (object instanceof Category) {
                reloadCategoryTree();
                return;
            }
        }
    }

    @Override
    public void entitiesRemoved(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (object instanceof Category) {
                reloadCategoryTree();
                return;
            }
        }
    }

    @Override
    public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (object instanceof Category) {
                reloadCategoryTree();
                return;
            }
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if (adjusting) {
            return;
        }

        fireStateChanged();
    }

    @Override
    public boolean isOpaque() {
        return true;
    }

    private void initComponents() {
        tcbCategory = new TreeComboBox() {
            @Override
            public void paint(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(g.getClipBounds().x, g.getClipBounds().y, g.getClipBounds().width, g.getClipBounds().height);

                super.paint(g);
            }

            @Override
            protected TreeCellRenderer createCellRenderer() {
                return new CheckBoxTreeComponent() {
                    private JLabel label = new JLabel();

                    @Override
                    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                        if (leaf) {
                            return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                        }
                        label.setText(value.toString());
                        return label;
                    }
                };
            }

            @Override
            protected TreeCellEditor createCellEditor() {
                return new CheckBoxTreeComponent() {
                    private JLabel label = new JLabel();

                    @Override
                    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
                        if (leaf) {
                            return super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
                        }

                        label.setText(value.toString());
                        return label;
                    }
                };
            }
        };
        tcbCategory.setFocusable(false);
        tcbCategory.setBackground(Color.WHITE);
        tcbCategory.setPreferredSize(new Dimension(170, 23));
        tcbCategory.setBorder(BorderFactory.createEmptyBorder());
        tcbCategory.addTreeSelectionListener(this);
        setBorder(BorderFactory.createEmptyBorder());

        chbEnabled = new JCheckBox();
        chbEnabled.setOpaque(false);
        chbEnabled.setHorizontalAlignment(SwingConstants.CENTER);
        chbEnabled.setVerticalAlignment(SwingConstants.CENTER);
        chbEnabled.setFocusable(false);
        chbEnabled.setBackground(Color.WHITE);
        chbEnabled.setPreferredSize(new Dimension(16, 23));
        chbEnabled.setSelected(true);
        chbEnabled.addActionListener(this);

        setLayout(new BorderLayout());
//        add(chbEnabled, BorderLayout.WEST);
        add(tcbCategory, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tcbCategory.setEnabled(chbEnabled.isSelected());
        if (adjusting) {
            return;
        }
        chbEnabled.revalidate();
        chbEnabled.repaint();
    }

    public boolean isSelected() {
        return chbEnabled.isSelected();
    }
}
