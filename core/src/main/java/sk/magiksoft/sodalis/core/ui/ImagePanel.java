
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

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.utils.ImageUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EventListener;

/**
 * @author wladimiiir
 */
public class ImagePanel extends JPanel {
    private static final int MAX_PIXELS = 3400000;
    private BufferedImage image = null;
    private String infoText = LocaleManager.getString("noImageInfo");
    private JPopupMenu popupMenu = new JPopupMenu();
    private boolean editable = true;
    private JFileChooser fileChooser;
    private JPanel imagePanel;
    private Window owner;

    public ImagePanel() {
        this(null);
    }

    public ImagePanel(Window owner) {
        this.owner = owner;
        initComponents();
    }

    private void initComponents() {
        imagePanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                if (image == null) {
                    super.paint(g);
                } else {
                    double ratio = (double) image.getWidth(null) / image.getHeight(null);
                    double panelRatio = (double) getWidth() / getHeight();

                    g.setColor(getBackground());
                    g.fillRect(0, 0, getWidth(), getHeight());
                    if (panelRatio > ratio) {
                        g.drawImage(image, (int) ((getWidth() - (ratio * getHeight())) / 2), 0, (int) (ratio * getHeight()), getHeight(), null);
                    } else {
                        g.drawImage(image, 0, (int) ((getHeight() - (this.getWidth() / ratio)) / 2), this.getWidth(), (int) (this.getWidth() / ratio), null);
                    }
                }
            }

        };

        imagePanel.setLayout(new GridBagLayout());
        imagePanel.add(new JLabel(getNoImageInfoText()));

        setLayout(new BorderLayout());
        add(imagePanel, BorderLayout.CENTER);
        JMenuItem menuItem = new JMenuItem(LocaleManager.getString("changePicture"));
        popupMenu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                loadImage();
            }
        });
        menuItem = new JMenuItem(LocaleManager.getString("removePicture"));
        popupMenu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setImage(null);
            }
        });
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!editable) {
                    return;
                }
                if (image != null) {
                    if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                        showImage();
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        popupMenu.show(ImagePanel.this, e.getX() - 10, e.getY() - 5);
                    }
                } else {
                    loadImage();
                }
            }
        });
    }

    private void showImage() {
        final JDialog frame = new JDialog(owner == null ? SwingUtilities.getWindowAncestor(this) : owner);
        final int width = image.getWidth();
        final int height = image.getHeight();
        final double zoom = Math.max(Math.max(1, width / Toolkit.getDefaultToolkit().getScreenSize().getWidth()),
                Math.max(1, height / (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 50)));

        frame.setContentPane(new JPanel() {
            @Override
            public void paint(Graphics g) {
                g.drawImage(image, 0, 0, (int) (width / zoom), (int) (height / zoom), null);
            }
        });
        frame.getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setVisible(false);
            }
        });
        frame.setSize((int) (width / zoom), (int) (height / zoom));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void initFileChooser() {
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(LocaleManager.getString("pictures"), "jpg", "png", "bmp", "gif"));
    }

    private void loadImage() {
        if (fileChooser == null) {
            initFileChooser();
        }

        if (fileChooser.showOpenDialog(this) == JFileChooser.CANCEL_OPTION || fileChooser.getSelectedFile() == null) {
            return;
        }

        try {
            final File file = fileChooser.getSelectedFile();
            BufferedImage image = ImageIO.read(file);
            if (image.getHeight() * image.getWidth() > MAX_PIXELS) {
                if (ISOptionPane.showConfirmDialog(this, LocaleManager.getString("imageTooBigResize"), LocaleManager.getString("information"),
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    final int pixels = image.getWidth() * image.getHeight();
                    image = ImageUtils.resizeImage(image, (double) MAX_PIXELS / pixels);
                }
            }
            setImage(image);
        } catch (IOException ex) {
            LoggerManager.getInstance().error(ImagePanel.class, ex);
        }
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        fireImageChanged();
        revalidate();
        repaint();
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getNoImageInfoText() {
        return infoText;
    }

    public void setNoImageInfoText(String infoText) {
        this.infoText = infoText;
    }

    private void fireImageChanged() {
        for (ImagePanelListener imagePanelListener : listenerList.getListeners(ImagePanelListener.class)) {
            imagePanelListener.imageChanged();
        }
    }

    public void addImagePanelListener(ImagePanelListener listener) {
        if (listener != null) {
            listenerList.add(ImagePanelListener.class, listener);
        }
    }

    public void removeImagePanelListener(ImagePanelListener listener) {
        listenerList.remove(ImagePanelListener.class, listener);
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public static interface ImagePanelListener extends EventListener {

        public void imageChanged();
    }
}