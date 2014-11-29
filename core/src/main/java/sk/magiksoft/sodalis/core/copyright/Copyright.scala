package sk.magiksoft.sodalis.core.copyright

import io.Source
import java.io.{FileWriter, File}
import java.lang.String

/**
 * @author wladimiiir
 * @since 2011/3/7
 */

object Copyright {
  private val Copyright =
    """
      |/***********************************************\
      |*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
      |*  Sodalis 2007-2011                            *
      |*  http://www.sodalis.sk                        *
      |\***********************************************/

    """.stripMargin

  def addCopyright(files: List[File], outputDir: File) {
    for (file <- files) {
      val outFile = new File(outputDir.getPath + "/" + file.getPath)
      outFile.getParentFile.mkdirs
      val writer = new FileWriter(outFile)
      println("Processing file: " + file.getPath)
      writer.write(Copyright)
      Source.fromFile(file).getLines.foreach {
        line => writer.write("\n" + line)
      }
      writer.close
    }
  }

  def main(args: Array[String]) {
    addCopyright(collectFiles(new File(".")), new File("output"))
  }

  private def collectFiles(file: File): List[File] = {
    if (file.isFile) {
      if (file.getName.endsWith(".java") || file.getName.endsWith(".scala")) List(file) else Nil
    } else {
      file.listFiles.map {
        collectFiles(_)
      }.toList.flatten
    }
  }
}
