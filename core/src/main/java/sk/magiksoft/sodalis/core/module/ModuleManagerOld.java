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
public class ModuleManagerOld implements ModuleManager {
    private final Vector<Module> availableModules = new Vector<>();

    public ModuleManagerOld(File moduleXMLFile) {
        loadModules(moduleXMLFile);
    }

    private void loadModules(File moduleXMLFile) {
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = builder.build(moduleXMLFile);
            Element root = document.getRootElement();
            root = root.getChild("modules");
            List<Element> moduleElements = root.getChildren("module");

            for (Element moduleElement : moduleElements) {
                String className = moduleElement.getValue();
                if (className.trim().isEmpty()) {
                    continue;
                }
                loadModule(className);
            }
        } catch (IOException | JDOMException ex) {
            LoggerManager.getInstance().error(ModuleManagerOld.class, ex);
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
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            LoggerManager.getInstance().error(ModuleManagerOld.class, ex);
        }
    }

    @Override
    public Module getModule(int index) {
        try {
            return availableModules.get(index);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public <T extends Module> T getModuleByClass(Class<T> moduleClass) {
        for (Module module : availableModules) {
            if (module.getClass() == moduleClass) {
                return moduleClass.cast(module);
            }
        }
        return null;
    }

    @Override
    public <T extends Module> T getModuleBySuperClass(Class<T> moduleSuperClass) {
        for (Module module : availableModules) {
            if (moduleSuperClass.isAssignableFrom(module.getClass())) {
                return moduleSuperClass.cast(module);
            }
        }
        return null;
    }

    @Override
    public boolean isModulePresent(Class<? extends Module> moduleClass) {
        return getModuleByClass(moduleClass) != null;
    }

    @Override
    public List<Module> getModules() {
        return new ArrayList<>(availableModules);
    }

    @Override
    public int getModulesCount() {
        return availableModules.size();
    }
}
