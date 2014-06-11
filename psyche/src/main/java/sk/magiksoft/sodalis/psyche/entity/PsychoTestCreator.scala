/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.entity

import sk.magiksoft.sodalis.person.entity.Person

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 6/24/11
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */

trait PsychoTestCreator {
  def createPsychoTest(generalPsychoTest:PsychoTest): Option[PsychoTest]

  def getPsychoTestName:String
}