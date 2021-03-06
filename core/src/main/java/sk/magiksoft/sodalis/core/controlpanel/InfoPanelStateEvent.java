package sk.magiksoft.sodalis.core.controlpanel;

/**
 * @author wladimiiir
 */
public class InfoPanelStateEvent {
    public static final int STATE_TYPE_UPDATE = 1;
    public static final int STATE_TYPE_VALIDITY = 2;

    private int stateType;
    private boolean stateValue;

    public InfoPanelStateEvent(int stateType, boolean stateValue) {
        this.stateType = stateType;
        this.stateValue = stateValue;
    }

    public boolean getStateValue() {
        return stateValue;
    }

    public int getStateType() {
        return stateType;
    }
}
