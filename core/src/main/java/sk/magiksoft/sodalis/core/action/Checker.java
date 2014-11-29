package sk.magiksoft.sodalis.core.action;

import java.util.EventListener;

/**
 * @author wladimiiir
 * @since 2011/6/24
 */
public interface Checker extends EventListener {
    boolean check();
}
