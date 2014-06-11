
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sk.magiksoft.swing;

import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author wladimiiir
 */
public class BufferedJPanel extends JPanel{

    public BufferedJPanel() {
        super(true);
    }

    public BufferedJPanel(LayoutManager layout) {
        super(layout, true);
    }

    @Override
    public void paint(Graphics g) {
        BufferedImage image=new BufferedImage(g.getClipBounds().width, g.getClipBounds().height, BufferedImage.TYPE_INT_RGB);
        Graphics imageG = image.getGraphics();
        super.paint(imageG);
        g.drawImage(image, 0, 0, null);
    }
}