
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.module;

import javax.swing.ImageIcon;

/**
 *
 * @author wladimiiir
 */
public class ModuleDescriptor {

    private ImageIcon icon;
    private String description;

    public ModuleDescriptor(ImageIcon icon, String description) {
        this.icon = icon;
        if (icon != null) {
            this.icon.setDescription(description);
        }else{
            this.description=description;
        }
    }

    public String getDescription() {
        return icon==null ? description : icon.getDescription();
    }

    public ImageIcon getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return icon==null ? description : icon.toString();
    }
}