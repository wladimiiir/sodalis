/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.psyche.rorschach.res

/*
 * Copyright (c) 2011
 */

import sk.magiksoft.sodalis.core.imex.ImExManager
import java.io.File
import sk.magiksoft.sodalis.psyche.rorschach.entity.{RorschachTable, Aperception}
import javax.imageio.ImageIO

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

    list.add(createRorschachTable("./-images/rorschach/rorschachTable01.jpg", 1))
    list.add(createRorschachTable("./-images/rorschach/rorschachTable02.jpg", 2))
    list.add(createRorschachTable("./-images/rorschach/rorschachTable03.jpg", 3))
    list.add(createRorschachTable("./-images/rorschach/rorschachTable04.jpg", 4))
    list.add(createRorschachTable("./-images/rorschach/rorschachTable05.jpg", 5))
    list.add(createRorschachTable("./-images/rorschach/rorschachTable06.jpg", 6))
    list.add(createRorschachTable("./-images/rorschach/rorschachTable07.jpg", 7))
    list.add(createRorschachTable("./-images/rorschach/rorschachTable08.jpg", 8))
    list.add(createRorschachTable("./-images/rorschach/rorschachTable09.jpg", 9))
    list.add(createRorschachTable("./-images/rorschach/rorschachTable10.jpg", 10))

    ImExManager.exportData(new File("rorschachTables.xml"), list)
  }

  private def createRorschachTable(imagePath: String, index:Int) = {
    val table = new RorschachTable
    table.image.setImage(ImageIO.read(new File(imagePath)))
    val bytes = table.image.getBytes
    table.image.setImage(null)
    table.image.setBytes(bytes)
    table.index = index
    table
  }
}