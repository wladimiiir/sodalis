package sk.magiksoft.swing.itemcomponent;

import sk.magiksoft.sodalis.core.table.ObjectTableModel;
import sk.magiksoft.swing.ISTable;
import sk.magiksoft.swing.table.TextFieldCellEditor;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public abstract class ItemComponent<T> extends JComponent {

    private JButton btnAdd;
    private JButton btnRemove;
    protected JTable table;
    protected ObjectTableModel<T> tableModel;

    public ItemComponent() {
        this.tableModel = createTableModel();
        initComponents();
    }

    private void initComponents() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JScrollPane scrollPane = new JScrollPane(table = new ItemTable(tableModel)) {

            @Override
            public JViewport getColumnHeader() {
                return isTableHeaderShown() ? super.getColumnHeader() : new JViewport();
            }
        };

        scrollPane.getViewport().setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());

        btnAdd = new JButton("+");
        btnRemove = new JButton("-");
        btnAdd.setBackground(Color.LIGHT_GRAY);
        btnRemove.setBackground(Color.LIGHT_GRAY);
        btnAdd.setPreferredSize(new Dimension(10, 12));
        btnRemove.setPreferredSize(new Dimension(10, 12));
        btnAdd.setFont(new Font(btnAdd.getFont().getFontName(), Font.PLAIN, 8));
        btnRemove.setFont(new Font(btnRemove.getFont().getFontName(), Font.PLAIN, 8));
        btnAdd.setFocusPainted(false);
        btnRemove.setFocusPainted(false);
        btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addItem();
            }
        });
        btnRemove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                removeItem();
            }
        });
        tableModel.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    fireItemUpdated(tableModel.getObject(e.getFirstRow()));
                }
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                fireSelectionChanged();
            }
        });

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRemove);

        this.add(scrollPane, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

    }

    public T getSelectedItem() {
        return table.getSelectedRow() >= 0 ? tableModel.getObject(table.getSelectedRow()) : null;
    }

    public List<T> getSelectedItems() {
        int[] rows = table.getSelectedRows();
        List<T> items = new ArrayList<T>(rows.length);

        for (int row : rows) {
            items.add(tableModel.getObject(row));
        }

        return items;
    }

    public void setSelectedItem(T item) {
        int index = tableModel.indexOf(item);
        table.getSelectionModel().setSelectionInterval(index, index);
    }

    protected boolean isTableHeaderShown() {
        return true;
    }

    protected void addItem() {
        final T newItem = getNewItem();

        if (newItem == null) {
            return;
        }

        tableModel.addObject(newItem);
        fireItemAdded(newItem);
        if (table.isCellEditable(tableModel.getRowCount() - 1, 0)) {
            table.editCellAt(tableModel.getRowCount() - 1, 0);
            table.getEditorComponent().requestFocus();
        }
        table.scrollRectToVisible(table.getCellRect(tableModel.indexOf(newItem), 0, true));
    }

    protected void removeItem() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow < 0) {
            return;
        }
        fireItemRemoved(tableModel.removeObject(selectedRow));
        if (selectedRow < tableModel.getRowCount()) {
            table.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
        } else if (tableModel.getRowCount() > 0) {
            table.getSelectionModel().setSelectionInterval(selectedRow - 1, selectedRow - 1);
        }
    }

    protected abstract T getNewItem();

    protected abstract ObjectTableModel<T> createTableModel();

    protected TableCellEditor getCellEditor(int column) {
        return new TextFieldCellEditor();
    }

    protected TableCellRenderer getCellRenderer(int column) {
        return new DefaultTableCellRenderer();
    }

    public List<T> getItems() {
        return tableModel.getObjects();
    }

    public void setItems(List<T> items) {
        tableModel.setObjects(items);
    }

    public void addItemComponentListener(ItemComponentListener<T> listener) {
        listenerList.add(ItemComponentListener.class, listener);
    }

    public void removeItemComponentListener(ItemComponentListener<T> listener) {
        listenerList.remove(ItemComponentListener.class, listener);
    }

    protected void fireItemAdded(T item) {
        ItemComponentListener<T>[] listeners = listenerList.getListeners(ItemComponentListener.class);

        for (ItemComponentListener<T> listener : listeners) {
            listener.itemAdded(item);
        }
    }

    protected void fireItemRemoved(T item) {
        ItemComponentListener<T>[] listeners = listenerList.getListeners(ItemComponentListener.class);

        for (ItemComponentListener<T> listener : listeners) {
            listener.itemRemoved(item);
        }
    }

    protected void fireItemUpdated(T item) {
        ItemComponentListener<T>[] listeners = listenerList.getListeners(ItemComponentListener.class);

        for (ItemComponentListener<T> listener : listeners) {
            listener.itemUpdated(item);
        }
    }

    protected void fireSelectionChanged() {
        ItemComponentListener<T>[] listeners = listenerList.getListeners(ItemComponentListener.class);

        for (ItemComponentListener<T> listener : listeners) {
            listener.selectionChanged();
        }
    }

    private class ItemTable extends ISTable {

        private final KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB && editingRow != -1 && editingColumn != -1 && editingColumn < getColumnCount() - 1) {
                    final int row = editingRow;
                    final int column = editingColumn + 1;
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            editCellAt(row, column);
                            getEditorComponent().requestFocus();
                        }
                    });
                }
            }
        };

        private ItemTable(ObjectTableModel tableModel) {
            super(tableModel);
            setRowHeight(20);
        }

        @Override
        public Component prepareEditor(TableCellEditor editor, int row, int column) {
            final Component component = super.prepareEditor(editor, row, column);
            component.removeKeyListener(keyListener);
            component.addKeyListener(keyListener);
            return component;
        }

        @Override
        public TableCellEditor getCellEditor(int row, int column) {
            return ItemComponent.this.getCellEditor(column);
        }

        @Override
        public TableCellRenderer getCellRenderer(int row, int column) {
            return ItemComponent.this.getCellRenderer(column);
        }
    }
}
