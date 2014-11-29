package sk.magiksoft.sodalis.core.settings.storage;

import org.jdesktop.application.Application;
import org.jdesktop.application.Application.ExitListener;
import org.jdesktop.application.LocalStorage;
import org.jdesktop.application.SessionStorage;
import org.jdesktop.application.session.PropertySupport;
import sk.magiksoft.sodalis.core.Constants;
import sk.magiksoft.sodalis.core.injector.Injector;
import sk.magiksoft.sodalis.core.injector.Resource;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.security.LoginManagerService;
import sk.magiksoft.sodalis.core.security.entity.User;
import sk.magiksoft.sodalis.core.security.event.LoginEvent;
import sk.magiksoft.sodalis.core.security.event.LoginListener;
import sk.magiksoft.sodalis.core.security.util.SecurityUtils;
import sk.magiksoft.sodalis.core.utils.FileUtils;
import sk.magiksoft.swing.HideableSplitPane;
import sk.magiksoft.swing.ISTable;

import javax.security.auth.Subject;
import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wladimiiir
 */
public class StorageManager implements LoginListener, ExitListener {

    private static final String TEMP_STORAGE_FILENAME = "storage.xml";
    private static final File CONFIG_DIR = new File("data/config/");
    private Application application;
    private EventListenerList listenerList = new EventListenerList();
    private Map<String, Component> componentMap = new HashMap<String, Component>();
    private Map<String, Component> componentStorageMap = new HashMap<String, Component>();
    @Resource
    private LoginManagerService loginManagerService;
    private Subject subject;

    public StorageManager(Application application) {
        Injector.inject(this);
        this.application = application;
        this.application.addExitListener(this);
        if (this.loginManagerService != null) {
            this.loginManagerService.addLoginListener(this);
        }
        initSessionStorage();
    }

    public SessionStorage getSessionStorage() {
        return application.getContext().getSessionStorage();
    }

    public synchronized void registerComponentForStorage(String key, Component component) {
        componentStorageMap.put(key, component);
        load(key);
    }

    public synchronized void registerComponent(String key, Component component) {
        componentMap.put(key, component);
        load(key);
    }

    public synchronized void deregisterComponent(String key) {
        componentMap.remove(key);
    }

    public void addLoginListener(LoginListener listener) {
        listenerList.add(LoginListener.class, listener);
    }

    private void fireSubjectLoggedIn(LoginEvent event) {
        LoginListener[] listeners = listenerList.getListeners(LoginListener.class);

        for (LoginListener loginListener : listeners) {
            loginListener.subjectLoggedIn(event);
        }
    }

    private void fireSubjectLoggedOut(LoginEvent event) {
        LoginListener[] listeners = listenerList.getListeners(LoginListener.class);

        for (LoginListener loginListener : listeners) {
            loginListener.subjectLoggedOut(event);
        }
    }

    private File getConfigFile(Subject subject, String key) {
        return new File(SecurityUtils.getCredential(subject, User.CREDENTIAL_USER_UID).toString() + File.separator + key + ".cfg");
    }

    public synchronized void saveAll() {
        if (subject == null) {
            return;
        }

        SessionStorage storage = getSessionStorage();

        application.getContext().getLocalStorage().setDirectory(CONFIG_DIR);
        for (String key : componentMap.keySet()) {
            File configFile = getConfigFile(subject, key);
            save(configFile, storage, key);
        }

        Map<String, Storage> storageMap = (Map<String, Storage>) StorageSettings.getInstance().getValue(StorageSettings.O_STORAGE_MAP);
        for (Map.Entry<String, Component> entry : componentStorageMap.entrySet()) {
            storageMap.put(entry.getKey(), saveComponentState(entry.getValue()));
        }
        StorageSettings.getInstance().setValue(StorageSettings.O_STORAGE_MAP, storageMap);
        StorageSettings.getInstance().save();
    }

    public synchronized void save(String key) {
        if (subject == null) {
            return;
        }
        SessionStorage storage = application.getContext().getSessionStorage();

        if (componentMap.containsKey(key)) {
            File configFile = getConfigFile(subject, key);
            application.getContext().getLocalStorage().setDirectory(CONFIG_DIR);
            save(configFile, storage, key);
        }
        if (componentStorageMap.containsKey(key)) {
            Map<String, Storage> storageMap = (Map<String, Storage>) StorageSettings.getInstance().getValue(StorageSettings.O_STORAGE_MAP);
            storageMap.put(key, saveComponentState(componentStorageMap.get(key)));
            StorageSettings.getInstance().save();
        }
    }

