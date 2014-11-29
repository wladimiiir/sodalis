package sk.magiksoft.sodalis.core.filter.action;

import java.util.EventListener;

/**
 * @author wladimiiir
 * @since 2011/1/7
 */
public interface FilterObjectListener extends EventListener {
    void filterObjectChanged(FilterObject filterObject);
}
