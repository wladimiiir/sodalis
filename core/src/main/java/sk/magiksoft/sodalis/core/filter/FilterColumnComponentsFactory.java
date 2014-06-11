
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.filter;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import sk.magiksoft.sodalis.core.enumeration.Enumeration;
import sk.magiksoft.sodalis.core.enumeration.EnumerationEntry;
import sk.magiksoft.sodalis.core.enumeration.EnumerationFactory;
import sk.magiksoft.sodalis.core.filter.element.ColumnComponent;
import sk.magiksoft.sodalis.core.logger.LoggerManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wladimiiir
 */
public class FilterColumnComponentsFactory {

    private URL fileURL;
    private List<ColumnComponent> columnComponents;

    public FilterColumnComponentsFactory(URL fileURL) {
        this.fileURL = fileURL;
        loadColumnComponents();
    }

    private void loadColumnComponents() {
        columnComponents = new ArrayList<ColumnComponent>();
        ColumnComponent columnComponent;
        Document document;
        Element root;
        String clazz;
        String labelText;
        String select, from, where;
        Element items;
        String enumeration;
        Object item;

        try {
            document = new SAXBuilder().build(fileURL);
            root = document.getRootElement();
            for (int i = 0; i < root.getChildren("ColumnComponent").size(); i++) {
                Element columnComponentElement = (Element) root.getChildren().get(i);
                clazz = columnComponentElement.getAttributeValue("class");
                labelText = columnComponentElement.getChildText("label_text");
                select = columnComponentElement.getChildText("select");
                from = columnComponentElement.getChildText("from");
                where = columnComponentElement.getChildText("where");
                items = columnComponentElement.getChild("items");
                columnComponent = (ColumnComponent) Class.forName(clazz).newInstance();
                columnComponent.setLabelText(labelText);
                if (select != null) {
                    columnComponent.setSelect(select);
                }
                if (from != null) {
                    columnComponent.setFrom(from);
                }
                if (where != null) {
                    columnComponent.setWhere(where);
                }
                if (items != null) {
                    enumeration = items.getAttributeValue("enumclass");
                    if (enumeration != null) {
                        Object[] objects = Class.forName(enumeration).getEnumConstants();
                        for (int j = 0; j < objects.length; j++) {
                            item = objects[j];
                            columnComponent.addItem(item);
                        }
                    }
                    enumeration = items.getAttributeValue("enumeration");
                    if (enumeration != null) {
                        Enumeration e = EnumerationFactory.getInstance().getEnumeration(enumeration);
                        for (EnumerationEntry enumerationEntry : e.getEntries()) {
                            columnComponent.addItem(enumerationEntry.toString());
                        }
                    }
                    for (int j = 0; j < items.getChildren().size(); j++) {
                        item = ((Element) items.getChildren().get(j)).getTextTrim();
                        columnComponent.addItem(item);
                    }
                }

                columnComponents.add(columnComponent);
            }
        } catch (InstantiationException ex) {
            LoggerManager.getInstance().error(FilterColumnComponentsFactory.class, ex);
        } catch (IllegalAccessException ex) {
            LoggerManager.getInstance().error(FilterColumnComponentsFactory.class, ex);
        } catch (ClassNotFoundException ex) {
            LoggerManager.getInstance().error(FilterColumnComponentsFactory.class, ex);
        } catch (JDOMException ex) {
            LoggerManager.getInstance().error(FilterColumnComponentsFactory.class, ex);
        } catch (IOException ex) {
            LoggerManager.getInstance().error(FilterColumnComponentsFactory.class, ex);
        }
    }

    public List<ColumnComponent> getColumnComponents() {
        return columnComponents;
    }
}