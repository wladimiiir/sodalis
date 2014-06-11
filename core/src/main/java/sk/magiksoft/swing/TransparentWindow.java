
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

/**
 *
 * @author wladimiiir
 */
import java.awt.*;
import java.awt.event.*;
import java.util.TimerTask;
import javax.swing.*;

public class TransparentWindow extends JWindow implements MouseMotionListener, FocusListener {

    Graphics tempImgGraphics;
    Image backgroundImage, temporaryImage;
    Point mousePointer;

    public TransparentWindow() {
        init();
        setVisible(true);
    }

    public void init() {
        addMouseMotionListener(this);
        setBounds(170, 170, 100, 100);
        capture();
        addFocusListener(this);
        java.util.Timer timer= new java.util.Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                capture();
            }
        }, 0, 500);
    }

    public void focusGained(FocusEvent aFocusEvent) {
        Point aPoint = getLocation();
        setLocation(15000, 0);
        capture();
        setLocation(aPoint);
    }

    public void focusLost(FocusEvent aFocusEvent) {
    }

    public void capture() {
        setVisible(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        try {
            Robot aRobot = new Robot();
            Rectangle rect = new Rectangle(0, 0, dim.width, dim.height);
            backgroundImage = aRobot.createScreenCapture(rect);
        } catch (AWTException awte) {
            System.out.println("robot excepton occurred");
        }
        setVisible(true);
    }

    public void mouseDragged(MouseEvent aMouseEvent) {
        Point aPoint = aMouseEvent.getPoint();
        int x = getX() + aPoint.x - mousePointer.x;
        int y = getY() + aPoint.y - mousePointer.y;
        setLocation(x, y);
        Graphics graphics = getGraphics();
        paint(graphics);
    }

    public void mouseMoved(MouseEvent aMouseEvent) {
        mousePointer = aMouseEvent.getPoint();
    }

    public void paint(Graphics graphics) {
        if (temporaryImage == null) {
            temporaryImage = createImage(getWidth(), getHeight());
            tempImgGraphics = temporaryImage.getGraphics();
        }
        tempImgGraphics.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(),
                getX(), getY(), getX() + getWidth(), getY() + getHeight(), null);
        tempImgGraphics.setColor(Color.red);
        tempImgGraphics.drawRect(10, 20, 70, 70);
        tempImgGraphics.setColor(Color.red);
        tempImgGraphics.drawString("Sushil", 30, 30);
        graphics.drawImage(temporaryImage, 0, 0, null);
    }

    public static void main(String[] args) {
        new TransparentWindow();
    }
}