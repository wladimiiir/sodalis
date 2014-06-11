/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche

import action.AddPsychoTestAction
import data.PsycheDataManager
import rorschach.entity.RorschachTestCreator
import sk.magiksoft.sodalis.core.context.AbstractContextManager
import ui.PsychoTestContext

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/13/11
 * Time: 11:16 AM
 * To change this template use File | Settings | File Templates.
 */

object PsycheContextManager extends AbstractContextManager {

  PsycheDataManager.registerPsychoTestCreator(new RorschachTestCreator)

  def getDataManager = PsycheDataManager

  def getDefaultQuery = "from PsychoTest"

  def createContext() = new PsychoTestContext


  def isFullTextActive = false
}