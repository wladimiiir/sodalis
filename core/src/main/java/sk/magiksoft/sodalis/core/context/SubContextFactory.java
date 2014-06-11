
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.context;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.table.ObjectTableModel;

/**
 *
 * @author wladimiiir
 */
public final class SubContextFactory {

    private SubContextFactory() {
    }

    public static List<SubContext> getSubContexts(URL subContextXMLURL) {
        List<SubContext> subContexts = new ArrayList<SubContext>();

        if(subContextXMLURL==null){
            return subContexts;
        }
        try {
            Element root = new SAXBuilder().build(subContextXMLURL).getRootElement();
            List subContextElements = root.getChildren("SubContext");
            for (int i = 0; i < subContextElements.size(); i++) {
                Element subContextElement = (Element) subContextElements.get(i);
                String name = subContextElement.getAttribute("name").getValue();
                String tableModelClassName = subContextElement.getChild("TableModel").getValue();
                String objectClassName = subContextElement.getChild("ObjectClass").getValue();
                String addActionClassName = subContextElement.getChild("AddAction").getValue();
                try {
                    ObjectTableModel tableModel = (ObjectTableModel) Class.forName(tableModelClassName).newInstance();
                    Class objectClass = Class.forName(objectClassName);
                    AbstractAction addAction = (AbstractAction) Class.forName(addActionClassName).newInstance();
                    SubContext subContext = new SubContext(name, tableModel, objectClass, addAction);
                    subContexts.add(subContext);
                } catch (InstantiationException ex) {
                    LoggerManager.getInstance().error(SubContextFactory.class, ex);
                } catch (IllegalAccessException ex) {
                    LoggerManager.getInstance().error(SubContextFactory.class, ex);
                } catch (ClassNotFoundException ex) {
                    LoggerManager.getInstance().error(SubContextFactory.class, ex);
                }
            }
        } catch (JDOMException ex) {
            LoggerManager.getInstance().error(SubContextFactory.class, ex);
        } catch (IOException ex) {
            LoggerManager.getInstance().error(SubContextFactory.class, ex);
        }
        return subContexts;
    }
}