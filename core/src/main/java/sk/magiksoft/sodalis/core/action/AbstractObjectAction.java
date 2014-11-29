package sk.magiksoft.sodalis.core.action;

import javax.swing.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public abstract class AbstractObjectAction extends AbstractAction implements ObjectAction {

    public AbstractObjectAction() {
    }

    public AbstractObjectAction(String name, Icon icon) {
        super(name, icon);
    }

    public AbstractObjectAction(String name) {
        super(name);
    }

    @Override
    public boolean isActionEnabled(List objects) {
        return true;
    }

    @Override
    public boolean isActionShown(List objects) {
        return true;
    }

}
