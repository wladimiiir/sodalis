
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

import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.sodalis.core.locale.LocaleManager;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;

/**
 * @author wladimiiir
 */
public class HideableSplitPane extends JSplitPane {

    public static final int STATE_EXPANDED = 0;
    public static final int STATE_LEFT_COLLAPSED = 1;
    public static final int STATE_RIGHT_COLLAPSED = 2;
    private boolean leftVisible = true;
    private boolean rightVisible = true;
    private JPanel leftPanel = new JPanel(new BorderLayout(), true);
    private JPanel rightPanel = new JPanel(new BorderLayout(), true);
    private Component thisLeftComponent = null;
    private Component thisRightComponent = null;
    private HideButton leftButton;
    private HideButton rightButton;
    private int state = STATE_EXPANDED;
    private boolean initialized = false;
    private boolean adjusting = false;

    public HideableSplitPane() {
        initialized = true;
        setupLayout();
    }

    public HideableSplitPane(int newOrientation, boolean newContinuousLayout, Component newLeftComponent, Component newRightComponent) {
        super(newOrientation, newContinuousLayout, newLeftComponent, newRightComponent);
        initialized = true;
        setupLayout();
    }

    public HideableSplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent) {
        super(newOrientation, newLeftComponent, newRightComponent);
        initialized = true;
        setupLayout();
    }

    public HideableSplitPane(int newOrientation, boolean newContinuousLayout) {
        super(newOrientation, newContinuousLayout);
        initialized = true;
        setupLayout();
    }

    public HideableSplitPane(int newOrientation) {
        super(newOrientation);
        initialized = true;
        setupLayout();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        switch (state) {
            case STATE_EXPANDED:
                leftVisible = true;
                leftButton.setHidden(false);
                rightVisible = true;
                rightButton.setHidden(false);
                break;
            case STATE_LEFT_COLLAPSED:
                leftVisible = false;
                leftButton.setHidden(true);
                rightVisible = true;
                rightButton.setHidden(false);
                adjusting = true;
                setupForLeft();
                adjusting = false;
                break;
            case STATE_RIGHT_COLLAPSED:
                leftVisible = true;
                leftButton.setHidden(false);
                rightVisible = false;
                rightButton.setHidden(true);
                adjusting = true;
                setupForRight();
                adjusting = false;
                break;
        }
    }

    private void dividerChanged(PropertyChangeEvent e) {
        if (adjusting) {
            return;
        }
        System.out.println(e.getSource());
        leftVisible = true;
        leftButton.setHidden(false);
        rightVisible = true;
        rightButton.setHidden(false);
    }

    private void leftButtonAction() {
        adjusting = true;
        leftVisible = !leftVisible;
        state = leftVisible ? STATE_EXPANDED : STATE_LEFT_COLLAPSED;
        leftButton.setHidden(!leftVisible);
        setupForLeft();
        adjusting = false;
    }

    private void setupForLeft() {
        if (!leftVisible) {
            if (orientation == JSplitPane.VERTICAL_SPLIT) {
                setDividerLocation(leftButton.getPreferredSize().height);
            } else {
                setDividerLocation(leftButton.getPreferredSize().width);
            }
            rightVisible = true;
            rightButton.setHidden(false);
        } else {
            if (orientation == JSplitPane.VERTICAL_SPLIT) {
                setDividerLocation(lastDividerLocation == getHeight() - getDividerSize() - rightButton.getPreferredSize().height - 10 ? getHeight() / 2 : lastDividerLocation);
            } else {
                setDividerLocation(lastDividerLocation == getWidth() - getDividerSize() - rightButton.getPreferredSize().width - 10 ? getWidth() / 2 : lastDividerLocation);
            }
        }
    }

    private void rightButtonAction() {
        adjusting = true;
        rightVisible = !rightVisible;
        state = rightVisible ? STATE_EXPANDED : STATE_RIGHT_COLLAPSED;
        rightButton.setHidden(!rightVisible);
        setupForRight();
        adjusting = false;

    }

    private void setupForRight() {
        if (!rightVisible) {
            if (orientation == JSplitPane.VERTICAL_SPLIT) {
                setDividerLocation(getHeight() - getDividerSize() - rightButton.getPreferredSize().height);
            } else {
                setDividerLocation(getWidth() - getDividerSize() - rightButton.getPreferredSize().width);
            }
            leftVisible = true;
            leftButton.setHidden(false);
        } else {
            if (orientation == JSplitPane.VERTICAL_SPLIT) {
                setDividerLocation(lastDividerLocation == leftButton.getPreferredSize().height + 10 ? getHeight() / 2 : lastDividerLocation);
            } else {
                setDividerLocation(lastDividerLocation == leftButton.getPreferredSize().width + 10 ? getWidth() / 2 : lastDividerLocation);
            }
        }
    }

    private void setupLayout() {
        BasicSplitPaneDivider divider = ((BasicSplitPaneUI) getUI()).getDivider();

        leftButton = new HideButton(JSplitPane.LEFT);
        rightButton = new HideButton(JSplitPane.RIGHT);

        divider.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                setState(STATE_EXPANDED);
            }
        });

        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                setState(getState());
            }
        });
