package sk.magiksoft.sodalis.service.entity.property

import sk.magiksoft.sodalis.service.entity.Service
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslator

/**
 * @author wladimiiir
 * @since 2011/4/7
 */

class ServicePropertyTranslator extends EntityPropertyTranslator[Service] {
  def getTranslations = List(
    EntityTranslation("serviceName", s => Option(s.name)),
    EntityTranslation("serviceCode", s => Option(s.code)),
    EntityTranslation("serviceDescription", s => Option(s.description)),
    EntityTranslation("servicePrice", s => Option(s.price.formattedPrice()))
  )
}
