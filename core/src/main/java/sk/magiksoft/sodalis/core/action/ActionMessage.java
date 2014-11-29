package sk.magiksoft.sodalis.core.action;

/**
 * @author wladimiiir
 */
public class ActionMessage {
    private boolean allowed;
    private String message;

    public ActionMessage(boolean allowed, String message) {
        this.allowed = allowed;
        this.message = message;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public String getMessage() {
        return message;
    }

}
