package sk.magiksoft.sodalis.core

import java.awt.Image
import java.text.MessageFormat
import java.util
import javax.imageio.ImageIO
import javax.swing.ImageIcon

import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.logger.LoggerManager
import sk.magiksoft.sodalis.core.splash.{AbstractSplashAction, SplashAction, SplashLoader}
import sk.magiksoft.sodalis.core.ui.ISOptionPane
import sk.magiksoft.sodalis.icon.IconManager
import scala.collection.JavaConversions._

/**
 * @author wladimiiir 
 * @since 2014/12/22
 */
class SodalisSplashLoader(application: SodalisApplication) extends SplashLoader {
  override def getTitle: String = "Sodalis"

  override def loaderFinished(): Unit = {
    application.showApplicationPanel()
    SodalisManager.serviceManager.applicationOpened()
  }

  override def loaderCancelled(e: Throwable): Unit = {
    e match {
      case e: Throwable => ISOptionPane.showMessageDialog(null, LocaleManager.getString("appStartError"))
      case _ => ISOptionPane.showMessageDialog(null, LocaleManager.getString("appStartCancelled"))
    }
    System.exit(1)
  }

  override def getIconImage: Image = IconManager.getInstance().getIcon("application").asInstanceOf[ImageIcon].getImage

  override def getSplashActions: util.List[SplashAction] = {
    val image = ImageIO.read(getClass.getResource("splash.png"))

    List(
      new AbstractSplashAction(image, LocaleManager.getString("StartingServices")) {
        override def run(): Unit = {
          SodalisManager.start(application)
        }
      },
      new AbstractSplashAction(image, LocaleManager.getString("InitializingMainFrame")) {
        override def run(): Unit = {
          application.initMainFrame()
        }
      }
    )
  }
}
