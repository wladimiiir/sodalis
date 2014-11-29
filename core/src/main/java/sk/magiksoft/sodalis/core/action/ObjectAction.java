package sk.magiksoft.sodalis.core.action;

import javax.swing.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public interface ObjectAction extends Action {

    public boolean isActionEnabled(List objects);

    public boolean isActionShown(List objects);
}
