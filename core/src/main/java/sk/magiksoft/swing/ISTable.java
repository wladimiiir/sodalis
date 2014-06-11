
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

import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.sodalis.core.imex.ImExManager;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.sodalis.core.table.SortableTableModel;
import sk.magiksoft.swing.table.ColumnVisibilityTableController;
import sk.magiksoft.swing.table.SelectionListener;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * @author wladimiiir
 */
public class ISTable extends JTable {

    public static final Class RIGHT_ALIGNMENT_CLASS = Number.class;
    public static final Class CENTER_ALIGNMENT_CLASS = Component.class;
    public static final Class LEFT_ALIGNMENT_CLASS = String.class;
    public static final int NOT_SORTED = 0;
    public static final int ASCENDING = 1;
    public static final int DESCENDING = 2;
    private Directive directive = new Directive(-1, NOT_SORTED);
    private ColumnVisibilityTableController columnVisibilityTableController = new ColumnVisibilityTableController();

    public ISTable() {
        initTable();
    }

    public ISTable(TableModel dm) {
        super(dm);
        initTable();
    }

    public ISTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
        initTable();
    }

    public ISTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
        initTable();
    }

    public ISTable(int numRows, int numColumns) {
        super(numRows, numColumns);
        initTable();
    }

    public ISTable(Vector rowData, Vector columnNames) {
        super(rowData, columnNames);
        initTable();
    }

    public ISTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
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

    public void removeSortFunction() {
        for (MouseListener mouseListener : tableHeader.getMouseListeners()) {
            if (mouseListener instanceof MouseHandler) {
                tableHeader.removeMouseListener(mouseListener);
                break;
            }
        }
    }

    private void initTable() {
        setSelectionBackground(ColorList.TABLE_SELECTION_BACKGROUND);
        tableHeader.setDefaultRenderer(new SortableHeaderRenderer(tableHeader.getDefaultRenderer()));
        tableHeader.addMouseListener(new MouseHandler());
        getTableHeader().addMouseListener(columnVisibilityTableController);
        columnVisibilityTableController.initForTable(this);
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getModifiers() == KeyEvent.CTRL_MASK && e.getKeyCode() == KeyEvent.VK_E && SodalisApplication.get().isDebugMode()) {
                    exportModel();
                }
            }
        });
    }

    private void exportModel() {
        JFileChooser fileChooser = new JFileChooser();
        File file;

        if (!(getModel() instanceof ObjectTableModel)) {
            return;
        }
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML files", "xml"));
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        file = fileChooser.getSelectedFile();
        if (!file.getName().endsWith(".xml")) {
            file = new File(file.getPath() + ".xml");
        }
        ImExManager.exportData(file, ((ObjectTableModel) getModel()).getObjects());
    }

    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        if ((rowIndex != getSelectedRow() || toggle) && !fireSelectionWillBeChanged()) {
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
        if (Arrays.binarySearch(getSelectedRows(), rowIndex) < 0) {
            c.setBackground(rowIndex % 2 == 0 ? Color.WHITE : ColorList.LIGHTER_BLUE);
        }
        return c;
    }

