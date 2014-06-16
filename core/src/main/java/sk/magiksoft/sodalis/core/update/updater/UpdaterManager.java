
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.update.updater;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * @author wladimiiir
 */
public class UpdaterManager {

    private static final Comparator<Updater> UPDATER_COMPARATOR = new Comparator<Updater>() {
        @Override
        public int compare(Updater o1, Updater o2) {
            UpdaterSequence sequence1 = o1.getClass().getAnnotation(UpdaterSequence.class);
            UpdaterSequence sequence2 = o2.getClass().getAnnotation(UpdaterSequence.class);
            int value1 = sequence1 == null ? 1 : sequence1.value();
            int value2 = sequence2 == null ? 1 : sequence2.value();

            return value1 - value2;
        }
    };
    private static final String UPDATERS_JAR = "data/temp/update/updaters.jar";
    private static UpdaterManager instance;

    public UpdaterManager() {
    }

    public static UpdaterManager getInstance() {
        if (instance == null) {
            instance = new UpdaterManager();
        }
        return instance;
    }

    public boolean isUpdatersAvailable() {
        return new File(UPDATERS_JAR).exists();
    }

    public void proceedUpdaters() throws Exception {
        proceedUpdaters(new File(UPDATERS_JAR));
    }

    public void proceedUpdaters(File file) throws Exception {
        List<Updater> updaters = getUpdaters(file);

        for (Updater updater : updaters) {
            updater.runUpdate();
        }
    }

    public static void main(String[] args) throws Exception {
        UpdaterManager.getInstance().proceedUpdaters();
    }

    private List<Updater> getUpdaters(File file) throws IOException {
        URLClassLoader clazzLoader = URLClassLoader.newInstance(new URL[]{
                new URL("jar:file://" + file.getAbsolutePath().replaceAll("\\\\", "/") + "!/")
        });
        List<Updater> updaters = new ArrayList<Updater>();

        JarInputStream jis = new JarInputStream(new FileInputStream(file));
        JarEntry jarEntry;

        while ((jarEntry = jis.getNextJarEntry()) != null) {
            if (!jarEntry.getName().endsWith(".class")) {
                continue;
            }
            try {
                String className = jarEntry.getName().substring(0, jarEntry.getName().lastIndexOf(".class"));
                Class clazz = Class.forName(className.replaceAll("/", "."), true, clazzLoader);

                if (Updater.class.isAssignableFrom(clazz)) {
                    updaters.add((Updater) clazz.newInstance());
                }
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        jis.close();

        Collections.sort(updaters, UPDATER_COMPARATOR);

        return updaters;
    }
}