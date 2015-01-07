package sk.magiksoft.sodalis.core;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Y12370
 * @since 2015/01/07
 */
public class ClassPathLoaderInjector {
    public static void injectLibraryDir(File directory) {
        final File[] libraryFiles = directory.listFiles((dir, name) -> name.endsWith(".jar"));
        if (libraryFiles == null) {
            return;
        }
        for (File libraryFile : libraryFiles) {
            injectLibraryFile(libraryFile);
        }
    }

    public static void injectLibraryFile(File libraryFile) {
        final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        final Method addUrlMethod;
        final boolean accessible;

        try {
            addUrlMethod = classLoader.getClass().getDeclaredMethod("addUrl", URL.class);
            accessible = addUrlMethod.isAccessible();
        } catch (NoSuchMethodException e) {
            return;
        }

        try {
            addUrlMethod.setAccessible(true);
            addUrlMethod.invoke(classLoader, libraryFile.toURI().toURL());
        } catch (InvocationTargetException | MalformedURLException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            addUrlMethod.setAccessible(accessible);
        }
    }
}