    private synchronized void save(File configFile, SessionStorage storage, String key) {
        new File(CONFIG_DIR, configFile.getPath()).getParentFile().mkdirs();
        try {
            storage.save(componentMap.get(key), configFile.getPath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void load(String key) {
        if (subject == null) {
            return;
        }
        SessionStorage storage = application.getContext().getSessionStorage();
        if (componentMap.containsKey(key)) {
            File configFile = getConfigFile(subject, key);

            if (!new File(CONFIG_DIR, configFile.getPath()).exists()) {
                configFile = getDefaultConfigFile(key);
                if (!new File(CONFIG_DIR, configFile.getPath()).exists()) {
                    return;
                }
            }
            application.getContext().getLocalStorage().setDirectory(CONFIG_DIR);
            try {
                storage.restore(componentMap.get(key), configFile.getPath());
            } catch (Exception ignored) {
            }
        }
        Map<String, Storage> storageMap = (Map<String, Storage>) StorageSettings.getInstance().getValue(StorageSettings.O_STORAGE_MAP);
        if (componentStorageMap.containsKey(key) && storageMap.containsKey(key)) {
            loadComponentState(storageMap.get(key), componentStorageMap.get(key));
        }
    }

    private File getDefaultConfigFile(String key) {
        return new File("default" + File.separator + key + ".cfg");
    }

    public synchronized void loadAll() {
        if (subject == null) {
            return;
        }

        final SessionStorage storage = application.getContext().getSessionStorage();

        application.getContext().getLocalStorage().setDirectory(CONFIG_DIR);
        for (String key : componentMap.keySet()) {
            File configFile = getConfigFile(subject, key);

            if (!new File(CONFIG_DIR, configFile.getPath()).exists()) {
                configFile = getDefaultConfigFile(key);
                if (!new File(CONFIG_DIR, configFile.getPath()).exists()) {
                    continue;
                }
            }
            try {
                storage.restore(componentMap.get(key), configFile.getPath());
            } catch (Exception ex) {
            }
        }
        Map<String, Storage> storageMap = (Map<String, Storage>) StorageSettings.getInstance().getValue(StorageSettings.O_STORAGE_MAP);
        for (Map.Entry<String, Component> entry : componentStorageMap.entrySet()) {
            if (storageMap.containsKey(entry.getKey())) {
                loadComponentState(storageMap.get(entry.getKey()), entry.getValue());
            }
        }
    }

    @Override
    public void subjectLoggedIn(LoginEvent event) {
        this.subject = event.getSubject();
        StorageSettings.getInstance().load();
        loadAll();
        fireSubjectLoggedIn(event);
    }

    @Override
    public void subjectLoggedOut(LoginEvent event) {
        this.subject = event.getSubject();
        saveAll();
        fireSubjectLoggedOut(event);
    }

    @Override
    public boolean canExit(EventObject event) {
        return true;
    }

    @Override
    public void willExit(EventObject event) {
        saveAll();
    }

    private void initSessionStorage() {
        final SessionStorage sessionStorage = application.getContext().getSessionStorage();

        sessionStorage.putProperty(ISTable.class, new ISTableProperty());
        sessionStorage.putProperty(HideableSplitPane.class, new HideableSplitPaneProperty());
        sessionStorage.putProperty(JCheckBox.class, new CheckBoxProperty());
    }

    public synchronized Storage saveComponentState(Component component) {
        LocalStorage localStorage = application.getContext().getLocalStorage();
        SessionStorage sessionStorage = application.getContext().getSessionStorage();

        try {
            localStorage.setDirectory(new File(Constants.TEMP_DIR));

            PropertySupport property = sessionStorage.getProperty(component);
//            if(property==null){
//                return null;
//            }
            localStorage.save(property.getSessionState(component), TEMP_STORAGE_FILENAME);

            return new Storage(FileUtils.getBytesFromFile(new File(Constants.TEMP_DIR + TEMP_STORAGE_FILENAME)));
        } catch (IOException ex) {
            LoggerManager.getInstance().warn(getClass(), ex);
        }

        return null;
    }

    public synchronized void loadComponentState(Storage storage, Component component) {
        LocalStorage localStorage = application.getContext().getLocalStorage();
        SessionStorage sessionStorage = application.getContext().getSessionStorage();
        String oldName = component.getName();

        try {
            localStorage.setDirectory(new File(Constants.TEMP_DIR));

            FileUtils.saveBytesToFile(new File(Constants.TEMP_DIR + TEMP_STORAGE_FILENAME),
                    storage.getBytes());

            Object state = localStorage.load(TEMP_STORAGE_FILENAME);
            PropertySupport property = sessionStorage.getProperty(component);
            if (property == null) {
                return;
            }
            property.setSessionState(component, state);
        } catch (Exception ex) {
            LoggerManager.getInstance().warn(getClass(), ex);
        }
    }
}
