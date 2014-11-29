package sk.magiksoft.sodalis.core.settings.storage;

/**
 * @author wladimiiir
 */
public class SelectedState {

    private boolean selected;

    public SelectedState() {
    }

    public SelectedState(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
