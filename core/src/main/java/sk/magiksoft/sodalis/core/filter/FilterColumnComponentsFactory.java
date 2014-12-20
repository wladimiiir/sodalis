package sk.magiksoft.sodalis.core.filter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
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
        columnComponents = new ArrayList<>();
        ColumnComponent columnComponent;
        Document document;
        Element root;
        String clazz;
        String labelText;
        String select, from, where;
        Element items;
        String enumeration;

        try {
            final SAXBuilder saxBuilder = new SAXBuilder(XMLReaders.NONVALIDATING);
            saxBuilder.setEntityResolver(new EntityResolver() {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    return new InputSource(FilterColumnComponentsFactory.class.getResourceAsStream("ColumnComponents.dtd"));
                }
            });
            document = saxBuilder.build(fileURL);
            root = document.getRootElement();
            for (Element columnComponentElement : root.getChildren("ColumnComponent")) {
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
                        for (Object object : objects) {
                            columnComponent.addItem(object);
                        }
                    }
                    enumeration = items.getAttributeValue("enumeration");
                    if (enumeration != null) {
                        Enumeration e = EnumerationFactory.getInstance().getEnumeration(enumeration);
                        for (EnumerationEntry enumerationEntry : e.getEntries()) {
                            columnComponent.addItem(enumerationEntry.toString());
                        }
                    }
                    for (Element child : items.getChildren()) {
                        columnComponent.addItem(child.getTextTrim());
                    }
                }

                columnComponents.add(columnComponent);
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IOException | JDOMException ex) {
            LoggerManager.getInstance().error(FilterColumnComponentsFactory.class, ex);
        }
    }

    public List<ColumnComponent> getColumnComponents() {
        return columnComponents;
    }
}
