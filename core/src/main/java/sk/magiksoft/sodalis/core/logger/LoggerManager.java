package sk.magiksoft.sodalis.core.logger;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

/**
 * @author wladimiiir
 */
public class LoggerManager {
    private static final boolean DEBUG_LOG = Boolean.getBoolean("debugLog");

    private static LoggerManager instance = null;
    private Logger errorLogger;
    private Logger warnLogger;
    private Logger infoLogger;
    private Logger debugLogger;

    private LoggerManager() {
        initLogger();
    }

    private void initLogger() {
        Handler handler;

        new File("logs").mkdirs();

        errorLogger = Logger.getLogger("error");
        warnLogger = Logger.getLogger("warn");
        infoLogger = Logger.getLogger("info");
        debugLogger = Logger.getLogger("debug");
        try {
            handler = new FileHandler("logs/error.log", 10000000, 1, true);
            handler.setFormatter(new SimpleFormatter());
            errorLogger.addHandler(handler);

            handler = new FileHandler("logs/warn.log", 10000000, 1, true);
            handler.setFormatter(new SimpleFormatter());
            warnLogger.addHandler(handler);

            handler = new FileHandler("logs/info.log", 10000000, 1, true);
            handler.setFormatter(new SimpleFormatter());
            infoLogger.addHandler(handler);

            handler = new FileHandler("logs/debug.log", 10000000, 1, true);
            handler.setFormatter(new SimpleFormatter());
            debugLogger.addHandler(handler);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    public static LoggerManager getInstance() {
        if (instance == null) {
            instance = new LoggerManager();
        }
        return instance;
    }

    public void error(Class clazz, Throwable e) {
        error(clazz.getName(), e);
    }

    public void error(String className, Throwable e) {
        errorLogger.log(Level.SEVERE, e.getMessage(), e);
    }

    public void warn(Class clazz, Throwable e) {
        warn(clazz.getName(), e);
    }

    public void warn(String className, Throwable e) {
        warnLogger.log(Level.WARNING, e.getMessage(), e);
    }

    public void info(Class clazz, Throwable e) {
        infoLogger.log(Level.INFO, e.getMessage(), e);
    }

    public void info(Class clazz, String message) {
        infoLogger.log(Level.INFO, message);
    }

    public void debug(Class clazz, String message) {
        if (DEBUG_LOG) {
            debugLogger.log(Level.INFO, clazz.getName() + ": " + message);
        }
    }
}
