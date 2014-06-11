
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.category.entity;

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity;
import sk.magiksoft.sodalis.core.entity.DatabaseEntity;
import sk.magiksoft.sodalis.core.entity.PostCreation;
import sk.magiksoft.sodalis.core.history.Historizable;
import sk.magiksoft.sodalis.core.history.HistoryEvent;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.search.FullText;

import java.util.*;

/**
 * @author wladimiiir
 */
public class Category extends AbstractDatabaseEntity implements Historizable {
    @FullText protected Category parentCategory;
    @FullText protected String name = "";
    @FullText protected String description = "";
    protected List<Category> childCategories = new LinkedList<Category>();
    @FullText protected Map<Class<? extends CategoryData>, CategoryData> categoryDatas = new HashMap<Class<? extends CategoryData>, CategoryData>();

    public Category() {
    }

    public Category(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public Category(Category parentCategory, String name) {
        this.parentCategory = parentCategory;
        this.name = name;
    }

    public Category(Category parentCategory, String name, Long id) {
        this.parentCategory = parentCategory;
        this.name = name;
        setId(id);
    }

    @PostCreation
    public void initCategoryDatas(Object... switches) {
        for (Object s : switches) {
            if (s instanceof Class && CategoryData.class.isAssignableFrom((Class) s)) {
                try {
                    categoryDatas.put((Class<? extends CategoryData>) s, (CategoryData) ((Class) s).newInstance());
                } catch (InstantiationException ex) {
                    LoggerManager.getInstance().error(Category.class, ex);
                } catch (IllegalAccessException ex) {
                    LoggerManager.getInstance().error(Category.class, ex);
                }
            }
        }
    }


    public Map<Class<? extends CategoryData>, CategoryData> getCategoryDatas() {
        return categoryDatas;
    }

    public void setCategoryDatas(Map<Class<? extends CategoryData>, CategoryData> categoryDatas) {
        this.categoryDatas = categoryDatas;
    }

    public <T extends CategoryData> T getCategoryData(Class<T> clazz) {
        return (T) categoryDatas.get(clazz);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public List<Category> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<Category> childCategories) {
        this.childCategories = childCategories;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof Category) || entity==this) {
            return;
        }

        final Category category = (Category) entity;

        this.name = category.name;
        this.description = category.description;
        this.parentCategory = category.parentCategory;
        this.childCategories.clear();
        this.childCategories.addAll(category.childCategories);
        for (CategoryData categoryData : categoryDatas.values()) {
            categoryData.updateFrom(category.getCategoryData(categoryData.getClass()));
        }
    }



    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void addHistoryEvent(HistoryEvent event) {
        List<HistoryEvent> historyEvents = getCategoryData(CategoryHistoryData.class).getHistoryEvents();

        historyEvents.add(event);
    }

    @Override
    public List<HistoryEvent> getHistoryEvents(Long entityID) {
        return getCategoryData(CategoryHistoryData.class)==null ? Collections.<HistoryEvent>emptyList() : getCategoryData(CategoryHistoryData.class).getHistoryEvents();
    }
}