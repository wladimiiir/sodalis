/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.ftpman.action

import swing.event.Event

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/8/11
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */

case class FilePartRetrieved(retrievedSize: Long, totalSize: Long) extends Event