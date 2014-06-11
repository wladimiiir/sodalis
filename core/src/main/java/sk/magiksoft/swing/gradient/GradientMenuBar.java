
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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import javax.swing.JMenuBar;
import sk.magiksoft.sodalis.core.factory.ColorList;

/**
 *
 * @author wladimiiir
 */
public class GradientMenuBar extends JMenuBar{

    public GradientMenuBar() {
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setPaint(new LinearGradientPaint(0, 0, 0, getHeight(), new float[]{0.1f,0.5f,0.9f},
                new Color[]{ColorList.LIGHT_BLUE, ColorList.SPLASH_FOREGROUND, ColorList.LIGHT_BLUE}));
        g2.fillRect(0, 0, getWidth(), getHeight());
        paintBorder(g);
        paintChildren(g);
    }
}