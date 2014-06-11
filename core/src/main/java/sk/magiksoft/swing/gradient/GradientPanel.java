
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
import java.awt.LayoutManager;
import java.awt.LinearGradientPaint;
import javax.swing.JPanel;
import sk.magiksoft.sodalis.core.factory.ColorList;

/**
 *
 * @author wladimiiir
 */
public class GradientPanel extends JPanel {

    public GradientPanel() {
    }

    public GradientPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public GradientPanel(LayoutManager layout) {
        super(layout);
    }

    public GradientPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }


    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setPaint(new LinearGradientPaint(getBounds().x, getBounds().y, getBounds().width, getBounds().height, new float[]{0f,0.5f,1f},
                new Color[]{ColorList.LIGHT_BLUE, ColorList.SPLASH_FOREGROUND, ColorList.LIGHT_BLUE}));
        g2.fillRect(0, 0, getWidth(), getHeight());
        paintBorder(g);
        paintChildren(g);
    }
}