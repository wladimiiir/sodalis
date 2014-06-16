/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.entity

import sk.magiksoft.sodalis.core.entity.AbstractDatabaseEntity
import java.util.{List => jList}

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/18/11
 * Time: 11:06 PM
 * To change this template use File | Settings | File Templates.
 */

class TestResult extends AbstractDatabaseEntity {
  var tableSignings = new ListBuffer[TableSigning]

  def getTableSignings = bufferAsJavaList(tableSignings)

  def setTableSignings(jTableSignings: jList[TableSigning]) {
    tableSignings = new ListBuffer[TableSigning]
    tableSignings ++= jTableSignings
  }

  def totalAnswerCount = tableSignings.foldLeft(0) {
    (count, signing) => count + signing.answers.size
  }

  def findAnswers[A](folding: (TableAnswer => List[A])): List[A] = tableSignings.foldLeft(new ListBuffer[A]) {
    (buffer, signing) => signing.answers.foldLeft(buffer) {
      (buffer, answer) => buffer ++= folding(answer)
    }
  }.toList

  def updateFrom(entity: DatabaseEntity) {
    entity match {
      case result: TestResult if result ne this => {
        tableSignings = result.tableSignings
      }
      case _ =>
    }
  }
}