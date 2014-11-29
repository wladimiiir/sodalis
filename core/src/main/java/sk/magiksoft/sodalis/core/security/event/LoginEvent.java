package sk.magiksoft.sodalis.core.security.event;

import javax.security.auth.Subject;
import java.io.Serializable;

/**
 * @author wladimiiir
 */
public class LoginEvent implements Serializable {

    private Subject subject;

    public LoginEvent(Subject subject) {
        this.subject = subject;
    }

    public Subject getSubject() {
        return subject;
    }
}
