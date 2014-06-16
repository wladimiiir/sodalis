
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.sodalis.core.ui.controlpanel;

import sk.magiksoft.sodalis.core.data.DataListener;

import javax.swing.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public interface ControlPanel extends DataListener {

    void cancelEditing();

    boolean isEditing();

    boolean doUpdate();

    void setupControlPanel(Object object);

    void setAdditionalObjects(List<Object> objects);

    JComponent getControlComponent();

    void setSelectedInfoPanelClass(Class<? extends InfoPanel> infoPanelClass);

    Class<? extends InfoPanel> getSelectedInfoPanelClass();
}