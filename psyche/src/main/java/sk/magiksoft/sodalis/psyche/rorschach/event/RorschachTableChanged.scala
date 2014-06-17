/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.event

import sk.magiksoft.sodalis.psyche.rorschach.entity.RorschachTable
import scala.swing.event.Event

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/17/11
 * Time: 6:57 PM
 * To change this template use File | Settings | File Templates.
 */

case class RorschachTableChanged(table: RorschachTable) extends Event {

}