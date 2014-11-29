package sk.magiksoft.sodalis.event.entity;

import sk.magiksoft.sodalis.core.data.DefaultDataManager;
import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntityContainer;
import sk.magiksoft.sodalis.event.data.EventDataManager;
import sk.magiksoft.sodalis.person.entity.PersonData;

import java.util.Collections;
import java.util.List;

/**
 * @author wladimiiir
 * @since 2010/12/1
 */
public class EventEntityData extends AbstractDatabaseEntity implements PersonData, DatabaseEntityContainer {
    @Override
    public void updateFrom(DatabaseEntity entity) {
    }


    @Override
    public <T extends DatabaseEntity> boolean acceptDatabaseEntity(Class<T> clazz) {
        return clazz == Event.class;
    }

    @Override
    public <T extends DatabaseEntity> T getDatabaseEntity(Class<T> clazz) {
        final List<T> entities = getDatabaseEntities(clazz);
        return entities.isEmpty() ? null : entities.get(0);
    }

    @Override
    public <T extends DatabaseEntity> List<T> getDatabaseEntities(Class<T> clazz) {
        return clazz == Event.class
                ? (List<T>) EventDataManager.getInstance().getEventsForMemberIDs(Collections.singletonList(getPersonID()))
                : Collections.<T>emptyList();
    }

    public Long getPersonID() {
        return DefaultDataManager.getInstance().getDatabaseEntity("select p from Person p, EventEntityData eed where eed in elements(p.personDatas) and eed.id=" + getId()).getId();
    }
}
