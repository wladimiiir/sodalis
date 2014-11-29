package sk.magiksoft.sodalis.core.table;

import java.util.Comparator;

/**
 * @author wladimiiir
 */
public interface SortableTableModel {
    public Comparator getComparator(int column);

    public void sort(int column, boolean ascending);
}
