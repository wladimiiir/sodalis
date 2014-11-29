package sk.magiksoft.swing.itemcomponent;

import java.util.EventListener;

/**
 * @author wladimiiir
 * @since 2010/6/25
 */
public interface ItemComponentListener<T> extends EventListener {
    void itemAdded(T item);

    void itemRemoved(T item);

    void itemUpdated(T item);

    void selectionChanged();
}
