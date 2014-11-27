/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.data

import sk.magiksoft.sodalis.psyche.rorschach.entity._
import java.io.File
import sk.magiksoft.sodalis.psyche.entity.PsychoTestCreator
import sk.magiksoft.sodalis.core.data.remote.client.ClientDataManager
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions
import sk.magiksoft.sodalis.core.imex.ImExManager
import scala.collection.JavaConversions._

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/13/11
 * Time: 9:41 AM
 * To change this template use File | Settings | File Templates.
 */

object PsycheDataManager extends ClientDataManager {
  lazy val basicTAEntries: List[BasicTAEntry] = loadBasicTAEntries()
  private val psychoTestCreators = new ListBuffer[PsychoTestCreator]

  def getPsychoTestCreators = psychoTestCreators.toList

  private def loadBasicTAEntries() =
    JavaConversions.asScalaBuffer(ImExManager.importFile(new File("data/xml/basicTAEntries.xml")))
      .map(_.asInstanceOf[BasicTAEntry]).toList

  def getAperceptions: List[Aperception] = getDatabaseEntities(classOf[Aperception]).toList

  def getDeterminants: List[Determinant] = getDatabaseEntities(classOf[Determinant]).toList

  def getContents: List[Content] = getDatabaseEntities(classOf[Content]).toList

  def getRorschachBlots = getDatabaseEntities(classOf[RorschachBlot]).toList

  def getVulgarAnswers = getDatabaseEntities(classOf[VulgarAnswer]).toList

  def getOriginalAnswers = getDatabaseEntities(classOf[OriginalAnswer]).toList

  def getSpecialSigns = getDatabaseEntities(classOf[SpecialSign]).toList

  def registerPsychoTestCreator(creator: PsychoTestCreator) {
    psychoTestCreators += creator
  }
}