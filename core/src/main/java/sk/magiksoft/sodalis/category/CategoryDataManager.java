
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.category;

import sk.magiksoft.sodalis.category.entity.Category;
import sk.magiksoft.sodalis.core.data.DataListener;
import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.module.Module;

import java.util.*;

/**
 * @author wladimiiir
 */
public class CategoryDataManager extends ClientDataManager implements DataListener {

    private final static CategoryDataManager instance = new CategoryDataManager();
    private final Map<Long, Category> categoryMap = new HashMap<Long, Category>();
    private final Vector<DataListener> dataListeners = new Vector<DataListener>();

    protected CategoryDataManager() {
        loadCategories();
        super.addDataListener(this);
    }

    @Override
    public void addDataListener(DataListener listener) {
        dataListeners.add(listener);
    }

    private void fireRecordsAdded(List<? extends DatabaseEntity> records) {
        for (int i = dataListeners.size() - 1; i >= 0; i--) {
            dataListeners.get(i).entitiesAdded(records);
        }
    }

    private void fireRecordsUpdated(List<? extends DatabaseEntity> records) {
        for (int i = dataListeners.size() - 1; i >= 0; i--) {
            dataListeners.get(i).entitiesUpdated(records);
        }
    }

    private void fireRecordsRemoved(List<? extends DatabaseEntity> records) {
        for (int i = dataListeners.size() - 1; i >= 0; i--) {
            dataListeners.get(i).entitiesRemoved(records);
        }
    }

    private void loadCategories() {
        final List<Category> categories = getDatabaseEntities(Category.class);
        for (Category category : categories) {
            categoryMap.put(category.id(), category);
        }
    }

    public static synchronized CategoryDataManager getInstance() {
        return instance;
    }

    public Category getInternalCategory(Long internalID) {
        for (Category category : categoryMap.values()) {
            if (category.internalID() != null && category.internalID().equals(internalID)) {
                return category;
            }
        }

        return null;
    }

    public Category getRootCategory() {
        final Category rootCategory = new Category();

        for (Category category : categoryMap.values()) {
            if (category.getParentCategory() == null) {
                rootCategory.getChildCategories().add(category);
            }
        }

        return rootCategory;
    }

    public Category getCategory(Long id) {
        return categoryMap.get(id);
    }

    public List<Category> getCategories(List<Long> ids) {
        final List<Category> categories = new LinkedList<Category>();

        for (Long id : ids) {
            if (categoryMap.containsKey(id)) {
                categories.add(categoryMap.get(id));
            }
        }

        return categories;
    }

    public Category getModuleCategory(Module module) {
        if (module == null) {
            return null;
        }

        final String moduleName = module.getModuleDescriptor().getDescription();
        for (Category category : categoryMap.values()) {
            if (category.getParentCategory() == null && category.getName().equals(moduleName)) {
                return category;
            }
        }

        return null;
    }

    @Override
    public synchronized void entitiesAdded(List<? extends DatabaseEntity> entities) {
        final List<Category> categories = new LinkedList<Category>();
        for (DatabaseEntity entity : entities) {
            if (!(entity instanceof Category)) {
                continue;
            }
            categories.add((Category) entity);
            categoryMap.put(entity.getId(), (Category) entity);
            if (((Category) entity).getParentCategory() != null) {
                final Category parent = categoryMap.get(((Category) entity).getParentCategory().id());
                boolean found = false;
                for (Category child : parent.getChildCategories()) {
                    if (child.id().equals(((Category) entity).id())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    parent.getChildCategories().add((Category) entity);
                }
            }
        }
        if (!categories.isEmpty()) {
            fireRecordsAdded(categories);
        }
    }

    @Override
    public synchronized void entitiesUpdated(List<? extends DatabaseEntity> entities) {
        final List<Category> categories = new LinkedList<Category>();
        for (DatabaseEntity entity : entities) {
            if (!(entity instanceof Category)) {
                continue;
            }
            categories.add((Category) entity);
            categoryMap.put(entity.getId(), (Category) entity);
        }
        if (!categories.isEmpty()) {
            fireRecordsUpdated(categories);
        }
    }

    @Override
    public synchronized void entitiesRemoved(List<? extends DatabaseEntity> entities) {
        final List<Category> categories = new LinkedList<Category>();
        for (DatabaseEntity entity : entities) {
            if (!(entity instanceof Category)) {
                continue;
            }
            categories.add((Category) entity);
            categoryMap.remove(entity.getId());
            if (((Category) entity).getParentCategory() != null) {
                final Category parent = categoryMap.get(((Category) entity).getParentCategory().id());
                final List<Category> childCategories = parent.getChildCategories();
                for (int index = 0, childCategoriesSize = childCategories.size(); index < childCategoriesSize; index++) {
                    final Category child = childCategories.get(index);
                    if (child.id().equals(((Category) entity).id())) {
                        parent.getChildCategories().remove(index);
                        break;
                    }
                }
            }
        }
        if (!categories.isEmpty()) {
            fireRecordsRemoved(categories);
        }
    }
}