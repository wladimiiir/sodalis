package sk.magiksoft.sodalis.folkensemble.inventory.ui;

import sk.magiksoft.sodalis.folkensemble.inventory.entity.Borrowing;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Calendar;

/**
 * @author wladimiiir
 */
public class BorrowingTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Borrowing borrowing = ((BorrowingTableModel) table.getModel()).getObject(row);

        if (!borrowing.isReturned()) {
            component.setFont(component.getFont().deriveFont(Font.BOLD));
            if (column == 2 && borrowing.getTo().before(Calendar.getInstance())) {
                component.setForeground(Color.RED);
            } else {
                component.setForeground(Color.BLACK);
            }
        } else {
            component.setForeground(Color.BLACK);
        }

        return component;
    }

}
