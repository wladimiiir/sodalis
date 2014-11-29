package sk.magiksoft.sodalis.core.ui

import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.utils.Utils
import swing.{Label, GridBagPanel}
import java.awt.Insets
import java.util.jar.JarFile
import swing.GridBagPanel.Anchor
import javax.swing.BorderFactory

/**
 * @author wladimiiir
 * @since 2011/2/9
 */
class VersionDialog extends OkCancelDialog(SodalisApplication.get.getMainFrame, LocaleManager.getString("versionInfo")) {
  initComponents

  private def initComponents = {
    var gridy = 0
    val mainPanel = new GridBagPanel {
      for (jarFile <- Utils.getModuleJarFiles) {
        add(new Label {
          text = jarFile.getName.dropRight(4) + ": "
        }, new Constraints {
          grid = (0, gridy)
          insets = new Insets(3, 3, 0, 0);
          anchor = Anchor.East
        })
        add(new Label {
          text = new JarFile(jarFile).getManifest.getMainAttributes.getValue("Version").toString
        }, new Constraints {
          grid = (1, gridy)
          insets = new Insets(3, 3, 0, 0);
          anchor = Anchor.West
        })
        gridy += 1
      }
      border = BorderFactory.createEmptyBorder(20, 20, 20, 20)
    }
    setCloseDialog
    setMainPanel(mainPanel.peer)
    pack
    setLocationRelativeTo(null)
  }
}
