package sk.magiksoft.swing.tree;

/**
 * @author wladimiiir
 */
public class CheckObject {
    private boolean checked;
    private Object value;

    public CheckObject(Object value) {
        this(value, false);
    }

    public CheckObject(Object value, boolean checked) {
        this.value = value;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value == null ? "" : value.toString();
    }
}
