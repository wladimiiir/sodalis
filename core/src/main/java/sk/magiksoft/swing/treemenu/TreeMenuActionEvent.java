package sk.magiksoft.swing.treemenu;

import javax.swing.tree.TreeNode;
import java.awt.event.ActionEvent;

/**
 * @author wladimiiir
 */
public class TreeMenuActionEvent extends ActionEvent {
    private TreeNode node;

    public TreeMenuActionEvent(Object source, int id, TreeNode node) {
        super(source, id, "TreeMenuAction");
        this.node = node;
    }

    public TreeMenuActionEvent(Object source, int id, TreeNode node, long when, int modifiers) {
        super(source, id, "TreeMenuAction", when, modifiers);
        this.node = node;
    }

    public TreeNode getTreeNode() {
        return node;
    }
}
