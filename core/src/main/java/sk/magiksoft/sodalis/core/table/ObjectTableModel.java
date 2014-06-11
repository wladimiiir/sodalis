
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.table;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.text.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

/**
 * @author wladimiiir
 */
public abstract class ObjectTableModel<T> extends AbstractTableModel implements SortableTableModel {
    public static final Format DATE_FORMAT = new SimpleDateFormat("d.M.yyyy");
    public static final Format DATE_TIME_FORMAT = new SimpleDateFormat("d.M.yyyy HH:mm");
    public static final Format TIME_FORMAT = new SimpleDateFormat("HH:mm");
    public static final Format TIME_SECOND_FORMAT = new SimpleDateFormat("HH:mm:ss");
    public static final Format NUMBER_WITH_SEPARATOR_FORMAT = new DecimalFormat("#,##0");
    protected Vector<T> objects = new Vector<T>();
    private Object[] columnNames;
    protected Object[] columnIdentificators = new Object[]{};
    private Class[] columnClasses;
    private Comparator[] columnComparators;

    public ObjectTableModel(Object[] columnNames) {
        this(columnNames, null);
    }

    public ObjectTableModel(Object[] columnNames, Class[] columnClasses) {
        this.columnNames = columnNames;
        this.columnClasses = columnClasses;
        initColumnComparators();
    }

    protected void initColumnComparators() {
        columnComparators = new Comparator[columnNames.length];
        for (int i = 0; i < columnNames.length; i++) {
            final int column = i;
            columnComparators[i] = new Comparator<T>() {
                @Override
                public int compare(T o1, T o2) {
                    final Object value1 = getValueAt(indexOf(o1), column);
                    final Object value2 = getValueAt(indexOf(o2), column);
                    return Collator.getInstance().compare(value1.toString(), value2.toString());
                }
            };
        }
    }

    public void addObject(T object) {
        objects.add(object);
        final int index = objects.size() - 1;
        final Runnable runnable = new Runnable() {
            @Override public void run() {
                fireTableRowsInserted(index, index);
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        }else{
            SwingUtilities.invokeLater(runnable);
        }
    }

    public void addObject(final int index, T object) {
        objects.add(index, object);
        final Runnable runnable = new Runnable() {
            @Override public void run() {
                fireTableRowsInserted(index, index);
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
        }
    }

    public void setObjects(List<T> objects) {
        setObjects(objects, false);
    }

    public void setObjects(List<T> objects, boolean silent) {
        this.objects.clear();
        this.objects.addAll(objects);
        if (!silent) {
            final Runnable runnable = new Runnable() {
                @Override public void run() {
                    fireTableDataChanged();
                }
            };
            if (SwingUtilities.isEventDispatchThread()) {
                runnable.run();
            } else {
                SwingUtilities.invokeLater(runnable);
            }
        }
    }

    public T removeObject(final int row) {
        T removed = objects.remove(row);
        final Runnable runnable = new Runnable() {
            @Override public void run() {
                fireTableRowsDeleted(row, row);
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
        }
        return removed;
    }

    public boolean removeObject(T object) {
        final int index = objects.indexOf(object);

        if (index != -1) {
            objects.remove(index);
            final Runnable runnable = new Runnable() {
                @Override public void run() {
                    fireTableRowsDeleted(index, index);
                }
            };
            if (SwingUtilities.isEventDispatchThread()) {
                runnable.run();
            } else {
                SwingUtilities.invokeLater(runnable);
            }
        }

        return index != -1;
    }

    public void removeAllObjects() {
        final int size = objects.size();

        objects.clear();
        if (size > 0) {
            final Runnable runnable = new Runnable() {
                @Override public void run() {
                    fireTableRowsDeleted(0, size - 1);
                }
            };
            if (SwingUtilities.isEventDispatchThread()) {
                runnable.run();
            } else {
                SwingUtilities.invokeLater(runnable);
            }

        }
    }

    public Vector<T> getObjects() {
        return new Vector<T>(objects);
    }

    public T getObject(int row) {
        if (row >= objects.size() || row < 0) {
            return null;
        }
        return objects.get(row);
    }

    public int indexOf(T object) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).equals(object)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column].toString();
    }

    public Object getColumnIdentificator(int column) {
        return columnIdentificators.length <= 0 || columnIdentificators.length < column + 1
                ? null
                : columnIdentificators[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClasses == null || columnIndex < 0 || columnIndex >= columnClasses.length
                ? super.getColumnClass(columnIndex)
                : columnClasses[columnIndex];
    }

    @Override
    public int getRowCount() {
        return objects.size();
    }

    @Override
    public Comparator getComparator(int column) {
        return columnComparators[column];
    }

    @Override
    public void sort(int column, boolean ascending) {
        Comparator comparator = ascending ? getComparator(column) : Collections.reverseOrder(getComparator(column));
        if (comparator != null) {
            Collections.sort(objects, comparator);
            final Runnable runnable = new Runnable() {
                @Override public void run() {
                    fireTableDataChanged();
                }
            };
            if (SwingUtilities.isEventDispatchThread()) {
                runnable.run();
            } else {
                SwingUtilities.invokeLater(runnable);
            }
        }
    }
}