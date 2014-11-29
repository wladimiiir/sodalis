package sk.magiksoft.sodalis.psyche.rorschach.res

/*
 * Copyright (c) 2011
 */

import java.io.File
import sk.magiksoft.sodalis.psyche.rorschach.entity.OriginalAnswer
import sk.magiksoft.sodalis.core.imex.ImExManager

/**
 * @author wladimiiir
 * @since 2011/5/13
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

  private def createOriginalAnswer(blotIndex: Int, name: String, interpretation: String) = {
    val answer = new OriginalAnswer
    answer.blotIndex = blotIndex
    answer.name = name
    answer.interpretation = interpretation
    answer
  }
}
