package sk.magiksoft.sodalis.core.settings;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.security.LoginManagerService;
import sk.magiksoft.sodalis.core.security.entity.User;
import sk.magiksoft.sodalis.core.security.event.LoginEvent;
import sk.magiksoft.sodalis.core.security.event.LoginListener;
import sk.magiksoft.sodalis.core.security.util.SecurityUtils;

import javax.swing.event.EventListenerList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wladimiiir
 */
public abstract class Settings extends AbstractDatabaseEntity implements LoginListener {
    public static final String O_DEFAULT_PRINT_SETTINGS = "newPrintSettings";
    public static final String O_USER_PRINT_SETTINGS = "userPrintSettings";
    public static final String O_SELECTED_CATEGORIES = "selectedCategories";

    private Map<String, Object> values = new HashMap<String, Object>();
    private Map<String, Object> globalValues = new HashMap<String, Object>();
    private String settingsKey;
    private String userUID;
    private long creationTime = System.currentTimeMillis();
    private transient EventListenerList listenerList;
    private transient String firingPropertyName = null;

    public Settings(String settingsKey) {
        this.settingsKey = settingsKey;
        this.userUID = SodalisApplication.get().getService(LoginManagerService.class, LoginManagerService.SERVICE_NAME).getLoggedSubjectUID();
        SodalisApplication.get().getService(LoginManagerService.class, LoginManagerService.SERVICE_NAME).addLoginListener(this);
        load();
    }

    private Settings(Long id, String settingsKey) {
        setId(id);
        this.settingsKey = settingsKey;
    }

    public EventListenerList getListenerList() {
        if (listenerList == null) {
            listenerList = new EventListenerList();
        }
        return listenerList;
    }

    private void firePropertyChangeEvent(PropertyChangeEvent event) {
        PropertyChangeListener[] listeners = getListenerList().getListeners(PropertyChangeListener.class);

        if (firingPropertyName != null && firingPropertyName.equals(event.getPropertyName())) {
            return;
        }
        firingPropertyName = event.getPropertyName();
        for (PropertyChangeListener propertyChangeListener : listeners) {
            propertyChangeListener.propertyChange(event);
        }
        firingPropertyName = null;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        getListenerList().add(PropertyChangeListener.class, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        getListenerList().remove(PropertyChangeListener.class, listener);
    }

    public Object setValue(String key, Object value) {
        return setValue(key, value, true);
    }

    public Object setValue(String key, Object value, boolean fireEvent) {
        Object oldValue = isGlobalKey(key) ? globalValues.put(key, value) : values.put(key, value);

        if (fireEvent) {
            firePropertyChangeEvent(new PropertyChangeEvent(this, key, oldValue, value));
        }

        return value;
    }

    private boolean isGlobalKey(String key) {
        Field[] fields = getClass().getFields();

        for (Field field : fields) {
            try {
                if (field.get(null) != null && field.get(null).toString().equals(key) && field.isAnnotationPresent(Global.class)) {
                    return true;
                }
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

        return false;
    }

    public Object getValue(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null!");
        }
        Map<String, Object> valueMap = isGlobalKey(key) ? globalValues : values;

        if (!valueMap.containsKey(key)) {
            valueMap.put(key, getDefaultSettingsMap().get(key));
        }
        return valueMap.get(key);
    }

    public int getInt(String key) {
        final Integer value = (Integer) getValue(key);
        return value == null ? 0 : value;
    }

    public Long getLong(String key) {
        final Long value = (Long) getValue(key);
        return value == null ? 0l : value;
    }

    public boolean getBoolean(String key) {
        final Boolean value = (Boolean) getValue(key);
        return value != null && value;
    }

    public double getDouble(String key) {
        final Double value = (Double) getValue(key);
        return value == null ? 0.0 : value;
    }

    public String getString(String key) {
        final String value = (String) getValue(key);
        return value == null ? "" : value;
    }

    public String getSettingsKey() {
        return settingsKey;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public void setSettingsKey(String settingsKey) {
        this.settingsKey = settingsKey;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    public Map<String, Object> getGlobalValues() {
        return globalValues;
    }

    public void setGlobalValues(Map<String, Object> globalValues) {
        this.globalValues = globalValues;
    }

    protected abstract Map<String, Object> getDefaultSettingsMap();

    public void save() {
        saveGlobalSettings();
        SettingsManager.getInstance().addOrUpdateEntity(Settings.class.getName(), this);
    }

    private void saveGlobalSettings() {
        List<Long> list = SettingsManager.getInstance().getDatabaseEntities(
                "select s.id from " + Settings.class.getName() + " as s where s.settingsKey=? and s.userUID is NULL",
                new Object[]{settingsKey},
                new Type[]{StringType.INSTANCE});
        Settings globalSettings = new Settings(list.isEmpty() ? null : list.get(0), settingsKey) {

            @Override
            protected Map<String, Object> getDefaultSettingsMap() {
                return null;
            }
        };
        globalSettings.globalValues = globalValues;

        SettingsManager.getInstance().addOrUpdateEntity(Settings.class.getName(), globalSettings);
    }

    public void load() {
        List<Object[]> list;
        Map<String, Object> dbValues;
        Map<String, Object> globalDBValues;

        if (userUID == null) {
            return;
        }
        list = SettingsManager.getInstance().getDatabaseEntities(
                "select s.id, s.values, s.creationTime from " + Settings.class.getName() + " as s where s.settingsKey=? and s.userUID=?",
                new Object[]{settingsKey, userUID},
                new Type[]{StringType.INSTANCE, StringType.INSTANCE});

        setId((Long) (list.isEmpty() ? null : list.get(0)[0]));
        dbValues = (Map<String, Object>) (list.isEmpty() ? null : list.get(0)[1]);
        creationTime = (Long) (list.isEmpty() ? System.currentTimeMillis() : list.get(0)[2]);

        if (dbValues == null) {
            dbValues = getDefaultSettingsMap();
        }

        list = SettingsManager.getInstance().getDatabaseEntities(
                "select s.globalValues, s.id, s.creationTime from " + Settings.class.getName() + " as s where s.settingsKey=? and s.userUID is NULL",
                new Object[]{settingsKey},
                new Type[]{StringType.INSTANCE});

        globalDBValues = (Map<String, Object>) (list.isEmpty() ? null : list.get(0)[0]);
        if (globalDBValues == null) {
            globalDBValues = getDefaultSettingsMap();
        }

        for (String key : dbValues.keySet()) {
            if (key == null) {
                continue;
            }
            setValue(key, dbValues.get(key));
        }
        for (String key : globalDBValues.keySet()) {
            if (key == null) {
                continue;
            }
            setValue(key, globalDBValues.get(key));
        }
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof Settings)) {
            return;
        }
        Settings settings = (Settings) entity;
        this.values = new HashMap<String, Object>(settings.values);
        this.globalValues = new HashMap<String, Object>(settings.globalValues);
        this.userUID = settings.userUID;
        this.creationTime = settings.creationTime;
    }

    @Override
    public void subjectLoggedIn(LoginEvent event) {
        this.userUID = (String) SecurityUtils.getCredential(event.getSubject(), User.CREDENTIAL_USER_UID);
        load();
    }

    @Override
    public void subjectLoggedOut(LoginEvent event) {
        save();
    }
}
