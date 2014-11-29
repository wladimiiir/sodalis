package sk.magiksoft.swing;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.treetable.TreeTableModel;
import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.swing.table.SelectionListener;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author wladimiiir
 */
public class ISTreeTable extends JXTreeTable {

    public ISTreeTable() {
        initTable();
    }

    public ISTreeTable(TreeTableModel treeModel) {
        super(treeModel);
        initTable();
    }

    public void addSelectionListener(SelectionListener listener) {
        listenerList.add(SelectionListener.class, listener);
    }

    public void removeSelectionListener(SelectionListener l) {
        listenerList.remove(SelectionListener.class, l);
    }

    private boolean fireSelectionWillBeChanged() {
        final SelectionListener[] listeners = listenerList.getListeners(SelectionListener.class);

        for (int i = 0; i < listeners.length; i++) {
            SelectionListener selectionListener = listeners[i];
            if (!selectionListener.selectionWillBeChanged()) {
                return false;
            }
        }
        return true;
    }

    private void initTable() {
        final JPopupMenu popupMenu = new JPopupMenu();

        popupMenu.add(new AbstractAction(LocaleManager.getString("expand")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                expandAll();
            }
        });
        popupMenu.add(new AbstractAction(LocaleManager.getString("collapse")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                collapseAll();
            }
        });

        setSelectionBackground(ColorList.TABLE_SELECTION_BACKGROUND);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row = rowAtPoint(e.getPoint());

                if (e.getClickCount() == 1 && SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(ISTreeTable.this, e.getX(), e.getY());
                } else if (row != -1 && SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                    changeSelection(row, 0, false, false);
                }
            }
        });

        setHighlighters(new ColorHighlighter(new HighlightPredicate() {

            @Override
            public boolean isHighlighted(Component component, ComponentAdapter adapter) {
                return adapter.row % 2 != 0;
            }
        }, ColorList.LIGHTER_BLUE, Color.BLACK));
        setShowsRootHandles(true);
    }

    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        if (rowIndex != getSelectedRow() && !fireSelectionWillBeChanged()) {
            return;
        }
        super.changeSelection(rowIndex, columnIndex, toggle, extend);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
        Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);

        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            if (jc instanceof JLabel) {
                jc.setToolTipText(((JLabel) c).getText());
            } else {
                jc.setToolTipText(getValueAt(rowIndex, vColIndex).toString());
            }
            setComponentAlignment(jc, vColIndex);
            jc.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
        }
//        if (Arrays.binarySearch(getSelectedRows(), rowIndex) < 0) {
//            c.setBackground(rowIndex % 2 == 0 ? Color.WHITE : ColorFactory.COLOR_LIGHTER_BLUE);
//        }
        return c;
    }

    private void setComponentAlignment(JComponent jc, int column) {
        Class columnClass = getColumnClass(column);

        if (columnClass == ISTable.RIGHT_ALIGNMENT_CLASS) {
            jc.setAlignmentY(JComponent.RIGHT_ALIGNMENT);
        } else if (columnClass == ISTable.CENTER_ALIGNMENT_CLASS) {
            jc.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        } else if (columnClass == ISTable.LEFT_ALIGNMENT_CLASS) {
            jc.setAlignmentY(JComponent.LEFT_ALIGNMENT);
        }
    }
}
