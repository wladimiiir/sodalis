
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.ui;

import sk.magiksoft.sodalis.core.factory.ColorList;
import sk.magiksoft.sodalis.core.module.Module;
import sk.magiksoft.sodalis.core.module.ModuleDescriptor;
import sk.magiksoft.sodalis.core.utils.UIUtils;
import sk.magiksoft.swing.SliderPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author wladimiiir
 */
public class ApplicationPanel extends JLayeredPane {

    public static final String WELCOME_PANEL_KEY = "WELCOME_PANEL";
    private JPanel mainPanel;
    private JList moduleChooser;
    private JPanel modulePanel;
    private ModuleChooserPanel moduleChooserPanel;
    private CardLayout moduleCardLayout;
    private Class<? extends Module> currentModuleClass;
    private SliderPanel leftSlidingPanel;
    private JPanel rightPanel;
    private JPanel statusPanel;
    private JComponent welcomePage;

    public ApplicationPanel() {
        initComponents();
    }

    private void initComponents() {
        final SpringLayout layout = new SpringLayout();

        mainPanel = new JPanel(new BorderLayout(), true);
        moduleChooserPanel = new ModuleChooserPanel();
        statusPanel = new JPanel(new BorderLayout());
        moduleChooser = new JList(new DefaultListModel());
        moduleChooser.setFocusable(false);

        setLayout(layout);

        initLeftSlidingPanel();
        initRightPanel();
        initModulePanel();
        initModuleChooser();
        initMainPanel();

        add(leftSlidingPanel, PALETTE_LAYER);
        add(mainPanel, DEFAULT_LAYER);

//        setComponentZOrder(leftSlidingPanel, 0);
        layout.putConstraint(SpringLayout.WEST, mainPanel, 25, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, mainPanel, 0, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.SOUTH, mainPanel, 0, SpringLayout.SOUTH, this);
        layout.putConstraint(SpringLayout.EAST, mainPanel, 0, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.WEST, leftSlidingPanel, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, leftSlidingPanel, 0, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.SOUTH, leftSlidingPanel, 0, SpringLayout.SOUTH, this);
    }

    private void initLeftSlidingPanel() {
        final JPanel panel = new JPanel(new BorderLayout());

        panel.add(moduleChooserPanel, BorderLayout.CENTER);
        leftSlidingPanel = new SliderPanel(panel, SliderPanel.POSITION_LEFT, 270);
//        leftSlidingPanel.setTitle("Kontexty");
        leftSlidingPanel.setBorder(new LineBorder(Color.BLACK));
        leftSlidingPanel.setFirstColor(ColorList.LIGHT_BLUE);
        leftSlidingPanel.setSecondColor(ColorList.SPLASH_FOREGROUND);
    }

    private void initMainPanel() {
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        mainPanel.add(modulePanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
    }

    private void initModulePanel() {
        try {
            welcomePage = UIUtils.makeGradientComponent(new WelcomePanel(), ColorList.LIGHT_BLUE,
                    ColorList.SPLASH_FOREGROUND);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ApplicationPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        moduleCardLayout = new CardLayout();
        modulePanel = new JPanel(moduleCardLayout);
        modulePanel.setBorder(null);
        modulePanel.add(welcomePage, WELCOME_PANEL_KEY);
        showWelcomePage();
    }

    private void initModuleChooser() {
        moduleChooser.setCellRenderer(new ModuleChooserCellRenderer());
        moduleChooser.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moduleChooser.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int index = moduleChooser.locationToIndex(e.getPoint());

                if (index < 0 || !moduleChooser.getCellBounds(index, index).contains(e.getPoint())) {
                    return;
                }
                final Module module = (Module) moduleChooser.getModel().getElementAt(index);
                loadModuleComponent(module.getClass());
                loadStatusPanel(module.getContextManager().getStatusPanel());
                module.getContextManager().getMainComponent().requestFocus();
            }
        });
        moduleChooser.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                int index = moduleChooser.locationToIndex(e.getPoint());

