/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.event

import sk.magiksoft.sodalis.psyche.rorschach.entity.TableAnswer

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/19/11
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */

case class TableAnswerChanged(answer: Option[TableAnswer]) extends Event