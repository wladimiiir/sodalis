package sk.magiksoft.sodalis.core.filter.action;

import java.util.List;

/**
 * @author wladimiiir
 * @since 2011/1/7
 */
public interface Filter<T> {
    void addFilterObjectListener(FilterObjectListener listener);

    List<T> filter(List<T> objects);
}
