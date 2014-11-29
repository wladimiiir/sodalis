package sk.magiksoft.sodalis.core.settings.storage;

import org.jdesktop.application.session.PropertySupport;

import javax.swing.*;
import java.awt.*;

/**
 * @author wladimiiir
 */
public class CheckBoxProperty implements PropertySupport {

    @Override
    public Object getSessionState(Component c) {
        if (!(c instanceof JCheckBox)) {
            return null;
        }

        return new SelectedState(((JCheckBox) c).isSelected());
    }

    @Override
    public void setSessionState(Component c, Object state) {
        if (!(c instanceof JCheckBox) || !(state instanceof SelectedState)) {
            return;
        }

        ((JCheckBox) c).setSelected(((SelectedState) state).isSelected());
    }
}
