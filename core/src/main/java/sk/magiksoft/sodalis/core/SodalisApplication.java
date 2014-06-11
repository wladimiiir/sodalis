
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core;

import org.jdesktop.application.Application;
import org.jdesktop.application.Application.ExitListener;
import org.jdesktop.application.SingleFrameApplication;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import sk.magiksoft.sodalis.core.action.AbstractImportAction;
import sk.magiksoft.sodalis.core.action.ActionMessage;
import sk.magiksoft.sodalis.core.action.ContextTransferAction;
import sk.magiksoft.sodalis.core.data.DBManager;
import sk.magiksoft.sodalis.core.data.DBManagerProvider;
import sk.magiksoft.sodalis.core.data.DefaultDataManager;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.enumeration.EnumerationFactory;
import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.sodalis.core.factory.IconFactory;
import sk.magiksoft.sodalis.core.function.Function;
import sk.magiksoft.sodalis.core.injector.Injector;
import sk.magiksoft.sodalis.core.license.LicenseException;
import sk.magiksoft.sodalis.core.license.LicenseManager;
import sk.magiksoft.sodalis.core.license.SodalisLicenseManager;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.module.Module;
import sk.magiksoft.sodalis.core.module.ModuleManager;
import sk.magiksoft.sodalis.core.service.Service;
import sk.magiksoft.sodalis.core.service.ServiceListener;
import sk.magiksoft.sodalis.core.service.ServiceManager;
import sk.magiksoft.sodalis.core.settings.storage.StorageManager;
import sk.magiksoft.sodalis.core.splash.AbstractSplashAction;
import sk.magiksoft.sodalis.core.splash.SplashAction;
import sk.magiksoft.sodalis.core.splash.SplashLoader;
import sk.magiksoft.sodalis.core.splash.SplashScreen;
import sk.magiksoft.sodalis.core.ui.ApplicationPanel;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.sodalis.core.ui.MainMenuBar;
import sk.magiksoft.sodalis.core.utils.ProcessUtils;
import sk.magiksoft.sodalis.core.utils.Utils;
import sk.magiksoft.swing.MessageGlassPaneManager;
import sk.magiksoft.swing.ProgressDialog;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author wladimiiir
 */
public class SodalisApplication extends SingleFrameApplication implements ExitListener, ManagerContainer {

    private static final String CHOOSE_MODULE_ACTION = "chooseModuleAction";
    private static final File PROPERTIES_FILE = new File(System.getProperty("properties.file",
            "config/sodalis.properties"));
    private static final File CONFIGURATION_XML_FILE = new File(System.getProperty("configuration.file",
            "config/sodalis.xml"));
    //
    private static PropertyHolder propertyHolder;
    private DefaultUncaughtExceptionHandler defaultUncaughtExceptionHandler;
    private LicenseManager licenseManager;
    private ServiceManager serviceManager;
    private ModuleManager moduleManager;
    private StorageManager storageManager;
    //
    private JMenuBar mainMenuBar;
    private ApplicationPanel applicationPanel;
    private MessageGlassPaneManager messageGlassPaneManager;
    private boolean restarting = false;
    //
    private long time;

    //---- public methods ----


    public SodalisApplication() {
        initKeyShortcuts();
    }

    private void initKeyShortcuts() {
        final AbstractImportAction importAction = new AbstractImportAction() {
            @Override protected void importObjects(List objects) {
                final List<DatabaseEntity> entities = new LinkedList<DatabaseEntity>();

                try {
                    DefaultDataManager.getInstance().executeHQLQuery("delete from Determinant");
                } catch (RemoteException e) {
                    LoggerManager.getInstance().error(getClass(), e);
                }

                for (Object object : objects) {
                    if(object instanceof DatabaseEntity){
                        entities.add((DatabaseEntity) object);
                    }
                }

                DefaultDataManager.getInstance().persistDatabaseEntities(entities);
            }

            @Override public ActionMessage getActionMessage(List objects) {
                return null;
            }
        };

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                KeyEvent e = (KeyEvent) event;
                if (e.getID() != KeyEvent.KEY_PRESSED) {
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_E && e.getModifiers() == (KeyEvent.ALT_MASK + KeyEvent.SHIFT_MASK)) {
                    //enumeration import
                    EnumerationFactory.getInstance().importEnumerations(EnumerationFactory.ENUMERATION_FILE_URL);
                    showMessage("Import číselníkov dokončený");
                }else if(e.getKeyCode() == KeyEvent.VK_I && e.getModifiers() == (KeyEvent.ALT_MASK + KeyEvent.SHIFT_MASK)){
                    importAction.actionPerformed(null);
                    showMessage("Import záznamov dokončený");
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);

    }

