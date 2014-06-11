package sk.magiksoft.sodalis

import java.util.Properties
import scala.collection.JavaConversions._
import javax.swing.UIManager
import scala.io.{BufferedSource, Source}
import java.io.File
import java.awt.{GraphicsEnvironment, Font}

/**
 * User: wladimiiir
 * Date: 5/1/14
 * Time: 10:14 PM
 */
object Environment {
  def init {
    setupUIManager
    setupFonts
  }


  def setupUIManager {
    val propertiesStream = getClass.getResourceAsStream("environment.properties")
    if (propertiesStream != null) {
      val properties = new Properties()
      properties.load(propertiesStream)

      properties.entrySet().foreach {
        e => UIManager.put(e.getKey, e.getValue)
      }
    }
  }

  private def setupFonts {
    val fontsDir = new File("data/fonts")
    if (fontsDir.isDirectory) {
      for (fontFile <- fontsDir.listFiles()) {
        val font = Font.createFont(Font.TRUETYPE_FONT, fontFile)

        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font)
        UIManager.put(fontFile.getName(), font)
      }
    }
  }
}
