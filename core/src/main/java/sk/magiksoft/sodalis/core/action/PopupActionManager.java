package sk.magiksoft.sodalis.core.action;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class PopupActionManager {
    private List<ObjectAction> objectActions = new ArrayList<ObjectAction>();

    public void registerObjectAction(ObjectAction objectAction) {
        this.objectActions.add(objectAction);
    }

    public List<ObjectAction> getObjectActions(List objects) {
        List<ObjectAction> actions = new ArrayList<ObjectAction>();

        for (ObjectAction objectAction : objectActions) {
            if (!objectAction.isActionShown(objects)) {
                continue;
            }
            objectAction.setEnabled(objectAction.isActionEnabled(objects));
            actions.add(objectAction);
        }

        return actions;
    }
}
