package sk.magiksoft.sodalis.core.history;

import java.util.List;

/**
 * @author wladimiiir
 */
public interface ItemContainer {
    <T> List<T> getItems(Class<T> clazz);
}
