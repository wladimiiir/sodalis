/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.service

import data.ServiceDataManager
import entity.Service
import ui.ServiceContext

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 3/10/11
 * Time: 9:32 PM
 * To change this template use File | Settings | File Templates.
 */

object ServiceContextManager extends AbstractContextManager {
  def getDataManager = ServiceDataManager

  def getDefaultQuery = "from " + classOf[Service].getName

  def createContext = new ServiceContext

  def isFullTextActive = false
}