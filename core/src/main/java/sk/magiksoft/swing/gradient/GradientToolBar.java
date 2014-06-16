
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.swing.gradient;

import javax.swing.*;
import java.awt.*;

/**
 * @author wladimiiir
 */
public class GradientToolBar extends JToolBar {

    public GradientToolBar(String name, int orientation) {
        super(name, orientation);
    }

    public GradientToolBar(String name) {
        super(name);
    }

    public GradientToolBar(int orientation) {
        super(orientation);
    }

    public GradientToolBar() {
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setPaint(new LinearGradientPaint(0, 0, 0, getHeight(), new float[]{0.1f, 0.5f, 0.9f},
                new Color[]{getBackground(), getForeground(), getBackground()}));
        g2.fillRect(0, 0, getWidth(), getHeight());
        paintBorder(g);
        paintChildren(g);
    }
}