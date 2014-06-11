
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 2/9/11
 * Time: 4:33 PM
 */
package sk.magiksoft.sodalis.core.ui

import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.utils.Utils
import swing.{Label, GridBagPanel}
import java.awt.Insets
import java.util.jar.JarFile
import swing.GridBagPanel.Anchor
import javax.swing.BorderFactory

class VersionDialog extends OkCancelDialog(SodalisApplication.get.getMainFrame, LocaleManager.getString("versionInfo")){
  initComponents

  private def initComponents = {
    var gridy = 0
    val mainPanel = new GridBagPanel {
      for (jarFile <- Utils.getModuleJarFiles) {
        add(new Label{
          text = jarFile.getName.dropRight(4)+": "
        }, new Constraints{
          grid = (0,gridy)
          insets = new Insets(3,3,0,0);
          anchor = Anchor.East
        })
        add(new Label{
          text = new JarFile(jarFile).getManifest.getMainAttributes.getValue("Version").toString
        }, new Constraints {
          grid = (1, gridy)
          insets = new Insets(3, 3, 0, 0);
          anchor = Anchor.West
        })
        gridy += 1
      }
      border = BorderFactory.createEmptyBorder(20,20,20,20)
    }
    setCloseDialog
    setMainPanel(mainPanel.peer)
    pack
    setLocationRelativeTo(null)
  }
}