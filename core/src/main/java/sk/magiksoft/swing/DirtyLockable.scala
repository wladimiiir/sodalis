package sk.magiksoft.swing

import org.jdesktop.jxlayer.plaf.ext.LockableUI


/**
 * @author wladimiiir
 * @since 2011/6/24
 */

class DirtyLockable extends LockableUI {
  override def setDirty(dirty: Boolean) {
    super.setDirty(dirty)
  }
}
