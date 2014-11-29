package sk.magiksoft.sodalis.person.ui.table;

import sk.magiksoft.sodalis.person.entity.PersonWrapper;
import sk.magiksoft.sodalis.person.utils.PersonUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public class PersonWrapperTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (c instanceof JLabel) {
            if (value instanceof PersonWrapper) {
                ((JLabel) c).setText(((PersonWrapper) value).getPersonName());
            } else if (value instanceof List && !((List) value).isEmpty() && ((List) value).get(0) instanceof PersonWrapper) {
                ((JLabel) c).setText(PersonUtils.personWrappersToString((List) value));
            }
        }

        return c;
    }


}
