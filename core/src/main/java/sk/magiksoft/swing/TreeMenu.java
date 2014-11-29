package sk.magiksoft.swing;

import sk.magiksoft.swing.treemenu.TreeMenuActionEvent;
import sk.magiksoft.swing.treemenu.TreeMenuActionListener;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class TreeMenu extends JPanel {

    private JTree tree;
    private DefaultMutableTreeNode currentNode;
    private int selected = -1;
    private Font itemFont = getFont();
    private String backText;
    private List<TreeMenuActionListener> treeMenuActionListeners = new ArrayList<TreeMenuActionListener>();
    private boolean moving = false;
    private DefaultMutableTreeNode leftNode = null;
    private DefaultMutableTreeNode rightNode = null;
    private int leftNodePosition = 0;
    private int rightNodePosition = 0;
    private int fixedItemHeight = 0;

    public TreeMenu(JTree tree, String backText) {
        if (tree == null) {
            throw new IllegalArgumentException("Tree cannot be null!");
        }
        this.backText = backText;
        this.tree = tree;
        this.currentNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
        init();
    }

    public TreeMenu(JTree tree, String backText, int fixedItemHeight) {
        this(tree, backText);
        this.fixedItemHeight = fixedItemHeight;
    }

    public void paintNode(Graphics g, DefaultMutableTreeNode node, int xPosition) {
        int itemCount = node.getChildCount();
        if (!node.isRoot()) {
            itemCount++;
        }
        int itemHeight = fixedItemHeight == 0 ? g.getClipBounds().height / itemCount : fixedItemHeight;

        for (int i = 0; i < itemCount; i++) {
            if (i == selected) {
                g.setColor(new Color(184, 207, 229));
            } else {
                g.setColor(getBackground());
            }
            g.fillRect(xPosition, i * itemHeight, g.getClipBounds().width - 1, itemHeight - 1);
            g.setColor(getForeground());
            g.drawRect(xPosition, i * itemHeight, g.getClipBounds().width - 1, itemHeight - 1);
            g.setFont(itemFont);
            if (i == (itemCount - 1) && !node.isRoot()) {
                g.drawString(backText, xPosition + 15, i * itemHeight + itemHeight / 2 + itemFont.getSize() / 2);
            } else {
                g.drawString(((DefaultMutableTreeNode) node.getChildAt(i)).getUserObject().toString(),
                        xPosition + 15, i * itemHeight + itemHeight / 2 + itemFont.getSize() / 2);
            }
            if (i == (itemCount - 1) && !node.isRoot()) {
                g.setColor(Color.BLACK);
                int x[] = new int[]{
                        xPosition + g.getClipBounds().width - 20,
                        xPosition + g.getClipBounds().width - 20,
                        xPosition + g.getClipBounds().width - 20 - itemFont.getSize()
                };
                int y[] = new int[]{
                        itemHeight * i + itemHeight / 2 - itemFont.getSize() / 2,
                        itemHeight * i + itemHeight / 2 + itemFont.getSize() / 2,
                        itemHeight * i + itemHeight / 2
                };
                g.fillPolygon(x, y, 3);
            } else if (!node.getChildAt(i).isLeaf()) {
                g.setColor(Color.BLACK);
                int x[] = new int[]{
                        xPosition + g.getClipBounds().width - 20 - itemFont.getSize(),
                        xPosition + g.getClipBounds().width - 20 - itemFont.getSize(),
                        xPosition + g.getClipBounds().width - 20
                };
                int y[] = new int[]{
                        itemHeight * i + itemHeight / 2 - itemFont.getSize() / 2,
                        itemHeight * i + itemHeight / 2 + itemFont.getSize() / 2,
                        itemHeight * i + itemHeight / 2
                };
                g.fillPolygon(x, y, 3);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        if (moving) {
            if (leftNode != null) {
                paintNode(g, leftNode, leftNodePosition);
            }
            if (rightNode != null) {
                paintNode(g, rightNode, rightNodePosition);
            }
        } else {
            paintNode(g, currentNode, 0);
        }

    }

    private void init() {
        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                TreeMenu.this.mouseMoved(e);
            }
        });
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                TreeMenu.this.mouseClicked(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                selected = -1;
                updateUI();
            }
        });
    }

    private void mouseMoved(MouseEvent e) {
        int itemCount = currentNode.getChildCount();
        if (!currentNode.isRoot()) {
            itemCount++;
        }

        int itemHeight = fixedItemHeight == 0 ? getHeight() / itemCount : fixedItemHeight;
        for (int i = 0; i < itemCount; i++) {
            int startPointY = itemHeight * i;
            if (e.getY() > startPointY && e.getY() <= (itemHeight * (i + 1))) {
                if (selected != i) {
                    selected = i;
                    updateUI();
                }
                return;
            }
        }
        selected = -1;
        updateUI();
    }

    private void goBackTo(final TreeNode node) {
        new Thread(new Runnable() {

            public void run() {
                leftNode = (DefaultMutableTreeNode) node;
                rightNode = currentNode;
                moving = true;
                rightNodePosition = 0;
                leftNodePosition = rightNodePosition - getWidth();

                while (leftNodePosition < 0) {
                    rightNodePosition += 20;
                    leftNodePosition = (rightNodePosition - getWidth()) > 0 ? 0 : rightNodePosition - getWidth();
                    updateUI();
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ex) {
                        currentNode = (DefaultMutableTreeNode) node;
                        moving = false;
                        updateUI();
                    }
                }

                currentNode = (DefaultMutableTreeNode) node;
                moving = false;
                updateUI();
            }
        }).start();
    }

    private void goForwardTo(final TreeNode node) {
        new Thread(new Runnable() {

            public void run() {
                leftNode = currentNode;
                rightNode = (DefaultMutableTreeNode) node;
                moving = true;
                leftNodePosition = 0;
                rightNodePosition = leftNodePosition + getWidth();

                while (rightNodePosition > 0) {
                    leftNodePosition -= 20;
                    rightNodePosition = (leftNodePosition + getWidth()) < 0 ? 0 : leftNodePosition + getWidth();
                    updateUI();
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ex) {
                        currentNode = (DefaultMutableTreeNode) node;
                        moving = false;
                        updateUI();
                    }
                }

                currentNode = (DefaultMutableTreeNode) node;
                moving = false;
                updateUI();
            }
        }).start();
    }

    private void mouseClicked(MouseEvent e) {
        if (moving) {
            return;
        }
        if (selected == (currentNode.getChildCount()) && !currentNode.isRoot()) {
            goBackTo(currentNode.getParent());
        } else if (selected > -1) {
            if (!currentNode.getChildAt(selected).isLeaf()) {
                goForwardTo(currentNode.getChildAt(selected));
            }
            TreeMenuActionEvent event = new TreeMenuActionEvent(this, e.getID(), currentNode.getChildAt(selected), System.currentTimeMillis(), e.getModifiers());
            fireTreeMenuActionPerformed(event);
        }
    }

    private void fireTreeMenuActionPerformed(TreeMenuActionEvent e) {
        for (TreeMenuActionListener treeMenuActionListener : treeMenuActionListeners) {
            treeMenuActionListener.actionPerformed(e);
        }
    }

    public Font getItemFont() {
        return itemFont;
    }

    public void setItemFont(Font itemFont) {
        this.itemFont = itemFont;
    }

    public List<TreeMenuActionListener> getTreeMenuActionListeners() {
        return treeMenuActionListeners;
    }

    public void addTreeMenuActionListener(TreeMenuActionListener listener) {
        treeMenuActionListeners.add(listener);
    }

    public boolean removeTreeMenuActionListener(TreeMenuActionListener listener) {
        return treeMenuActionListeners.remove(listener);
    }

    public boolean isFixedItemHeight() {
        return fixedItemHeight == 0;
    }

    public void setFixedItemHeight(int fixedItemHeight) {
        this.fixedItemHeight = fixedItemHeight;
    }

    public static void main(String[] args) {
        JTree tree = new JTree(new Object[]{new Object[]{"Jahoda", "Vanilka"}, "Klobasa", "Marhula"});
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 100);
        frame.setLocationRelativeTo(null);
        frame.add(new TreeMenu(tree, "Späť", 50), BorderLayout.CENTER);
        System.out.println(tree.getModel().getChild(tree.getModel().getRoot(), 2).getClass().getName());

        frame.setVisible(true);

    }
}
