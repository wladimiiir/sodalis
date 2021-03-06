package sk.magiksoft.sodalis.core.settings.storage;

import org.jdesktop.application.session.SplitPaneState;


/**
 * @author wladimiiir
 */
public class HideableSplitPaneState extends SplitPaneState {
    private int state;
    private int lastDividerLocation;

    public HideableSplitPaneState() {
    }

    public HideableSplitPaneState(SplitPaneState splitPaneState, int state, int lastDividerLocation) {
        this.state = state;
        this.lastDividerLocation = lastDividerLocation;
        setOrientation(splitPaneState.getOrientation());
        setDividerLocation(splitPaneState.getDividerLocation());
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setLastDividerLocation(int lastDividerLocation) {
        this.lastDividerLocation = lastDividerLocation;
    }

    public int getState() {
        return state;
    }

    public int getLastDividerLocation() {
        return lastDividerLocation;
    }
}