//        addPropertyChangeListener(DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
//
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                dividerChanged(evt);
//            }
//        });

        leftButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                leftButtonAction();
            }
        });

        rightButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                rightButtonAction();
            }
        });

        if (orientation == JSplitPane.VERTICAL_SPLIT) {
            leftPanel.add(leftButton, BorderLayout.NORTH);
            rightPanel.add(rightButton, BorderLayout.SOUTH);
        } else {
            leftPanel.add(leftButton, BorderLayout.WEST);
            rightPanel.add(rightButton, BorderLayout.EAST);
        }
        leftPanel.setOpaque(false);
        rightPanel.setOpaque(false);

        Component leftComp = leftComponent;
        Component rightComp = rightComponent;

        super.setLeftComponent(leftPanel);
        super.setRightComponent(rightPanel);
        setLeftComponent(leftComp);
        setRightComponent(rightComp);
    }

    @Override
    public void setLeftComponent(Component comp) {
        if (!initialized) {
            super.setLeftComponent(comp);
            return;
        }

        if (comp == null) {
            if (thisLeftComponent != null) {
                leftPanel.remove(thisLeftComponent);
                thisLeftComponent = null;
            }
        } else {
            leftPanel.add(comp, BorderLayout.CENTER);
            thisLeftComponent = comp;
        }
    }

    @Override
    public void setRightComponent(Component comp) {
        if (!initialized) {
            super.setRightComponent(comp);
            return;
        }

        if (comp == null) {
            if (thisRightComponent != null) {
                rightPanel.remove(thisRightComponent);
                thisRightComponent = null;
            }
        } else {
            rightPanel.add(comp, BorderLayout.CENTER);
            thisRightComponent = comp;
        }
    }

    @Override
    public Component getLeftComponent() {
        return thisLeftComponent;
    }

    @Override
    public Component getRightComponent() {
        return thisRightComponent;
    }

    public boolean isLeftVisible() {
        return leftVisible;
    }

    public boolean isRightVisible() {
        return rightVisible;
    }

    public void setLeftText(String text) {
        leftButton.setText(text);
    }

    public void setRightText(String text) {
        rightButton.setText(text);
    }

    @Override
    public void repaint() {
        super.repaint();
        if (leftButton != null) {
            leftButton.repaint();
        }
        if (rightButton != null) {
            rightButton.repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (leftButton != null) {
            leftButton.repaint();
        }
        if (rightButton != null) {
            rightButton.repaint();
        }
    }

    private class HideButton extends JButton {

        private String position;
        private boolean hidden = false;

        public HideButton(String position) {
            this.position = position;
            setPreferredSize(new Dimension(10, 10));
        }

        private void setHidden(boolean hidden) {
            this.hidden = hidden;
            if (hidden) {
                setPreferredSize(new Dimension(20, 20));
                setToolTipText(LocaleManager.getString("openPanel"));
            } else {
                setPreferredSize(new Dimension(10, 10));
                setToolTipText(LocaleManager.getString("hidePanel"));
            }
            revalidate();
            repaint();
        }

        @Override
        public void paint(Graphics g) {
            int[] x;
            int[] y;
            Graphics2D g2 = (Graphics2D) g;

            if (hidden) {
                g2.setPaint(new LinearGradientPaint(getBounds().x, getBounds().y, getBounds().width, getBounds().height, new float[]{0f, 0.5f, 1f},
                        new Color[]{ColorList.LIGHT_BLUE, ColorList.SPLASH_FOREGROUND, ColorList.LIGHT_BLUE}));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(getForeground());
                if (getOrientation() == JSplitPane.VERTICAL_SPLIT) {
                    g.drawString(getText(), 18, 15);
                    x = new int[]{2, 8, 14};
                    if (position.equals(JSplitPane.LEFT)) {
                        y = new int[]{8, 14, 8};
                    } else {
                        y = new int[]{14, 8, 14};
                    }
                } else {
                    x = new int[]{2, 6, 10};
                    if (position.equals(JSplitPane.LEFT)) {
                        y = new int[]{2, 8, 2};
                    } else {
                        y = new int[]{8, 2, 8};
                    }
                }
            } else {
                g.setColor(this.getBackground());
                g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g.setColor(this.getForeground());
                if (orientation == JSplitPane.VERTICAL_SPLIT) {
                    x = new int[]{getWidth() / 2 - 6, getWidth() / 2, getWidth() / 2 + 6};
                    if (position.equals(JSplitPane.RIGHT)) {
                        y = new int[]{2, 8, 2};
                    } else {
                        y = new int[]{8, 2, 8};
                    }
                } else {
                    if (position.equals(JSplitPane.RIGHT)) {
                        x = new int[]{2, 8, 2};
                    } else {
                        x = new int[]{8, 2, 8};
                    }
                    y = new int[]{getHeight() / 2 - 6, getHeight() / 2, getHeight() / 2 + 6};
                }
            }

            g.fillPolygon(x, y, x.length);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
//        JPanel panel=new JPanel(new BorderLayout());
//        panel.add(new JButton("sdfd"), BorderLayout.NORTH);
//        panel.add(new JButton("dsdsfsd"),  BorderLayout.CENTER);
//        frame.getContentPane().add(panel);
        frame.add(new HideableSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JList(new Object[]{"skuska", "text"}), new JButton("sdfds")));
        frame.setVisible(true);
    }
}