                moduleChooser.setSelectedIndex(index);
            }
        });
    }

    public void loadModuleComponent(Class<? extends Module> moduleClass) {
        moduleCardLayout.show(modulePanel, moduleClass.getName());
        currentModuleClass = moduleClass;
        leftSlidingPanel.setState(SliderPanel.STATE_HIDDEN);
    }

    public void addModule(Module module) {
        modulePanel.add(module.getContextManager().getMainComponent(), module.getClass().getName());
        createModuleButton(module);
    }

    private void createModuleButton(Module module) {
//        ((DefaultListModel) moduleChooser.getModel()).addElement(module);
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.weightx = c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        moduleChooserPanel.add(new ModuleChooserButton(module), c);
    }

    public void loadStatusPanel(Component moduleStatusPanel) {
        statusPanel.removeAll();
        if (moduleStatusPanel != null) {
            statusPanel.add(moduleStatusPanel, BorderLayout.CENTER);
        }
        statusPanel.revalidate();
        statusPanel.repaint();
    }

    public void showWelcomePage() {
        moduleCardLayout.show(modulePanel, WELCOME_PANEL_KEY);
        currentModuleClass = null;
    }

    private void initRightPanel() {
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(200, 100));
        rightPanel.setBorder(new LineBorder(ColorList.BORDER));
        rightPanel.setVisible(false);
    }

    public Class<? extends Module> getCurrentModuleClass() {
        return currentModuleClass;
    }

    private class ModuleChooserPanel extends JPanel {

        public ModuleChooserPanel() {
            super(new GridBagLayout(), true);
        }

        @Override
        protected void processMouseEvent(MouseEvent e) {
            super.processMouseEvent(e);
        }
    }

    private class ModuleChooserButton extends JPanel implements ActionListener, MouseListener {

        private Module module;
        private JButton button;
        private JLabel text;
        private boolean selected;

        public ModuleChooserButton(Module module) {
            this.module = module;
            init();
        }

        private void init() {
            final ModuleDescriptor moduleDescriptor = module.getModuleDescriptor();
            button = new JButton("<html><b>" + moduleDescriptor.getDescription() + "</b><br/><font size=\"2\">(ctrl + " + (modulePanel.getComponentCount() - 1) + ")</font></html>",
                    moduleDescriptor.getIcon());
            button.addActionListener(this);
            button.addMouseListener(this);
            button.setFocusable(false);
            button.setBackground(new Color(226, 236, 255));
            button.setForeground(new Color(0x00, 0x33, 0x66));
            button.setFont(button.getFont().deriveFont(20f));
            button.setHorizontalAlignment(SwingConstants.LEFT);

            text = new JLabel(module.getModuleDescriptor().getDescription()) {
                @Override
                protected void paintComponent(Graphics g) {
                    AffineTransform transform = ((Graphics2D) g).getTransform();

                    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setColor(module.getClass() == currentModuleClass ? ColorList.SPLASH_FOREGROUND : getBackground());
                    g.fillRect(0, 0, getWidth(), getHeight());
                    ((Graphics2D) g).rotate(Math.PI / 2);
                    g.setColor(getForeground());

                    float fontSize = 15f;
                    while (fontSize > 0) {
                        g.setFont(g.getFont().deriveFont(fontSize));
                        if (g.getFontMetrics().stringWidth(getText()) < getHeight()) {
                            break;
                        }
                        fontSize--;
                    }
                    FontMetrics metrics = g.getFontMetrics();
                    g.drawString(getText(), getHeight() / 2 - metrics.stringWidth(getText()) / 2, -(getWidth() / 2 - metrics.getHeight() / 2) - 2);
                    ((Graphics2D) g).setTransform(transform);
                }

                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(25, button.getHeight());
                }
            };
            text.addMouseListener(this);
            text.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    loadModuleComponent(module.getClass());
                    loadStatusPanel(module.getContextManager().getStatusPanel());
                    module.getContextManager().getMainComponent().requestFocus();
                }
            });
            text.setFocusable(false);
            text.setOpaque(true);
            text.setBackground(new Color(226, 236, 255));
            text.setForeground(new Color(0x00, 0x33, 0x66));
            text.setFont(new Font("Courier", Font.BOLD, 13));
            text.setHorizontalAlignment(SwingConstants.LEFT);

            setLayout(new BorderLayout());
            add(button, BorderLayout.CENTER);
            add(text, BorderLayout.EAST);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            loadModuleComponent(module.getClass());
            loadStatusPanel(module.getContextManager().getStatusPanel());
            module.getContextManager().getMainComponent().requestFocus();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBackground(ColorList.SPLASH_FOREGROUND);
            button.setForeground(Color.BLACK);
            text.setBackground(ColorList.SPLASH_FOREGROUND);
            text.setForeground(Color.BLACK);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(new Color(226, 236, 255));
            button.setForeground(new Color(0x00, 0x33, 0x66));
            text.setBackground(new Color(226, 236, 255));
            text.setForeground(new Color(0x00, 0x33, 0x66));
        }
    }

    private class ModuleChooserCellRenderer extends DefaultListCellRenderer {

        private JButton button;

        @Override
        public Component getListCellRendererComponent(JList list, final Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (!(value instanceof Module)) {
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
            if (button == null) {
                initButton();
            }
            final ModuleDescriptor moduleDescriptor = ((Module) value).getModuleDescriptor();
            button.setText("<html><b>" + moduleDescriptor.getDescription() + "</b><br/>(CTRL + " + (index + 1) + ")<html>");
            button.setIcon(moduleDescriptor.getIcon());
            button.setBackground(isSelected
                    ? /*new Color(75, 104, 184)*/ ColorList.SPLASH_FOREGROUND
                    : new Color(226, 236, 255));
            button.setForeground(isSelected
                    ? /*new Color(226, 225, 204)*/ Color.BLACK
                    : new Color(0x00, 0x33, 0x66));

            return button;
        }

        protected void initButton() {
            button = new JButton() {

                @Override
                public void paint(Graphics g) {
                    super.paint(g);
                    leftSlidingPanel.repaint();
                }
            };
            button.setBackground(new Color(226, 236, 255));
            button.setForeground(new Color(0x00, 0x33, 0x66));
            button.setFont(button.getFont().deriveFont(20f));
            button.setHorizontalAlignment(SwingConstants.LEFT);
        }
    }
}