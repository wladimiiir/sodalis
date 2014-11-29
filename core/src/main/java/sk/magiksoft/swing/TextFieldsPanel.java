package sk.magiksoft.swing;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class TextFieldsPanel extends ComponentsPanel {

    @Override
    protected Component createPanelComponent(Object value) {
        JTextField textField = new JTextField();

        if (value != null) {
            textField.setText(getText(value));
        }
        textField.addActionListener(getActionListener());
        return textField;
    }

    protected String getText(Object value) {
        return value.toString();
    }

    @Override
    public List getValues() {
        List values = new ArrayList();

        for (int i = 0; i < getPanelComponents().length - 1; i++) {
            JTextField textField = (JTextField) getPanelComponents()[i];
            values.add(textField.getText());
        }

        return values;
    }

    @Override
    protected boolean isEmpty(Component component) {
        return ((JTextField) component).getText().trim().isEmpty();
    }

}
