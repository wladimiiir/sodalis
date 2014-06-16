/*
 * Copyright (c) 2011
 */

package sk.magiksoft.swing

import org.jdesktop.jxlayer.plaf.ext.LockableUI


/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 6/24/11
 * Time: 9:28 AM
 * To change this template use File | Settings | File Templates.
 */

class DirtyLockable extends LockableUI {
  override def setDirty(dirty: Boolean) {
    super.setDirty(dirty)
  }
}