/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.ftpman.action


/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/6/11
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */

case class FileScanned(fileName: String, host: String, path: String, fileSize: Long) extends Event