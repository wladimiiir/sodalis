package sk.magiksoft.sodalis.psyche.rorschach.res

/*
 * Copyright (c) 2011
 */

import java.io.File
import sk.magiksoft.sodalis.psyche.rorschach.entity.RorschachBlot
import javax.imageio.ImageIO
import sk.magiksoft.sodalis.core.imex.ImExManager

/**
 * @author wladimiiir
 * @since 2011/5/13
 */

object RorschachBlotsToXML {

  def main(args: Array[String]) {
    exportRorschachBlots()
  }

  private def exportRorschachBlots() {
    val list = new java.util.LinkedList[RorschachBlot]

    list.add(createRorschachBlot("D:/temp/rorschach/File-Rorschach_blot_01.jpg", 1))
    list.add(createRorschachBlot("D:/temp/rorschach/File-Rorschach_blot_02.jpg", 2))
    list.add(createRorschachBlot("D:/temp/rorschach/File-Rorschach_blot_03.jpg", 3))
    list.add(createRorschachBlot("D:/temp/rorschach/File-Rorschach_blot_04.jpg", 4))
    list.add(createRorschachBlot("D:/temp/rorschach/File-Rorschach_blot_05.jpg", 5))
    list.add(createRorschachBlot("D:/temp/rorschach/File-Rorschach_blot_06.jpg", 6))
    list.add(createRorschachBlot("D:/temp/rorschach/File-Rorschach_blot_07.jpg", 7))
    list.add(createRorschachBlot("D:/temp/rorschach/File-Rorschach_blot_08.jpg", 8))
    list.add(createRorschachBlot("D:/temp/rorschach/File-Rorschach_blot_09.jpg", 9))
    list.add(createRorschachBlot("D:/temp/rorschach/File-Rorschach_blot_10.jpg", 10))

    ImExManager.exportData(new File("rorschach_blots.xml"), list)
  }

  private def createRorschachBlot(imagePath: String, index: Int) = {
    val blot = new RorschachBlot
    blot.image.setImage(ImageIO.read(new File(imagePath)))
    val bytes = blot.image.getBytes
    blot.image.setImage(null)
    blot.image.setBytes(bytes)
    blot.index = index
    blot
  }
}
