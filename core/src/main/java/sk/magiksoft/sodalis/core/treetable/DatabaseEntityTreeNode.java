package sk.magiksoft.sodalis.core.treetable;

import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.entity.Entity;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.*;

/**
 * @author wladimiiir
 */
public abstract class DatabaseEntityTreeNode<T extends DatabaseEntity> implements TreeNode, Entity {

    protected DatabaseEntityTreeNode<T> parentNode;
    protected T entity;
    protected Vector<DatabaseEntityTreeNode<T>> children = new Vector<DatabaseEntityTreeNode<T>>();

    public DatabaseEntityTreeNode(DatabaseEntityTreeNode<T> parentNode, T entity) {
        this.parentNode = parentNode;
        this.entity = entity;
    }

    public T getDatabaseEntity() {
        return entity;
    }

    public DatabaseEntityTreeNode<T> getParentNode() {
        return parentNode;
    }

    public Vector<DatabaseEntityTreeNode<T>> getChildren() {
        return children;
    }

    public void addDatabaseEntity(T entity) {
        children.add(createTreeNode(entity));
    }

    public DatabaseEntityTreeNode<T> removeDatabaseEntity(T entity) {
        DatabaseEntityTreeNode node = getTreeNode(entity);

        if (node != null) {
            children.remove(node);
        }

        return node;
    }

    public DatabaseEntityTreeNode<T> getTreeNode(T entity) {
        for (DatabaseEntityTreeNode<T> databaseEntityTreeNode : children) {
            if (databaseEntityTreeNode.getDatabaseEntity().getId().equals(entity.getId())) {
                return databaseEntityTreeNode;
            }
        }
        return null;
    }

    protected abstract DatabaseEntityTreeNode<T> createTreeNode(T entity);

    @Override
    public TreeNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public TreeNode getParent() {
        return parentNode;
    }

    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return getChildCount() == 0;
    }

    public void removeAllChildren() {
        children.clear();
    }

    public void addChild(DatabaseEntityTreeNode<T> treeNode) {
        children.add(treeNode);
    }

    public TreePath getTreePath() {
        return new TreePath(getPath(this));
    }

    public TreeNode[] getPath(TreeNode node) {
        List<TreeNode> result = node.getParent() == null
                ? new ArrayList<TreeNode>() : new ArrayList<TreeNode>(Arrays.asList(getPath(node.getParent())));

        result.add(node);

        return result.toArray(new TreeNode[]{});
    }

    @Override
    public Enumeration children() {
        return children.elements();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DatabaseEntityTreeNode)) {
            return false;
        }

        return ((DatabaseEntityTreeNode) obj).getDatabaseEntity().equals(entity);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.entity != null ? this.entity.hashCode() : 0);
        return hash;
    }


}
