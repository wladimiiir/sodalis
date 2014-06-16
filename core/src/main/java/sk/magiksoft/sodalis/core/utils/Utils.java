
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.utils;

import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author wladimiiir
 */
public class Utils {
    public static final String OS_WINDOWS = "windows";
    public static final String OS_MAC = "mac";
    public static final String OS_UNIX = "unix";
    public static final String OS_UNKNOWN = "unknown";


    private Utils() {
    }

    public static List<Long> getIDList(List<? extends DatabaseEntity> entities) {
        List<Long> ids = new ArrayList<Long>();

        for (DatabaseEntity databaseEntity : entities) {
            ids.add(databaseEntity.getId());
        }

        return ids;
    }

    public static void clearDatabaseEntityIDs(DatabaseEntity databaseEntity) {
        clearDatabaseEntityIDs(new Stack<DatabaseEntity>(), databaseEntity);
    }

    public static void clearDatabaseEntityIDs(Stack<DatabaseEntity> stack, DatabaseEntity databaseEntity) {
        Field[] fields = databaseEntity.getClass().getDeclaredFields();
        Object value;

        if (stack.contains(databaseEntity)) {
            return;
        }
        stack.push(databaseEntity);
        databaseEntity.setId(null);
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                value = field.get(databaseEntity);
                if (value instanceof DatabaseEntity) {
                    clearDatabaseEntityIDs(stack, (DatabaseEntity) value);
                } else if (value instanceof List) {
                    for (Object item : ((List) value)) {
                        if (item instanceof DatabaseEntity) {
                            clearDatabaseEntityIDs(stack, (DatabaseEntity) item);
                        }
                    }
                } else if (value instanceof Map) {
                    for (Object item : ((Map) value).values()) {
                        if (item instanceof DatabaseEntity) {
                            clearDatabaseEntityIDs(stack, (DatabaseEntity) item);
                        }
                    }
                }
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        stack.pop();
    }

    public static URL getURL(String spec) {
        try {
            return new URL(spec);
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    public static <T extends Cloneable> List<T> cloneItems(List<T> items) {
        if (items == null) {
            return null;
        }

        List<T> cloned = new ArrayList<T>(items.size());

        for (T item : items) {
            try {
                cloned.add((T) item.getClass().getMethod("clone").invoke(item));
            } catch (Exception e) {
                LoggerManager.getInstance().error(Utils.class, e);
            }
        }

        return cloned;
    }

    public static String getOSName() {
        final String osName = System.getProperty("os.name");
        if (osName.contains("win")) {
            return OS_WINDOWS;
        } else if (osName.contains("mac")) {
            return OS_MAC;
        } else if (osName.contains("nux") || osName.contains("unix")) {
            return OS_UNIX;
        }

        return OS_UNKNOWN;
    }

    public static boolean isWindows() {
        return getOSName().equals(OS_WINDOWS);
    }

    public static String getClassPath() {
        final StringBuilder classPath = new StringBuilder();
        final File[] files = new File("lib").listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });

        for (File file : files) {
            if (classPath.length() > 0) {
                classPath.append(File.pathSeparatorChar);
            }
            classPath.append("lib").append(File.separatorChar).append(file.getName());
        }

        return classPath.toString();
    }

    public static File[] getModuleJarFiles() {
        return new File("lib").listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("sodalis-") && name.endsWith(".jar");
            }
        });
    }
}