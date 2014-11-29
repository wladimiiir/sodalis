package sk.magiksoft.sodalis.folkensemble.repertory.entity;

import sk.magiksoft.sodalis.category.entity.Categorized;
import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.entity.PostCreation;
import sk.magiksoft.sodalis.core.history.Historizable;
import sk.magiksoft.sodalis.core.history.HistoryEvent;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.person.entity.PersonWrapper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wladimiiir
 */
public class Song extends AbstractDatabaseEntity implements Categorized, Historizable {

    private static final long serialVersionUID = -1L;
    public static final DecimalFormat DURATION_FORMAT = new DecimalFormat("00");
    private String name = "";
    private String description = "";
    private String genre = "";
    private String region = "";
    private int duration = 0; //in seconds
    private List<PersonWrapper> composers = new ArrayList<PersonWrapper>();
    private List<PersonWrapper> choreographers = new ArrayList<PersonWrapper>();
    private List<PersonWrapper> pedagogists = new ArrayList<PersonWrapper>();
    private List<PersonWrapper> interpreters = new ArrayList<PersonWrapper>();
    private List<Category> categories = new ArrayList<Category>();
    protected Map<Class<? extends SongData>, SongData> songDatas = new HashMap<Class<? extends SongData>, SongData>();

    @PostCreation
    public void initSongDatas(Object... switches) {
        for (Object s : switches) {
            if (s instanceof Class && SongData.class.isAssignableFrom((Class) s)) {
                try {
                    songDatas.put((Class<? extends SongData>) s, (SongData) ((Class) s).newInstance());
                } catch (InstantiationException ex) {
                    LoggerManager.getInstance().error(Song.class, ex);
                } catch (IllegalAccessException ex) {
                    LoggerManager.getInstance().error(Song.class, ex);
                }
            }
        }
    }

    public Map<Class<? extends SongData>, SongData> getSongDatas() {
        return songDatas;
    }

    public void setSongDatas(Map<Class<? extends SongData>, SongData> songDatas) {
        this.songDatas = songDatas;
    }

    public <T extends SongData> T getSongData(Class<T> clazz) {
        return (T) songDatas.get(clazz);
    }

    public void putSongData(SongData data) {
        songDatas.put(data.getClass(), data);
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof Song) || entity == this) {
            return;
        }
        Song song = (Song) entity;

        this.name = song.name;
        this.description = song.description;
        this.genre = song.genre;
        this.region = song.region;
        this.duration = song.duration;
        this.choreographers.clear();
        this.choreographers.addAll(song.choreographers);
        this.composers.clear();
        this.composers.addAll(song.composers);
        this.interpreters.clear();
        this.interpreters.addAll(song.interpreters);
        this.pedagogists.clear();
        this.pedagogists.addAll(song.pedagogists);
        this.categories.clear();
        this.categories.addAll(song.categories);

        for (SongData songData : songDatas.values()) {
            songData.updateFrom(song.getSongData(songData.getClass()));
        }
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDurationString() {
        return DURATION_FORMAT.format(duration / 60) + ":" + DURATION_FORMAT.format(duration % 60);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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

    public List<PersonWrapper> getInterpreters() {
        return interpreters;
    }

    public void setInterpreters(List<PersonWrapper> interpreters) {
        this.interpreters = interpreters;
    }

    public List<PersonWrapper> getPedagogists() {
        return pedagogists;
    }

    public void setPedagogists(List<PersonWrapper> pedagogists) {
        this.pedagogists = pedagogists;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    public String toString() {
        return name;
    }

    @Override
    public void addHistoryEvent(HistoryEvent event) {
        List<HistoryEvent> events = getSongData(SongHistoryData.class).getHistoryEvents();

        events.add(event);
    }

    @Override
    public List<HistoryEvent> getHistoryEvents(Long entityID) {
        return getSongData(SongHistoryData.class).getHistoryEvents();
    }
}
