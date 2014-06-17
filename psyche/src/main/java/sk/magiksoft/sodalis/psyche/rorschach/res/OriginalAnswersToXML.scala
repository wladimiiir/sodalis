/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.res

/*
 * Copyright (c) 2011
 */

import java.io.File
import sk.magiksoft.sodalis.psyche.rorschach.entity.OriginalAnswer
import sk.magiksoft.sodalis.core.imex.ImExManager

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/13/11
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */

object OriginalAnswersToXML {

  def main(args: Array[String]) {
    exportOriginalAnswers()
  }

  private def exportOriginalAnswers() {
    val list = new java.util.LinkedList[OriginalAnswer]()

    list.add(createOriginalAnswer(1, "O", ""))
    list.add(createOriginalAnswer(2, "O", ""))
    list.add(createOriginalAnswer(3, "O", ""))
    list.add(createOriginalAnswer(4, "O", ""))
    list.add(createOriginalAnswer(5, "O", ""))
    list.add(createOriginalAnswer(6, "O", ""))
    list.add(createOriginalAnswer(7, "O", ""))
    list.add(createOriginalAnswer(8, "O", ""))
    list.add(createOriginalAnswer(9, "O", ""))
    list.add(createOriginalAnswer(10, "O", ""))

    ImExManager.exportData(new File("originalAnswers.xml"), list)
  }

  private def createOriginalAnswer(tableIndex: Int, name: String, interpretation: String) = {
    val answer = new OriginalAnswer
    answer.tableIndex = tableIndex
    answer.name = name
    answer.interpretation = interpretation
    answer
  }
}