//    @Override
//    public void addColumn(TableColumn aColumn) {
//        if (columnVisibilityTableControler != null) {
//            columnVisibilityTableControler.setAdjusting(true);
//        }
//        super.addColumn(aColumn);
//        if (columnVisibilityTableControler != null) {
//            columnVisibilityTableControler.setAdjusting(false);
//        }
//    }
//
//    @Override
//    public void removeColumn(TableColumn aColumn) {
//        if (columnVisibilityTableControler != null) {
//            columnVisibilityTableControler.setAdjusting(true);
//        }
//        super.removeColumn(aColumn);
//        if (columnVisibilityTableControler != null) {
//            columnVisibilityTableControler.setAdjusting(false);
//        }
//    }

    public void sort() {
        List selected = new ArrayList();
        int row;
        boolean first = true;

        for (int i = 0; i < getSelectedRows().length; i++) {
            row = getSelectedRows()[i];
            selected.add(((ObjectTableModel) getModel()).getObject(row));
        }
        if (directive.direction != NOT_SORTED && getModel() instanceof SortableTableModel) {
            ((SortableTableModel) getModel()).sort(directive.column, directive.direction == ASCENDING);
        }
        getSelectionModel().clearSelection();
        for (Object object : selected) {
            row = ((ObjectTableModel) getModel()).indexOf(object);
            getSelectionModel().addSelectionInterval(row, row);
            if (first) {
                scrollRectToVisible(getCellRect(row, row, true));
                first = false;
            }
        }
    }

    @Override
    public void setModel(TableModel dataModel) {
        if (columnVisibilityTableController != null) {
            getTableHeader().removeMouseListener(columnVisibilityTableController);
            super.setModel(dataModel);
            getTableHeader().addMouseListener(columnVisibilityTableController);
            columnVisibilityTableController.initForTable(this);
        } else {
            super.setModel(dataModel);
        }
    }

    protected Icon getHeaderRendererIcon(int column, int size) {
        if (directive.column != column || directive.direction == NOT_SORTED) {
            return null;
        }
        return new Arrow(directive.direction == DESCENDING, size, 0);
    }

    private void setComponentAlignment(JComponent jc, int column) {
        Class columnClass = getColumnClass(column);

        if (jc instanceof JLabel) {
            if (columnClass == RIGHT_ALIGNMENT_CLASS) {
                ((JLabel) jc).setHorizontalAlignment(SwingConstants.RIGHT);
            } else if (columnClass == CENTER_ALIGNMENT_CLASS) {
                ((JLabel) jc).setHorizontalAlignment(SwingConstants.CENTER);
            } else if (columnClass == LEFT_ALIGNMENT_CLASS) {
                ((JLabel) jc).setHorizontalAlignment(SwingConstants.LEFT);
            }
        } else {
            if (columnClass == RIGHT_ALIGNMENT_CLASS) {
                jc.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
            } else if (columnClass == CENTER_ALIGNMENT_CLASS) {
                jc.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            } else if (columnClass == LEFT_ALIGNMENT_CLASS) {
                jc.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            }
        }
    }

    public void removeColumnVisibilityTableController(){
        getTableHeader().removeMouseListener(columnVisibilityTableController);
    }

    public ColumnVisibilityTableController getColumnVisibilityTableController() {
        return columnVisibilityTableController;
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }

            JTableHeader h = (JTableHeader) e.getSource();
            TableColumnModel columnModel = h.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(e.getX());
            int column = columnModel.getColumn(viewColumn).getModelIndex();

            if (column != -1) {
                if (directive.column != column) {
                    directive.column = column;
                    directive.direction = NOT_SORTED;
                }
                if (!e.isShiftDown()) {
                    directive.directionUp();
                } else {
                    directive.directionDown();
                }
                tableHeader.repaint();
                sort();
            }
        }
    }

    private static class Arrow implements Icon {

        private boolean descending;
        private int size;
        private int priority;

        public Arrow(boolean descending, int size, int priority) {
            this.descending = descending;
            this.size = size;
            this.priority = priority;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Color color = c == null ? Color.GRAY : c.getBackground();
            // In a compound sort, make each succesive triangle 20% 
            // smaller than the previous one. 
            int dx = (int) (size / 2 * Math.pow(0.8, priority));
            int dy = descending ? dx : -dx;
            // Align icon (roughly) with font baseline. 
            y = y + 5 * size / 6 + (descending ? -dy : 0);
            int shift = descending ? 1 : -1;
            g.translate(x, y);

            // Right diagonal. 
            g.setColor(color.darker());
            g.drawLine(dx / 2, dy, 0, 0);
            g.drawLine(dx / 2, dy + shift, 0, shift);

            // Left diagonal. 
            g.setColor(color.brighter());
            g.drawLine(dx / 2, dy, dx, 0);
            g.drawLine(dx / 2, dy + shift, dx, shift);

            // Horizontal line. 
            if (descending) {
                g.setColor(color.darker().darker());
            } else {
                g.setColor(color.brighter().brighter());
            }
            g.drawLine(dx, 0, 0, 0);

            g.setColor(color);
            g.translate(-x, -y);
        }

        public int getIconWidth() {
            return size;
        }

        public int getIconHeight() {
            return size;
        }
    }

    private class SortableHeaderRenderer implements TableCellRenderer {

        private TableCellRenderer tableCellRenderer;

        public SortableHeaderRenderer(TableCellRenderer tableCellRenderer) {
            this.tableCellRenderer = tableCellRenderer;
        }

        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {
            Component c = tableCellRenderer.getTableCellRendererComponent(table,
                    value, isSelected, hasFocus, row, column);
            if (c instanceof JLabel) {
                JLabel l = (JLabel) c;
                l.setHorizontalTextPosition(JLabel.LEFT);
                int modelColumn = table.convertColumnIndexToModel(column);
                l.setIcon(getHeaderRendererIcon(modelColumn, l.getFont().getSize()));
            }
            return c;
        }
    }

    private static class Directive {

        private int column;
        private int direction;

        public Directive(int column, int direction) {
            this.column = column;
            this.direction = direction;
        }

        private void directionUp() {
            direction = (direction + 1) > DESCENDING ? NOT_SORTED : direction + 1;
        }

        private void directionDown() {
            direction = (direction - 1) < NOT_SORTED ? DESCENDING : direction - 1;
        }
    }
}