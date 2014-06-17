/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.service.entity.property

import sk.magiksoft.sodalis.service.entity.Service
import sk.magiksoft.sodalis.core.entity.property.EntityPropertyTranslator

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 4/7/11
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */

class ServicePropertyTranslator extends EntityPropertyTranslator[Service] {
  def getTranslations = List(
    EntityTranslation("serviceName", s => Option(s.name)),
    EntityTranslation("serviceCode", s => Option(s.code)),
    EntityTranslation("serviceDescription", s => Option(s.description)),
    EntityTranslation("servicePrice", s => Option(s.price.formattedPrice()))
  )
}