package sk.magiksoft.sodalis.core.security;

import com.jhlabs.image.BlurFilter;
import org.jdesktop.application.Application;
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.effect.BufferedImageOpEffect;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;
import sk.magiksoft.sodalis.core.SodalisApplication;
import sk.magiksoft.sodalis.core.data.DataAdapter;
import sk.magiksoft.sodalis.core.data.remote.DataManagerProvider;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.injector.Injector;
import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.security.entity.SodalisUser;
import sk.magiksoft.sodalis.core.security.entity.User;
import sk.magiksoft.sodalis.core.security.event.LoginEvent;
import sk.magiksoft.sodalis.core.security.event.LoginListener;
import sk.magiksoft.sodalis.core.security.ui.LoginPanel;
import sk.magiksoft.sodalis.core.security.util.SecurityUtils;
import sk.magiksoft.sodalis.core.service.AbstractLocalService;
import sk.magiksoft.sodalis.core.service.ServiceListener;
import sk.magiksoft.sodalis.core.ui.ISOptionPane;
import sk.magiksoft.sodalis.core.utils.UIUtils;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

/**
 * @author wladimiiir
 */
public class LoginManagerService extends AbstractLocalService {
    //--ui--

    private LoginPanel loginPanel;
    private JDialog loginDialog;
    private LockableUI blurUI = new LockableUI(new BufferedImageOpEffect(new BlurFilter()));
    private Timer lockTimer;
    //--login--
    private LoginContext loginContext;
    private Subject loggedSubject;
    private EventListenerList listenerList = new EventListenerList();
    public static final String SERVICE_NAME = "LoginManagerService";
    private boolean firstTime = true;

    public LoginManagerService() {
        Injector.registerResource(LoginManagerService.class, this);
        initComponents();
        initLoginContext();
    }

    public void addLoginListener(LoginListener listener) {
        listenerList.add(LoginListener.class, listener);
    }

    public Subject getLoggedSubject() {
        return loggedSubject;
    }

    public String getLoggedSubjectUID() {
        return loggedSubject == null ? null : (String) SecurityUtils.getCredential(loggedSubject, User.CREDENTIAL_USER_UID);
    }

