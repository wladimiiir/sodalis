package sk.magiksoft.sodalis.core.settings.valuecomponent;

import javax.swing.*;

/**
 * @author wladimiiir
 */
public abstract class ValueComponent {
    protected JComponent component;

    public ValueComponent(JComponent component) {
        this.component = component;
    }

    public JComponent getComponent() {
        return component;
    }

    public abstract void setValue(Object value);

    public abstract Object getValue();

    public abstract void checkValue() throws WrongValueException;
}
