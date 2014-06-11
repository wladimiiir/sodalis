/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.event

import swing.event.Event
import sk.magiksoft.sodalis.psyche.rorschach.entity.TableSigning

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/19/11
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */

case class TableSigningChanged(signing:TableSigning) extends Event