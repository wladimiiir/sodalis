/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.res

/*
 * Copyright (c) 2011
 */

import java.io.File
import sk.magiksoft.sodalis.psyche.rorschach.entity.RorschachTable
import javax.imageio.ImageIO
import sk.magiksoft.sodalis.core.imex.ImExManager

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 5/13/11
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */

object RorschachTablesToXML {

  def main(args: Array[String]) {
    exportRorschachTables()
  }

  private def exportRorschachTables() {
    val list = new java.util.LinkedList[RorschachTable]

    list.add(createRorschachTable("D:/temp/rorschach/File-Rorschach_blot_01.jpg", 1))
    list.add(createRorschachTable("D:/temp/rorschach/File-Rorschach_blot_02.jpg", 2))
    list.add(createRorschachTable("D:/temp/rorschach/File-Rorschach_blot_03.jpg", 3))
    list.add(createRorschachTable("D:/temp/rorschach/File-Rorschach_blot_04.jpg", 4))
    list.add(createRorschachTable("D:/temp/rorschach/File-Rorschach_blot_05.jpg", 5))
    list.add(createRorschachTable("D:/temp/rorschach/File-Rorschach_blot_06.jpg", 6))
    list.add(createRorschachTable("D:/temp/rorschach/File-Rorschach_blot_07.jpg", 7))
    list.add(createRorschachTable("D:/temp/rorschach/File-Rorschach_blot_08.jpg", 8))
    list.add(createRorschachTable("D:/temp/rorschach/File-Rorschach_blot_09.jpg", 9))
    list.add(createRorschachTable("D:/temp/rorschach/File-Rorschach_blot_10.jpg", 10))

    ImExManager.exportData(new File("rorschach_blots.xml"), list)
  }

  private def createRorschachTable(imagePath: String, index: Int) = {
    val table = new RorschachTable
    table.image.setImage(ImageIO.read(new File(imagePath)))
    val bytes = table.image.getBytes
    table.image.setImage(null)
    table.image.setBytes(bytes)
    table.index = index
    table
  }
}