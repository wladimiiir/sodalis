
/** *********************************************\
  * Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
  * Sodalis 2007-2011                            *
  * http://www.sodalis.sk                        *
\ ***********************************************/


package sk.magiksoft.sodalis.item.factory

import java.net.URL
import sk.magiksoft.sodalis.item.entity.ItemProperty

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jun 10, 2010
 * Time: 3:58:05 PM
 * To change this template use File | Settings | File Templates.
 */

class ItemPropertiesFactory(definitionFileURL: URL) {
  private var itemProperties = new mutable.ListBuffer[ItemProperty]

  parseItemProperties

  private def parseItemProperties = {
    var document = new SAXReader().read(definitionFileURL)
    var rootElement = document.getRootElement
    var elements = JavaConversions.asBuffer(rootElement.elements("item_property").asInstanceOf[java.util.List[Element]])

    for (element <- elements) {
      var itemProperty = new ItemProperty

      itemProperty.typeName = element.elementText("type_name")
      itemProperty.name = element.elementText("name")
      itemProperty.propertyTypes = parsePropertyTypes(element.element("property_types"))
      itemProperty.model = parseModel(element.element("model"))
      itemProperty.presenterClassName = element.elementText("presenter").trim

      itemProperties += itemProperty
    }
  }

  private def parseModel(modelElement: Element) = modelElement match {
    case null => null
    case _ => modelElement.getText
  }

  private def parsePropertyTypes(propertyTypesElement: Element) = propertyTypesElement match {
    case null => new ListBuffer[String]
    case _ =>
      new ListBuffer[String] ++ JavaConversions.asBuffer(propertyTypesElement.elements("property_type").asInstanceOf[java.util.List[Element]]).map {
        e => e.getText
      }.toList
  }

  def itemPropertyTypeNames = itemProperties.map {
    i => i.typeName
  }

  def createItemProperty(typeName: String): ItemProperty = Marshal.load[ItemProperty](
    Marshal.dump(itemProperties.find(i => i.typeName.equals(typeName)).get))
}