    public static SodalisApplication get() {
        return Application.getInstance(SodalisApplication.class);
    }

    public synchronized <T extends Service> T getService(Class<T> serviceClass, String serviceName) {
        return (T) serviceManager.getService(serviceName);
    }

    public synchronized void addServiceListener(String serviceName, ServiceListener listener) {
        serviceManager.addServiceListener(serviceName, listener);
    }

    @Override
    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    @Override
    public File getConfigurationXMLFile() {
        return CONFIGURATION_XML_FILE;
    }

    public DefaultUncaughtExceptionHandler getDefaultUncaughtExceptionHandler() {
        return defaultUncaughtExceptionHandler;
    }

    public static String getProperty(String key, String defaultValue) {
        if (propertyHolder == null) {
            propertyHolder = new PropertyHolder(PROPERTIES_FILE, false);
        }
        return propertyHolder.getProperty(key, defaultValue);
    }

    @Override
    public LicenseManager getLicenseManager() {
        return licenseManager;
    }

    @Override
    public StorageManager getStorageManager() {
        return storageManager;
    }

    @Override
    public ModuleManager getModuleManager() {
        return moduleManager;
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

    public Module loadModule(Class moduleClass) {
        Module module = moduleManager.getModuleByClass(moduleClass);

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

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showMessage(message.toString());
                setActionsStatusPanel(contextActions);
            }
        });
    }

    public void restartApplication() {
        try {
            ProcessUtils.runCommand(false, getProperty(PropertyHolder.JAVA_BIN, "jre/bin/java"), "-cp", Utils.getClassPath(), Main.class.getName());
            restarting = true;
            exit();
        } catch (IOException ex) {
            LoggerManager.getInstance().error(SodalisApplication.class, ex);
        } catch (InterruptedException ex) {
            LoggerManager.getInstance().error(SodalisApplication.class, ex);
        }
    }

    public boolean isContextInitialized() {
        for (Module module : moduleManager.getModules()) {
            if (!module.getContextManager().isContextInitialized()) {
                return false;
            }
        }

        return true;
    }

    //---- initialization methods ----

    @Override
    protected void startup() {
        time = System.currentTimeMillis();
        Thread.setDefaultUncaughtExceptionHandler(defaultUncaughtExceptionHandler = new DefaultUncaughtExceptionHandler());
        propertyHolder = new PropertyHolder(PROPERTIES_FILE, false);
        Locale.setDefault(new Locale(propertyHolder.getProperty(PropertyHolder.LOCALE_LANGUAGE, Locale.getDefault().getLanguage()),
                propertyHolder.getProperty(PropertyHolder.LOCALE_COUNTRY, Locale.getDefault().getCountry())));
        moduleManager = new ModuleManager(CONFIGURATION_XML_FILE);
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

    public void runProgress(String message, final Function function, final String errorMessage){
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

    private void initMainFrame() {
        final JFrame mainFrame = getMainFrame();

        applicationPanel = new ApplicationPanel();
        mainMenuBar = new MainMenuBar();
        messageGlassPaneManager = new MessageGlassPaneManager(mainFrame);

        mainFrame.setJMenuBar(mainMenuBar);
        mainFrame.setIconImage(((ImageIcon) IconFactory.getInstance().getIcon("application")).getImage());
        mainFrame.setTitle("Sodalis");
        mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        mainFrame.setMinimumSize(new Dimension(1000, 700));
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
    }

    private void runSplashScreen() {
        final SplashScreen splashScreen = new SplashScreen(new SodalisSplashLoader(), 400, 320);

        splashScreen.setForeground(ColorList.SPLASH_FOREGROUND);
        splashScreen.setBackground(ColorList.LIGHT_BLUE);
        splashScreen.start();
    }

    private void initLicenseManager() {
        try {
            licenseManager = new SodalisLicenseManager();
        } catch (LicenseException ex) {
            ISOptionPane.showMessageDialog(getMainFrame(), ex.getMessage());
            System.exit(1);
        }
    }

    public void initServiceManager() {
        try {
            Document xmlDocument = new SAXBuilder().build(getConfigurationXMLFile());
            Element serviceManagerElement = xmlDocument.getRootElement().getChild("service_manager");

            serviceManager = (ServiceManager) Class.forName(serviceManagerElement.getTextTrim()).newInstance();
            serviceManager.initialize();
        } catch (ClassNotFoundException ex) {
            LoggerManager.getInstance().error(SodalisApplication.class, ex);
        } catch (InstantiationException ex) {
            LoggerManager.getInstance().error(SodalisApplication.class, ex);
        } catch (IllegalAccessException ex) {
            LoggerManager.getInstance().error(SodalisApplication.class, ex);
        } catch (JDOMException ex) {
            LoggerManager.getInstance().error(SodalisApplication.class, ex);
        } catch (IOException ex) {
            LoggerManager.getInstance().error(SodalisApplication.class, ex);
        }
    }

    public void showWelcomePage() {
        applicationPanel.showWelcomePage();
    }

    private void initModule(final Module module, final int index) {
        applicationPanel.addModule(module);

        new Thread(new Runnable() {
            @Override
            public void run() {
                applicationPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_1
                        + index, KeyEvent.CTRL_DOWN_MASK), CHOOSE_MODULE_ACTION + "_" + index);
                applicationPanel.getActionMap().put(CHOOSE_MODULE_ACTION + "_" + index, new ChooseModuleAction(index));
                module.postInitialization();
            }
        }).start();
    }

    private void loadModule(final Module module) {
        if (module == null) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                applicationPanel.loadModuleComponent(module.getClass());
                applicationPanel.loadStatusPanel(module.getContextManager().getStatusPanel());
                module.getContextManager().getMainComponent().requestFocus();
            }
        });
    }

    public void setActionsStatusPanel(Action... actions) {
        JPanel actionsPanel = new JPanel(new GridLayout(1, actions.length));

        for (Action action : actions) {
            actionsPanel.add(new JButton(action));
        }
        applicationPanel.loadStatusPanel(actionsPanel);
    }

    public boolean isDebugMode() {
        return licenseManager.getLicense().isDebugMode();
    }

    private class SodalisSplashLoader implements SplashLoader {

        @Override
        public List<SplashAction> getSplashActions() {
            final Image splashImage;
            List<SplashAction> splashActions = new ArrayList<SplashAction>();
            try {
                splashImage = ImageIO.read(new File("data/splash.png"));

                splashActions.add(new AbstractSplashAction(splashImage, LocaleManager.getString(
                        "StartingServices")) {

                    @Override
                    public void run() {
                        initServiceManager();
                    }
                });
                splashActions.add(new AbstractSplashAction(splashImage, LocaleManager.getString(
                        "VerifyingLicense")) {

                    @Override
                    public void run() {
                        initLicenseManager();
                    }
                });
                splashActions.add(new AbstractSplashAction(splashImage, LocaleManager.getString(
                        "InitializingManagers")) {

                    @Override
                    public void run() {
                        storageManager = new StorageManager(SodalisApplication.this);
                        Injector.registerResource(StorageManager.class, storageManager);
                    }
                });
                splashActions.add(new AbstractSplashAction(splashImage, LocaleManager.getString(
                        "InitializingMainFrame")) {

                    @Override
                    public void run() {
                        initMainFrame();
                    }
                });

                final List<Module> modules = moduleManager.getModules();
                for (int i = 0; i < modules.size(); i++) {
                    final Module module = modules.get(i);
                    final int index = i;

                    splashActions.add(new AbstractSplashAction(splashImage,
                            MessageFormat.format(LocaleManager.getString("InitializingModule"), module.
                                    getModuleDescriptor().getDescription())) {

                        @Override
                        public void run() {
                            initModule(module, index);
                        }
                    });
                }


            } catch (IOException ex) {
                LoggerManager.getInstance().error(SodalisApplication.class, ex);
            }

            return splashActions;
        }

        @Override
        public void loaderFinished() {
            show(applicationPanel);
            LoggerManager.getInstance().info(SodalisApplication.class, MessageFormat.format("Startup time: {0} ms", System.currentTimeMillis() - time));
            serviceManager.applicationOpened();
        }

        @Override
        public void loaderCancelled(Throwable e) {
            if (e != null) {
                ISOptionPane.showMessageDialog(null, LocaleManager.getString("appStartError"));
            } else {
                ISOptionPane.showMessageDialog(null, LocaleManager.getString("appStartCancelled"));
            }

            System.exit(1);
        }

        @Override
        public String getTitle() {
            return "Sodalis";
        }

        @Override
        public Image getIconImage() {
            return ((ImageIcon) IconFactory.getInstance().getIcon("application")).getImage();
        }
    }

    private class ChooseModuleAction extends AbstractAction {

        private int moduleIndex;

        public ChooseModuleAction(int moduleIndex) {
            this.moduleIndex = moduleIndex;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Module chosenModule = moduleManager.getModule(moduleIndex);

            loadModule(chosenModule);
        }
    }
}