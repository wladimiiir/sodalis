package sk.magiksoft.sodalis.core;

import org.jdesktop.application.Application;
import org.jdesktop.application.Application.ExitListener;
import org.jdesktop.application.SingleFrameApplication;
import sk.magiksoft.sodalis.core.action.AbstractImportAction;
import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.action.ContextTransferAction;
import sk.magiksoft.sodalis.core.data.DBManager;
import sk.magiksoft.sodalis.core.data.DBManagerProvider;
import sk.magiksoft.sodalis.core.data.DefaultDataManager;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.enumeration.EnumerationFactory;
import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.sodalis.core.license.SodalisLicenseManager;
import sk.magiksoft.sodalis.icon.IconManager;
import sk.magiksoft.sodalis.core.function.Function;
import sk.magiksoft.sodalis.core.license.LicenseManager;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.module.*;
import sk.magiksoft.sodalis.core.service.Service;
import sk.magiksoft.sodalis.core.service.ServiceListener;
import sk.magiksoft.sodalis.core.service.ServiceManager;
import sk.magiksoft.sodalis.core.settings.storage.StorageManager;
import sk.magiksoft.sodalis.core.splash.SplashScreen;
import sk.magiksoft.sodalis.core.ui.ApplicationPanel;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.sodalis.core.ui.MainMenuBar;
import sk.magiksoft.sodalis.core.utils.ProcessUtils;
import sk.magiksoft.sodalis.core.utils.Utils;
import sk.magiksoft.sodalis.module.ui.ModuleConfigurationDialog;
import sk.magiksoft.swing.MessageGlassPaneManager;
import sk.magiksoft.swing.ProgressDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * @author wladimiiir
 */
public class SodalisApplication extends SingleFrameApplication implements ExitListener, ApplicationContainer {
    private static final String CHOOSE_MODULE_ACTION = "chooseModuleAction";
    private static final URL PROPERTIES_URL = SodalisApplication.class.getResource("config/sodalis.properties");
    private static final URL CONFIGURATION_URL = SodalisApplication.class.getResource("config/sodalis.xml");
    //
    private static PropertyHolder propertyHolder;
    private ApplicationPanel applicationPanel;
    private MessageGlassPaneManager messageGlassPaneManager;
    private boolean restarting = false;

    //---- public methods ----


    public SodalisApplication() {
        initKeyShortcuts();
    }

