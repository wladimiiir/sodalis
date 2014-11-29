package sk.magiksoft.sodalis.core.ui.controlpanel;

import sk.magiksoft.sodalis.core.logger.LoggerManager;

import java.util.List;

/**
 * @author wladimiiir
 */
public class AcceptProperty {
    private String propertyName;
    private List<String> values;

    public AcceptProperty(String propertyName, List<String> values) {
        this.propertyName = propertyName;
        this.values = values;
    }

    public boolean accept(Object object) {
        try {
            String value = object.getClass().getDeclaredField(propertyName).get(object).toString();

            for (String acceptValue : values) {
                if (acceptValue.equals(value)) {
                    return true;
                }
            }
            //not found
            return false;
        } catch (Exception e) {
            LoggerManager.getInstance().info(getClass(), e);
        }

        //exception probably
        return true;
    }
}
