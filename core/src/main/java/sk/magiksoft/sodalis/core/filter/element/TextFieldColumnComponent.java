package sk.magiksoft.sodalis.core.filter.element;

import javax.swing.*;

/**
 * @author wladimiiir
 */
public class TextFieldColumnComponent extends AbstractColumnComponent {

    private JTextField component = new JTextField();
    private boolean searchFromStart;

    public TextFieldColumnComponent() {
        this(false);
    }

    public TextFieldColumnComponent(boolean searchFromStart) {
        this.searchFromStart = searchFromStart;
    }

    @Override
    public JComponent getComponent() {
        return component;
    }

    @Override
    protected String getWhereText() {
        String text = component.getText().toUpperCase();

        if (text.startsWith("\"") && text.endsWith("\"")) {
            text = text.substring(1, text.length() - 1);
        } else {
            text = "'" + (searchFromStart ? "" : "%") + text + "%'";
        }

        return "remove_diacritics(" + text + ")";
    }

    @Override
    protected String getComparator() {
        return "LIKE";
    }

    @Override
    public boolean isIncluded() {
        return !component.getText().trim().isEmpty();
    }
}
