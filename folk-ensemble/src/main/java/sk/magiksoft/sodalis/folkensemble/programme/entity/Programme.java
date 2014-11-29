package sk.magiksoft.sodalis.folkensemble.programme.entity;

import sk.magiksoft.sodalis.category.entity.Categorized;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntityContainer;
import sk.magiksoft.sodalis.core.entity.PostCreation;
import sk.magiksoft.sodalis.core.history.Historizable;
import sk.magiksoft.sodalis.core.history.HistoryEvent;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.folkensemble.repertory.entity.Song;
import sk.magiksoft.sodalis.person.entity.PersonWrapper;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @author wladimiiir
 */
public class Programme extends AbstractDatabaseEntity implements Categorized, Historizable, DatabaseEntityContainer {

    public static final DecimalFormat DURATION_FORMAT = new DecimalFormat("00");
    private String name;
    private String description;
    private List<Category> categories = new ArrayList<Category>();
    private List<PersonWrapper> authors = new ArrayList<PersonWrapper>();
    private List<PersonWrapper> choreographers = new ArrayList<PersonWrapper>();
    private List<PersonWrapper> composers = new ArrayList<PersonWrapper>();
    private List<ProgrammeSong> programmeSongs = new ArrayList<ProgrammeSong>();
    protected Map<Class<? extends ProgrammeData>, ProgrammeData> programmeDatas = new HashMap<Class<? extends ProgrammeData>, ProgrammeData>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        int duration = 0;

        for (ProgrammeSong programmeSong : programmeSongs) {
            duration += programmeSong.getSong().getDuration();
        }

        return duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PersonWrapper> getAuthors() {
        return authors;
    }

    public void setAuthors(List<PersonWrapper> authors) {
        this.authors = authors;
    }

    public Collection<PersonWrapper> getInterpreters() {
        Set<PersonWrapper> interpreters = new HashSet<PersonWrapper>();

        for (ProgrammeSong programmeSong : programmeSongs) {
            interpreters.addAll(programmeSong.getInterpreters());
        }

        return interpreters;
    }

    public List<PersonWrapper> getChoreographers() {
        return choreographers;
    }

    public void setChoreographers(List<PersonWrapper> choreographers) {
        this.choreographers = choreographers;
    }

    public List<PersonWrapper> getComposers() {
        return composers;
    }

    public void setComposers(List<PersonWrapper> composers) {
        this.composers = composers;
    }

    public List<ProgrammeSong> getProgrammeSongs() {
        return programmeSongs;
    }

    public void setProgrammeSongs(List<ProgrammeSong> programmeSongs) {
        this.programmeSongs = programmeSongs;
    }

    public Map<Class<? extends ProgrammeData>, ProgrammeData> getProgrammeDatas() {
        return programmeDatas;
    }

    public void setProgrammeDatas(Map<Class<? extends ProgrammeData>, ProgrammeData> programmeDatas) {
        this.programmeDatas = programmeDatas;
    }

    public String getDurationString() {
        return DURATION_FORMAT.format(getDuration() / 3600) + ":"
                + DURATION_FORMAT.format(getDuration() / 60) + ":"
                + DURATION_FORMAT.format(getDuration() % 60);
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof Programme) || entity == this) {
            return;
        }

        Programme p = (Programme) entity;
        this.name = p.name;
        this.description = p.description;
        this.categories.clear();
        this.categories.addAll(p.categories);
        this.authors.clear();
        this.authors.addAll(p.authors);
        this.choreographers.clear();
        this.choreographers.addAll(p.choreographers);
        this.composers.clear();
        this.composers.addAll(p.composers);
        this.programmeSongs.clear();
        this.programmeSongs.addAll(p.programmeSongs);
        for (ProgrammeData programmeData : programmeDatas.values()) {
            programmeData.updateFrom(p.getProgrammeData(programmeData.getClass()));
        }
    }

    @PostCreation
    public void initProgrammeDatas(Object... switches) {
        for (Object s : switches) {
            if (s instanceof Class && ProgrammeData.class.isAssignableFrom((Class) s)) {
                try {
                    programmeDatas.put((Class<? extends ProgrammeData>) s, (ProgrammeData) ((Class) s).
                            newInstance());
                } catch (InstantiationException ex) {
                    LoggerManager.getInstance().error(Song.class, ex);
                } catch (IllegalAccessException ex) {
                    LoggerManager.getInstance().error(Song.class, ex);
                }
            }
        }
    }

    public <T extends ProgrammeData> T getProgrammeData(Class<T> clazz) {
        return (T) programmeDatas.get(clazz);
    }

    public void putProgrammeData(ProgrammeData data) {
        programmeDatas.put(data.getClass(), data);
    }

    @Override
    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public void addHistoryEvent(HistoryEvent event) {
        List<HistoryEvent> events = getProgrammeData(ProgrammeHistoryData.class).getHistoryEvents();

        events.add(event);
    }

    @Override
    public List<HistoryEvent> getHistoryEvents(Long entityID) {
        return getProgrammeData(ProgrammeHistoryData.class).getHistoryEvents();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public <T extends DatabaseEntity> boolean acceptDatabaseEntity(Class<T> clazz) {
        return clazz == Song.class;
    }

    @Override
    public <T extends DatabaseEntity> T getDatabaseEntity(Class<T> clazz) {
        final List<T> databaseEntities = getDatabaseEntities(clazz);
        return databaseEntities.isEmpty() ? null : databaseEntities.get(0);
    }

    @Override
    public <T extends DatabaseEntity> List<T> getDatabaseEntities(Class<T> clazz) {
        final List<T> entities = new LinkedList<T>();
        if (clazz == Song.class) {
            for (ProgrammeSong programmeSong : programmeSongs) {
                entities.add((T) programmeSong.getSong());
            }
        }

        return entities;
    }
}