    private void fireSubjectLoggedIn(final Subject subject) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!SodalisApplication.get().isContextInitialized()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        LoggerManager.getInstance().error(getClass(), e);
                    }
                }

                LoginListener[] listeners = listenerList.getListeners(LoginListener.class);
                LoginEvent event = new LoginEvent(subject);

                for (LoginListener subjectListener : listeners) {
                    subjectListener.subjectLoggedIn(event);
                }
            }
        }).start();
    }

    private void fireSubjectLoggedOut(Subject subject) {
        LoginListener[] listeners = listenerList.getListeners(LoginListener.class);
        LoginEvent event = new LoginEvent(subject);

        for (LoginListener subjectListener : listeners) {
            subjectListener.subjectLoggedOut(event);
        }
    }

    @Override
    public void applicationOpened() {
        login();
    }

    @Override
    public void initialize() {
        DataManagerProvider.addDataListener(new DataAdapter() {

            @Override
            public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
                if (loggedSubject == null) {
                    return;
                }

                for (Object object : entities) {
                    if (!(object instanceof SodalisUser) || !((SodalisUser) object).getUserUID().equals(getLoggedSubjectUID())) {
                        continue;
                    }
                    SodalisUser sodalisUser = (SodalisUser) object;

                    loggedSubject.getPrivateCredentials().clear();
                    loggedSubject.getPublicCredentials().clear();
                    loggedSubject.getPrivateCredentials().add(new SimpleEntry(User.CREDENTIAL_USERNAME, sodalisUser.getUserName()));
                    loggedSubject.getPrivateCredentials().add(new SimpleEntry(User.CREDENTIAL_USER_UID, sodalisUser.getUserUID()));
                    loggedSubject.getPublicCredentials().add(new SimpleEntry(User.CREDENTIAL_PERSON, sodalisUser.getCredentialsMap().get(User.CREDENTIAL_PERSON)));
                }
            }
        });
    }

    private void checkAdminExistence() {
        if (SecurityDataManager.getInstance().isAdminPresent()) {
            return;
        }

        //no admin user found
        ISOptionPane.showMessageDialog(null, LocaleManager.getString("noAdminUserFound"));
        Component titlePanel = UIUtils.createTitlePanel(LocaleManager.getString("createAdminUser"));

        loginDialog.getContentPane().add(titlePanel, BorderLayout.NORTH);
        loginPanel.setLoginAction(new CreateAdminAction());
        loginPanel.showConfirmPassword(true);
        loginDialog.setVisible(true);
        loginDialog.getContentPane().remove(titlePanel);
        titlePanel = UIUtils.createTitlePanel(LocaleManager.getString("Logining"));
        loginDialog.getContentPane().add(titlePanel, BorderLayout.NORTH);
        loginPanel.setLoginAction(new LoginAction());
        loginPanel.showConfirmPassword(false);
    }

    private void initComponents() {
        final JXLayer layer = new JXLayer(loginPanel = new LoginPanel());

        layer.setUI(blurUI);
        loginPanel.setLoginAction(new LoginAction());
        loginPanel.setExitAction(new ExitAction());
        loginPanel.showConfirmPassword(false);
        loginDialog = new JDialog(Application.getInstance(SodalisApplication.class).getMainFrame(), true);
        loginDialog.setTitle(LocaleManager.getString("Logining"));
        loginDialog.setContentPane(layer);
        loginDialog.setSize(300, 175);
        loginDialog.setLocationRelativeTo(null);
        loginDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        UIUtils.makeISDialog(loginDialog);
    }

    public boolean login() {
        if (firstTime) {
            try {
                checkAdminExistence();
            } catch (Exception e) {
            }

            firstTime = false;
            if (System.getProperty("defaultUser") != null) {
                try {
                    loginContext.login();
                    loggedSubject = loginContext.getSubject();
                    fireSubjectLoggedIn(loggedSubject);
                    return true;
                } catch (LoginException e) {
                }
            }
        }

        loginPanel.resetPassword();
        loginDialog.setVisible(true);
        fireSubjectLoggedIn(loggedSubject);

        return loggedSubject != null;
    }

    public boolean logout() {
        try {
            loginContext.logout();
            fireSubjectLoggedOut(loggedSubject);
            loggedSubject = null;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SodalisApplication.get().showWelcomePage();
                    login();
                }
            }).start();

            return true;
        } catch (LoginException ex) {
            LoggerManager.getInstance().error(LoginManagerService.class, ex);
        }
        return false;
    }

    private void initLoginContext() {
        try {
            SodalisConfiguration.initConfiguration();
            loginContext = new LoginContext("sodalis", new CallbackHandler() {

                @Override
                public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                    final NameCallback nameCallback = (NameCallback) callbacks[0];
                    final PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];

                    nameCallback.setName(loginPanel.getUserName());
                    passwordCallback.setPassword(loginPanel.getPassword());
                }
            });
        } catch (LoginException ex) {
            LoggerManager.getInstance().error(LoginManagerService.class, ex);
        }
    }

    private void wrongPassword() {
        if (lockTimer == null) {
            lockTimer = new Timer(3000, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    blurUI.setLocked(false);
                    lockTimer.stop();
                }
            });
        }

        blurUI.setLocked(true);
        lockTimer.start();
    }

    public SodalisUser getUserForUID(String userUID) {
        return SecurityDataManager.getInstance().getDatabaseEntity(SodalisUser.class, "userUID='" + userUID + "'");
    }

    @Override
    public void applicationWillExit() {
        fireSubjectLoggedOut(loggedSubject);
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    @Override
    public void registerServiceListener(ServiceListener listener) {
    }

    private class CreateAdminAction extends AbstractAction {

        public CreateAdminAction() {
            super(LocaleManager.getString("okAction"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final SodalisUser sodalisUser = new SodalisUser();

            sodalisUser.setAdmin(true);
            sodalisUser.setUserName(loginPanel.getUserName());
            sodalisUser.setPassword(CryptoUtils.makeDigest(String.valueOf(loginPanel.getPassword()), sodalisUser.getUserName()));

            saveUser(sodalisUser);

            loginDialog.setVisible(false);
        }

        private void saveUser(SodalisUser sodalisUser) {
            SecurityDataManager.getInstance().addDatabaseEntity(sodalisUser);
        }
    }

    private class LoginAction extends AbstractAction {

        public LoginAction() {
            super(LocaleManager.getString("login"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                loginContext.login();
                loggedSubject = loginContext.getSubject();
                loginDialog.setVisible(false);
            } catch (LoginException ex) {
                loginPanel.resetPassword();
                wrongPassword();
            }
        }
    }

    private class ExitAction extends AbstractAction {

        public ExitAction() {
            super(LocaleManager.getString("Exit"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Application.getInstance().exit();
        }
    }
}
