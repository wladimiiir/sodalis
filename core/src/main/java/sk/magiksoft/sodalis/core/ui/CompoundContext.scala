
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 2/15/11
 * Time: 5:05 PM
 */
package sk.magiksoft.sodalis.core.ui

import sk.magiksoft.sodalis.core.context.Context
import sk.magiksoft.sodalis.core.entity.{Entity, DatabaseEntity}
import collection.mutable.ListBuffer
import java.awt.BorderLayout
import swing.TabbedPane.Page
import collection.JavaConversions._
import swing.{Component, TabbedPane}
import java.util.{ArrayList, Collections, List}

class CompoundContext(clazz: Class[_ <: DatabaseEntity]) extends AbstractContext(clazz){
  val contexts = new ListBuffer[AbstractContext]
  val tabbedPane = new TabbedPane

  initComponents

  private def initComponents = {
    setLayout(new BorderLayout)
    add(tabbedPane.peer, BorderLayout.CENTER)
  }

  def entitiesRemoved(entities: List[_ <: DatabaseEntity]) = for (context <- contexts) {
    context.entitiesAdded(entities)
  }

  def entitiesUpdated(entities: List[_ <: DatabaseEntity]) = for (context <- contexts) {
    context.entitiesUpdated(entities)
  }

  def entitiesAdded(entities: List[_ <: DatabaseEntity]) = for (context <- contexts) {
    context.entitiesAdded(entities)
  }

  def removeAllRecords = for (context <- contexts) {
    context.removeAllRecords
  }

  def getEntities:List[_ <: Entity] = currentContext match {
    case Some(context) => context.getEntities
    case None => new ArrayList[Entity](0)
  }

  def getSelectedEntities: List[_ <: Entity] = currentContext match {
    case Some(context) => context.getSelectedEntities
    case None => new ArrayList[Entity](0)
  }


  def setSelectedEntities(entities: List[_ <: Entity]) = for (context <- contexts) {
    context.setSelectedEntities(entities)
  }

  def canChangeEntity = contexts.find(!_.canChangeEntity).isEmpty

  def currentContext = tabbedPane.pages.size match {
    case 0 => None
    case _ => Option(tabbedPane.selection.page.content.asInstanceOf[Context])
  }

  def addContext(title:String, context:AbstractContext) = {
    contexts += context
    tabbedPane.pages += new Page(title, Component.wrap(context))
  }
}