    private void initKeyShortcuts() {
        final AbstractImportAction importAction = new AbstractImportAction() {
            @Override
            protected void importObjects(List objects) {
                final List<DatabaseEntity> entities = new LinkedList<DatabaseEntity>();

                try {
                    DefaultDataManager.getInstance().executeHQLQuery("delete from Determinant");
                } catch (RemoteException e) {
                    LoggerManager.getInstance().error(getClass(), e);
                }

                for (Object object : objects) {
                    if (object instanceof DatabaseEntity) {
                        entities.add((DatabaseEntity) object);
                    }
                }

                DefaultDataManager.getInstance().persistDatabaseEntities(entities);
            }

            @Override
            public ActionMessage getActionMessage(List objects) {
                return null;
            }
        };

        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            KeyEvent e = (KeyEvent) event;
            if (e.getID() != KeyEvent.KEY_PRESSED) {
                return;
            }

            if (e.getKeyCode() == KeyEvent.VK_E && e.getModifiers() == (KeyEvent.ALT_MASK + KeyEvent.SHIFT_MASK)) {
                //enumeration import
                EnumerationFactory.getInstance().importEnumerations(EnumerationFactory.ENUMERATION_FILE_URL);
                showMessage("Import číselníkov dokončený");
            } else if (e.getKeyCode() == KeyEvent.VK_I && e.getModifiers() == (KeyEvent.ALT_MASK + KeyEvent.SHIFT_MASK)) {
                importAction.actionPerformed(null);
                showMessage("Import záznamov dokončený");
            } else if (e.getKeyCode() == KeyEvent.VK_M && e.getModifiers() == (KeyEvent.ALT_MASK + KeyEvent.SHIFT_MASK)) {
                new ModuleConfigurationDialog(getMainFrame(), new DatabaseModuleManager()).setVisible(true);
            }
        }, AWTEvent.KEY_EVENT_MASK);

    }

    public static SodalisApplication get() {
        return Application.getInstance(SodalisApplication.class);
    }

    //TODO: move to SodalisManager
    public synchronized <T extends Service> T getService(Class<T> serviceClass, String serviceName) {
        return serviceClass.cast(SodalisManager.serviceManager().getService(serviceName));
    }

    //TODO: move to SodalisManager
    public synchronized void addServiceListener(String serviceName, ServiceListener listener) {
        SodalisManager.serviceManager().addServiceListener(serviceName, listener);
    }

    //TODO: move to SodalisManager
    @Override
    public ServiceManager getServiceManager() {
        return SodalisManager.serviceManager();
    }

    @Override
    public URL getConfigurationURL() {
        return CONFIGURATION_URL;
    }

    public static String getProperty(String key, String defaultValue) {
        if (propertyHolder == null) {
            propertyHolder = new PropertyHolder(PROPERTIES_URL, false);
        }
        return propertyHolder.getProperty(key, defaultValue);
    }

    //TODO: move to SodalisManager
    @Override
    public LicenseManager getLicenseManager() {
        return SodalisManager.licenseManager();
    }

    //TODO: move to SodalisManager
    @Override
    public StorageManager getStorageManager() {
        return SodalisManager.storageManager();
    }

    //TODO: move to SodalisManager
    @Override
    public ModuleManager getModuleManager() {
        return SodalisManager.moduleManager();
    }

    public static DBManager getDBManager() {
        return DBManagerProvider.getDBManager();
    }

    public void showMessage(String message, Object... arguments) {
        messageGlassPaneManager.showMessage(MessageFormat.format(message, arguments));
    }

    public void showError(String message, Object... arguments) {
        messageGlassPaneManager.showMessage(MessageFormat.format(message, arguments), Color.RED);
    }

    public Module loadModule(Class<? extends Module> moduleClass) {
        Module module = getModuleManager().getModuleByClass(moduleClass);

        loadModule(module);
        return module;
    }

    public Class<? extends Module> getCurrentModuleClass() {
        return applicationPanel.getCurrentModuleClass();
    }

    public ActionMap getActionMap() {
        return applicationPanel.getActionMap();
    }

    public InputMap getInputMap() {
        return applicationPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public void registerContextTransferAction(final ContextTransferAction contextAction) {
        final Action[] contextActions = contextAction.getContextActions();
        final StringBuilder message = new StringBuilder();

        for (int i = 0, contextActionsLength = contextActions.length; i < contextActionsLength; i++) {
            Action action = contextActions[i];
            KeyStroke keyStroke = (KeyStroke) action.getValue(KeyStroke.class.getName());
            if (keyStroke == null) {
                continue;
            }
            getInputMap().put(keyStroke, "ContextAction" + i);
            getActionMap().put("ContextAction" + i, action);

            message.append("<tr><td align=\"right\"><b>").append(KeyEvent.getKeyText(keyStroke.getKeyCode())).append("</b></td>")
                    .append("<td>").append(action.getValue(Action.SHORT_DESCRIPTION)).append("</td></tr>");
        }

        message.insert(0, "<html><table border=\"0\" cellpadding=\"5\">").append("</table></html>");

        SwingUtilities.invokeLater(() -> {
            showMessage(message.toString());
            setActionsStatusPanel(contextActions);
        });
    }

    public void restartApplication() {
        try {
            ProcessUtils.runCommand(false, getProperty(PropertyHolder.JAVA_BIN, "jre/bin/java"), "-cp", Utils.getClassPath(), "sk.magiksoft.sodalis.app.Main");
            restarting = true;
            exit();
        } catch (IOException | InterruptedException ex) {
            LoggerManager.getInstance().error(SodalisApplication.class, ex);
        }
    }

    public boolean isContextInitialized() {
        for (Module module : getModuleManager().getAllModules()) {
            if (!module.getContextManager().isContextInitialized()) {
                return false;
            }
        }

        return true;
    }

    //---- initialization methods ----

    @Override
    protected void startup() {
        Thread.setDefaultUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler());
        propertyHolder = new PropertyHolder(PROPERTIES_URL, false);
        Locale.setDefault(new Locale(propertyHolder.getProperty(PropertyHolder.LOCALE_LANGUAGE, Locale.getDefault().getLanguage()),
                propertyHolder.getProperty(PropertyHolder.LOCALE_COUNTRY, Locale.getDefault().getCountry())));
//        moduleManager = new ModuleManagerOld(CONFIGURATION_URL);
//        moduleManager = new DatabaseModuleManager();
        addExitListener(this);
        runSplashScreen();
    }

    @Override
    public boolean canExit(EventObject event) {
        int result;

        if (restarting) {
            return true;
        }
        result = ISOptionPane.showConfirmDialog(getMainFrame(), LocaleManager.getString("reallyExit"),
                LocaleManager.getString("Exit"), ISOptionPane.YES_NO_OPTION);
        return result == ISOptionPane.YES_OPTION;
    }

    @Override
    protected void shutdown() {
        try {
            getContext().getSessionStorage().save(getMainFrame(), "mainframe");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void willExit(EventObject event) {
        getServiceManager().applicationWillExit();
    }

    public void runProgress(String message, final Function function, final String errorMessage) {
        final ProgressDialog dialog = new ProgressDialog(getMainFrame());

        dialog.setProgressMessage(message);
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                try {
                    function.apply();
                } catch (Exception e) {
                    if (errorMessage != null) {
                        showError(errorMessage);
                    } else {
                        LoggerManager.getInstance().error(SodalisApplication.class, e);
                    }
                    throw e;
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception e) {
                    LoggerManager.getInstance().error(getClass(), e);
                }
                dialog.stopProgress();
            }
        }.execute();

        dialog.startProgress();
    }

    public static void main(String[] args) {
        Application.launch(SodalisApplication.class, new String[0]);
    }

    void initMainFrame() {
        final JFrame mainFrame = getMainFrame();

        applicationPanel = new ApplicationPanel();
        messageGlassPaneManager = new MessageGlassPaneManager(mainFrame);

        mainFrame.setJMenuBar(new MainMenuBar());
        mainFrame.setIconImage(((ImageIcon) IconManager.getInstance().getIcon("application")).getImage());
        mainFrame.setTitle("Sodalis");
        mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        mainFrame.setMinimumSize(new Dimension(1000, 700));
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });

        int index = 0;
        for (Module module : SodalisManager.moduleManager().getVisibleModules()) {
            addVisibleModule(module, index++);
        }
    }

    private void runSplashScreen() {
        final SplashScreen splashScreen = new SplashScreen(new SodalisSplashLoader(this), 400, 320);

        splashScreen.setForeground(ColorList.SPLASH_FOREGROUND);
        splashScreen.setBackground(ColorList.LIGHT_BLUE);
        splashScreen.start();
    }

    public void showWelcomePage() {
        applicationPanel.showWelcomePage();
    }

    public void showApplicationPanel() {
        show(applicationPanel);
    }

    private void addVisibleModule(final Module module, final int index) {
        applicationPanel.addModule(module);
        applicationPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_1 + index, KeyEvent.CTRL_DOWN_MASK),
                CHOOSE_MODULE_ACTION + "_" + index
        );
        applicationPanel.getActionMap().put(CHOOSE_MODULE_ACTION + "_" + index, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadModule(module);
            }
        });
    }

    private void loadModule(final Module module) {
        if (module == null) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            applicationPanel.loadModuleComponent(module.getClass());
            applicationPanel.loadStatusPanel(module.getContextManager().getStatusPanel());
            module.getContextManager().getMainComponent().requestFocus();
        });
    }

    public void setActionsStatusPanel(Action... actions) {
        JPanel actionsPanel = new JPanel(new GridLayout(1, actions.length));

        for (Action action : actions) {
            actionsPanel.add(new JButton(action));
        }
        applicationPanel.loadStatusPanel(actionsPanel);
    }
}
