package sk.magiksoft.sodalis.core.module;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author wladimiiir
 */
public class ModuleManager {
    private Vector<Module> availableModules;

    public ModuleManager(File moduleXMLFile) {
        loadModules(moduleXMLFile);
    }

    private void loadModules(File moduleXMLFile) {
        availableModules = new Vector<Module>();
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = builder.build(moduleXMLFile);
            Element root = document.getRootElement();
            root = root.getChild("modules");
            List moduleElements = root.getChildren("module");

            for (int i = 0; i < moduleElements.size(); i++) {
                Element element = (Element) moduleElements.get(i);
                String className = element.getValue();
                if (className == null || className.trim().isEmpty()) {
                    continue;
                }
                loadModule(className);
            }
        } catch (IOException ex) {
            LoggerManager.getInstance().error(ModuleManager.class, ex);
        } catch (JDOMException ex) {
            LoggerManager.getInstance().error(ModuleManager.class, ex);
        }
    }

    private void loadModule(String className) {
        try {
            Class moduleClass = Class.forName(className);
            Object module = moduleClass.newInstance();
            if (!(module instanceof Module)) {
                return;
            }

            availableModules.add((Module) module);
        } catch (InstantiationException ex) {
            LoggerManager.getInstance().error(ModuleManager.class, ex);
        } catch (IllegalAccessException ex) {
            LoggerManager.getInstance().error(ModuleManager.class, ex);
        } catch (ClassNotFoundException ex) {
            LoggerManager.getInstance().error(ModuleManager.class, ex);
        }
    }

    public Module getModule(int index) {
        try {
            return availableModules.get(index);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public <T extends Module> T getModuleByClass(Class<T> moduleClass) {
        for (Module module : availableModules) {
            if (module.getClass() == moduleClass) {
                return (T) module;
            }
        }
        return null;
    }

    public <T extends Module> T getModuleBySuperClass(Class<T> moduleSuperClass) {
        for (Module module : availableModules) {
            if (moduleSuperClass.isAssignableFrom(module.getClass())) {
                return (T) module;
            }
        }
        return null;
    }

    public boolean isModulePresent(Class<? extends Module> moduleClass) {
        return getModuleByClass(moduleClass) != null;
    }

    public List<Module> getModules() {
        return new ArrayList<Module>(availableModules);
    }

    public int getModulesCount() {
        return availableModules.size();
    }
}
