package sk.magiksoft.sodalis.item.factory

import java.net.URL
import sk.magiksoft.sodalis.item.entity.ItemProperty
import org.dom4j.io.SAXReader
import scala.collection.JavaConversions
import org.dom4j.Element
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

/**
 * @author wladimiiir
 * @since 2010/6/10
 */

class ItemPropertiesFactory(definitionFileURL: URL) {
  private var itemProperties = new ListBuffer[ItemProperty]

  parseItemProperties

  private def parseItemProperties = {
    val document = new SAXReader().read(definitionFileURL)
    val rootElement = document.getRootElement
    val elements = JavaConversions.asScalaBuffer(rootElement.elements("item_property").asInstanceOf[java.util.List[Element]])

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
      new ListBuffer[String] ++ propertyTypesElement.elements("property_type").asInstanceOf[java.util.List[Element]].map {
        e => e.getText
      }.toList
  }

  def itemPropertyTypeNames = itemProperties.map {
    i => i.typeName
  }

  def createItemProperty(typeName: String): ItemProperty = itemProperties.find(i => i.typeName.equals(typeName)).getOrElse(new ItemProperty)
}
