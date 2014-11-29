package sk.magiksoft.sodalis.event.action;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wladimiiir
 */
public class ActionFactory {
    public static String ACTION_PREVIOUS_DAY = "dayBack";
    public static String ACTION_NEXT_DAY = "dayNext";
    public static String ACTION_PREVIOUS_WEEK = "weekBack";
    public static String ACTION_NEXT_WEEK = "weekNext";
    public static String ACTION_TOGGLE_SNAP = "toggleSnap";
    public static String ACTION_CHANGE_COLOR = "changeColor";
    public static String ACTION_DEFAULT_EVENT = "defaultEvent";
    public static String ACTION_EVENT_DURATION = "eventDuration";

    private static ActionFactory instance = null;

    private Map<String, Action> actionMap = new HashMap<String, Action>();

    private ActionFactory() {
        initActions();
    }

    public static ActionFactory getInstance() {
        if (instance == null) {
            instance = new ActionFactory();
        }
        return instance;
    }

    private void initActions() {
        actionMap.put(ACTION_PREVIOUS_DAY, new PreviousDayAction());
        actionMap.put(ACTION_NEXT_DAY, new NextDayAction());
        actionMap.put(ACTION_PREVIOUS_WEEK, new PreviousWeekAction());
        actionMap.put(ACTION_NEXT_WEEK, new NextWeekAction());
        actionMap.put(ACTION_TOGGLE_SNAP, new ToggleSnapAction());
        actionMap.put(ACTION_CHANGE_COLOR, new ChangeColorAction());
        actionMap.put(ACTION_DEFAULT_EVENT, new DefaultEventTypeAction());
        actionMap.put(ACTION_EVENT_DURATION, new DefaultEventDurationAction());
    }

    public Action getAction(String key) {
        return actionMap.get(key);
    }
}
