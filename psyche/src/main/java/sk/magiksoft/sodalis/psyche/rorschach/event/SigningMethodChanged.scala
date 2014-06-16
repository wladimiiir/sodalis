/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.event

import sk.magiksoft.sodalis.psyche.rorschach.entity.SigningMethod

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/22/11
 * Time: 7:50 AM
 * To change this template use File | Settings | File Templates.
 */

case class SigningMethodChanged(method: SigningMethod.Value) extends Event {

}