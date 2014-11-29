package sk.magiksoft.sodalis.core.security.event;

import java.util.EventListener;

/**
 * @author wladimiiir
 */
public interface LoginListener extends EventListener {
    void subjectLoggedIn(LoginEvent event);

    void subjectLoggedOut(LoginEvent event);
}
