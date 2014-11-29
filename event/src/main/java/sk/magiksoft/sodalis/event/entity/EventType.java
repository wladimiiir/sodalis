package sk.magiksoft.sodalis.event.entity;

import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.factory.EntityFactory;
import sk.magiksoft.sodalis.event.data.EventDataManager;
import sk.magiksoft.sodalis.event.settings.EventSettings;

import java.util.List;

/**
 * @author wladimiiir
 */
public class EventType extends AbstractDatabaseEntity {

    private String name;
    private String[] infoPanelClassNames;

    public EventType() {
    }

    public EventType(String name, String[] infoPanelClassNames) {
        this.name = name;
        this.infoPanelClassNames = infoPanelClassNames;
    }

    public String[] getInfoPanelClassNames() {
        return infoPanelClassNames;
    }

    public void setInfoPanelClassNames(String[] infoPanelClassNames) {
        this.infoPanelClassNames = infoPanelClassNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        EventType eventType = (EventType) entity;

        name = eventType.name;
        infoPanelClassNames = eventType.infoPanelClassNames;
    }

    public Event createEvent() {
        Event event = EntityFactory.getInstance().createEntity(Event.class);

        event.setEventType(this);
        event.setCategories(getCategories(event));

        return event;
    }

    private List<Category> getCategories(Event event) {
        List<Long> ids = (List<Long>) EventSettings.getInstance().getValue(EventSettings.O_SELECTED_CATEGORIES);
        List<Category> categories = EventDataManager.getInstance().getDatabaseEntities(Category.class, ids);

        return categories;
    }

    @Override
    public String toString() {
        return name;
    }
}
