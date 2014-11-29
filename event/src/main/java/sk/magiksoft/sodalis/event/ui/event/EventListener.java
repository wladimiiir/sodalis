package sk.magiksoft.sodalis.event.ui.event;

import sk.magiksoft.sodalis.event.entity.Event;

import java.awt.*;

/**
 * @author wladimiiir
 */
public interface EventListener {
    public void addEvent(Event event, Point point);

    public void removeEvent(Point point);

    public void removeFromRepeating(Point point);
}
