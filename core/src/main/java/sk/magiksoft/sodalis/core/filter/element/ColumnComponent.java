package sk.magiksoft.sodalis.core.filter.element;

import javax.swing.*;

/**
 * @author wladimiiir
 */
public interface ColumnComponent {
    String getLabelText();

    JComponent getComponent();

    void setSelect(String select);

    void setFrom(String from);

    void setWhere(String where);

    void setLabelText(String labelText);

    void addItem(Object object);

    String getSelectQuery();

    String getFromQuery();

    String getWhereQuery();

    boolean isIncluded();
}
