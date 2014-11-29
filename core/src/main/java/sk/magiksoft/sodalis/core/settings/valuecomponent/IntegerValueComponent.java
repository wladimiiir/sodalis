package sk.magiksoft.sodalis.core.settings.valuecomponent;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.swing.CheckedTextField;

import java.text.MessageFormat;

/**
 * @author wladimiiir
 */
public class IntegerValueComponent extends ValueComponent {

    private int minValue;
    private int maxValue;

    public IntegerValueComponent(int minValue, int maxValue) {
        super(new CheckedTextField("[0-9]*"));
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void setValue(Object value) {
        ((CheckedTextField) component).setText(value.toString());
    }

    @Override
    public Object getValue() {
        try {
            return Integer.valueOf(((CheckedTextField) component).getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public void checkValue() throws WrongValueException {
        int value = (Integer) getValue();

        if (value < minValue || value > maxValue) {
            throw new WrongValueException(MessageFormat.format(LocaleManager.getString("valueMustBeBetween"), minValue, maxValue));
        }
    }
}
