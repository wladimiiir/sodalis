package sk.magiksoft.sodalis.person.entity;

import sk.magiksoft.sodalis.category.entity.Categorized;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntityContainer;
import sk.magiksoft.sodalis.core.history.Historizable;
import sk.magiksoft.sodalis.core.history.HistoryEvent;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.search.FullText;

import java.util.*;

/**
 * @author wladimiiir
 */
public class Person extends AbstractDatabaseEntity implements Categorized, Historizable, DatabaseEntityContainer {

    private static final long serialVersionUID = -2993803166060720955L;

    public enum Sex {

        MALE,
        FEMALE;

        @Override
        public String toString() {
            switch (this) {
                case MALE:
                    return LocaleManager.getString("male");
                case FEMALE:
                    return LocaleManager.getString("female");
                default:
                    return "";
            }
        }
    }

    @FullText
    protected String titles = "";
    @FullText
    protected String firstName = "";
    @FullText
    protected String lastName = "";
    @FullText
    protected Sex sex = Sex.MALE;
    protected boolean deleted = false;
    @FullText
    protected Map<Class<? extends PersonData>, PersonData> personDatas = new HashMap<Class<? extends PersonData>, PersonData>();
    @FullText
    private List<Category> categories = new ArrayList<Category>();

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Map<Class<? extends PersonData>, PersonData> getPersonDatas() {
        return personDatas;
    }

    public void setPersonDatas(Map<Class<? extends PersonData>, PersonData> personDatas) {
        this.personDatas = personDatas;
    }

    public <T extends PersonData> T getPersonData(Class<T> clazz) {
        final PersonData data;

        if (personDatas.containsKey(clazz)) {
            data = personDatas.get(clazz);
        } else {
            try {
                data = clazz.newInstance();
                personDatas.put(clazz, data);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Cannot create new instance for data class: " + clazz.getName(), e);
            }
        }

        return clazz.cast(data);
    }

    public void putPersonData(PersonData data) {
        personDatas.put(data.getClass(), data);
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @Override
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof Person) || entity == this) {
            return;
        }
        Person person = (Person) entity;
        this.firstName = person.firstName;
        this.lastName = person.lastName;
        this.deleted = person.deleted;
        this.sex = person.sex;
        this.categories = person.categories;
        for (PersonData personData : personDatas.values()) {
            personData.updateFrom(person.getPersonData(personData.getClass()));
        }
        for (Map.Entry<Class<? extends PersonData>, PersonData> entry : person.personDatas.entrySet()) {
            if (!personDatas.containsKey(entry.getKey())) {
                personDatas.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public String getFullName(boolean firstNameFirst) {
        return firstNameFirst
                ? (getFirstName() + " " + getLastName())
                : (getLastName() + " " + getFirstName());
    }

    @Override
    public String toString() {
        return getFullName(false);
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public void addHistoryEvent(HistoryEvent event) {
        PersonHistoryData historyData = getPersonData(PersonHistoryData.class);
        if (historyData != null) {
            historyData.addHistoryEvent(event);
        }
    }

    @Override
    public List<HistoryEvent> getHistoryEvents(Long entityID) {
        List<HistoryEvent> historyEvents = new ArrayList<HistoryEvent>();

        for (PersonData personData : personDatas.values()) {
            if (personData instanceof Historizable) {
                historyEvents.addAll(((Historizable) personData).getHistoryEvents(getId()));
            } else if (personData instanceof PersonHistoryData) {
                historyEvents.addAll(((PersonHistoryData) personData).getHistoryEvents(getId()));
            }
        }

        return historyEvents;
    }

    @Override
    public <T extends DatabaseEntity> boolean acceptDatabaseEntity(Class<T> clazz) {
        for (PersonData personData : personDatas.values()) {
            if (personData instanceof DatabaseEntityContainer && ((DatabaseEntityContainer) personData).acceptDatabaseEntity(clazz)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T extends DatabaseEntity> T getDatabaseEntity(Class<T> clazz) {
        final List<T> entities = getDatabaseEntities(clazz);
        return entities.isEmpty() ? null : entities.get(0);
    }

    @Override
    public <T extends DatabaseEntity> List<T> getDatabaseEntities(Class<T> clazz) {
        final List<T> entities = new LinkedList<T>();

        for (PersonData personData : personDatas.values()) {
            if (personData instanceof DatabaseEntityContainer) {
                entities.addAll(((DatabaseEntityContainer) personData).getDatabaseEntities(clazz));
            }
        }

        return entities;
    }
}
