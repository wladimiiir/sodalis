
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.enumeration;

import org.hibernate.Hibernate;
import org.hibernate.type.Type;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author wladimiiir
 */
public class EnumerationFactory implements DataListener {

    public static final URL ENUMERATION_FILE_URL = EnumerationFactory.class.getResource("enumeration.xml");
    private static EnumerationFactory instance = null;
    private Map<String, Enumeration> enumerationMap = new HashMap<String, Enumeration>();
    private ClientDataManager dataManager = new ClientDataManager();
    private List<DataListener> dataListeners = new LinkedList<DataListener>();

    private EnumerationFactory() {
        instance = this;
        dataManager.addDataListener(this);
//        initEnumerations();
    }

    public synchronized static EnumerationFactory getInstance() {
        if (instance == null) {
            new EnumerationFactory();
        }
        return instance;
    }

    public Enumeration getEnumeration(String name) {
        if (enumerationMap.get(name) == null || enumerationMap.get(name).getId() == null) {
            initEnumeration(name);
        }
        return enumerationMap.get(name);
    }

    private void initEnumeration(String name) {
        Enumeration enumeration = new Enumeration(name);
        loadEnumeration(enumeration);
        enumerationMap.put(name, enumeration);
    }

    private void initEnumerations() {
        Document document;
        Element enumeration;
        String name;

        try {
            document = new SAXBuilder().build(ENUMERATION_FILE_URL);
            for (int i = 0; i < document.getRootElement().getChildren().size(); i++) {
                enumeration = (Element) document.getRootElement().getChildren().get(i);
                name = enumeration.getAttributeValue("name");
                enumerationMap.put(name, null);
            }
        } catch (JDOMException ex) {
            LoggerManager.getInstance().error(EnumerationFactory.class, ex);
        } catch (IOException ex) {
            LoggerManager.getInstance().error(EnumerationFactory.class, ex);
        }
    }

    //TODO: optimilization - duplicates

    public void importEnumerations(URL url) {
        Document document;
        Element enumElement;
        String name;
        String infoClass;
        String entryID;
        EnumerationInfo enumerationInfo;
        Enumeration enumeration;
        EnumerationEntry enumerationEntry;

        try {
            document = new SAXBuilder().build(url);
            for (int i = 0; i < document.getRootElement().getChildren().size(); i++) {
                enumElement = (Element) document.getRootElement().getChildren().get(i);
                name = enumElement.getAttributeValue("name");
                infoClass = enumElement.getAttributeValue("info");
                enumeration = new Enumeration(name);
                enumerationInfo = null;
                if (infoClass != null && !infoClass.trim().isEmpty()) {
                    try {
                        enumerationInfo = (EnumerationInfo) Class.forName(infoClass).newInstance();
                    } catch (InstantiationException ex) {
                        LoggerManager.getInstance().warn(EnumerationFactory.class, ex);
                    } catch (IllegalAccessException ex) {
                        LoggerManager.getInstance().warn(EnumerationFactory.class, ex);
                    } catch (ClassNotFoundException ex) {
                        LoggerManager.getInstance().warn(EnumerationFactory.class, ex);
                    }
                }
                if (enumerationInfo == null) {
                    enumerationInfo = new DefaultEnumerationInfo();
                }
                enumeration.setEnumerationInfoClass(enumerationInfo.getClass());
                for (int j = 0; j < enumElement.getChildren().size(); j++) {
                    Element valueElement = (Element) enumElement.getChildren().get(j);

                    try {
                        enumerationEntry = enumerationInfo.getEnumerationEntryClass().newInstance();
                        entryID = valueElement.getAttributeValue("id");
                        if (entryID != null) {
                            enumerationEntry.setEntryID(Long.valueOf(entryID));
                        }
                        if (valueElement.getChildren().isEmpty()) {
                            enumerationEntry.setText(valueElement.getValue());
                        } else {
                            for (int k = 0; k < valueElement.getChildren().size(); k++) {
                                Element subValueElement = (Element) valueElement.getChildren().get(k);
                                String methodName = "set" + subValueElement.getName().substring(0, 1).toUpperCase() + subValueElement.getName().substring(1);
                                Method method = enumerationInfo.getEnumerationEntryClass().getMethod(methodName, String.class);

                                method.invoke(enumerationEntry, subValueElement.getValue());
                            }
                        }

                        enumeration.addEntry(enumerationEntry);
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    } catch (SecurityException ex) {
                        ex.printStackTrace();
                    } catch (InstantiationException ex) {
                        ex.printStackTrace();
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                }

                resolveDuplicates(enumeration);
                saveEnumeration(enumeration);
            }
        } catch (JDOMException ex) {
            LoggerManager.getInstance().error(EnumerationFactory.class, ex);
        } catch (IOException ex) {
            LoggerManager.getInstance().error(EnumerationFactory.class, ex);
        }

    }

    private void resolveDuplicates(Enumeration enumeration) {
        Enumeration dbEnumeration = getEnumeration(enumeration.getName());

        if (dbEnumeration == null || dbEnumeration.getId() == null) {
            return;
        }

        dataManager.removeDatabaseEntity(dbEnumeration);
    }

    void invalidateEnumeration(Enumeration enumeration) {
        enumerationMap.remove(enumeration.getName());
    }

    public void exportEnumerations(File file) {
        //TODO: export to file
    }

    void saveEnumeration(Enumeration enumeration) {
        dataManager.addOrUpdateEntity(enumeration);
    }

    void loadEnumeration(Enumeration enumeration) {
        Enumeration dbEnumeration;
        List<Enumeration> enumerations;

        enumerations = dataManager.getDatabaseEntities("from Enumeration enumeration where enumeration.name = ?",
                new Object[]{enumeration.getName()},
                new Type[]{Hibernate.STRING});
        dbEnumeration = enumerations.isEmpty() ? null : enumerations.get(0);
        enumeration.setEnumerationInfoClass(DefaultEnumerationInfo.class);
        enumeration.updateFrom(dbEnumeration);
        enumeration.setId(dbEnumeration == null ? null : dbEnumeration.getId());
        Collections.sort(enumeration.getEntries());
    }

    public void addDataListener(DataListener listener) {
        dataListeners.add(listener);
    }

    public List<Enumeration> getEnumerations() {
        final List<Enumeration> enumerations = dataManager.getDatabaseEntities(Enumeration.class);
        for (Enumeration enumeration : enumerations) {
            Collections.sort(enumeration.getEntries());
        }
        return enumerations;
    }

    @Override
    public void entitiesAdded(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (object instanceof Enumeration) {
                invalidateEnumeration((Enumeration) object);
                loadEnumeration((Enumeration) object);
                for (DataListener dataListener : dataListeners) {
                    dataListener.entitiesAdded(entities);
                }
            }
        }
    }

    @Override
    public void entitiesUpdated(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (object instanceof Enumeration) {
                invalidateEnumeration((Enumeration) object);
                loadEnumeration((Enumeration) object);
                for (DataListener dataListener : dataListeners) {
                    dataListener.entitiesUpdated(entities);
                }
            }
        }
    }

    @Override
    public void entitiesRemoved(List<? extends DatabaseEntity> entities) {
        for (Object object : entities) {
            if (object instanceof Enumeration) {
                invalidateEnumeration((Enumeration) object);
                for (DataListener dataListener : dataListeners) {
                    dataListener.entitiesRemoved(entities);
                }
            }
        }
    }
}