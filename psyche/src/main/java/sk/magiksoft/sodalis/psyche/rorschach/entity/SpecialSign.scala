/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.entity

import reflect.BeanProperty

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/18/11
 * Time: 8:33 PM
 * To change this template use File | Settings | File Templates.
 */

class SpecialSign extends Signing {
  @BeanProperty var category = ""
  @BeanProperty var qualitySign = false
}