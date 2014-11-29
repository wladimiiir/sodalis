package sk.magiksoft.sodalis.core.action;

import javax.swing.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public abstract class MessageAction extends AbstractAction {

    public MessageAction() {
    }

    public MessageAction(String name) {
        super(name);
    }

    protected MessageAction(Icon icon) {
        super("", icon);
    }

    public MessageAction(String name, Icon icon) {
        super(name, icon);
    }


    public abstract ActionMessage getActionMessage(List objects);
}
