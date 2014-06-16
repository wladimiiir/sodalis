
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.entity;

import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author wladimiiir
 */
public class ImageEntity extends AbstractDatabaseEntity {
    private static final long serialVersionUID = -1L;

    private Image image = null;
    private byte[] bytes = null;
    private static final int MAX_PIXELS = 5000000;

    public ImageEntity() {
    }

    public ImageEntity(Image image) {
        this.image = image;
    }

    public byte[] getBytes() {
        if (bytes == null) {
            initBytes();
        }
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Image getImage() {
        if (image == null) {
            initImage();
        }
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
        bytes = null;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof ImageEntity)) {
            return;
        }

        ImageEntity ie = (ImageEntity) entity;
        this.image = ie.image;
        this.bytes = ie.bytes;
    }

    private void initBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            if (image == null) {
                bytes = new byte[0];
            } else {
                final int pixels = image.getWidth(null) * image.getHeight(null);
                if (pixels > MAX_PIXELS) {
                    image = ImageUtils.resizeImage(image, (double) MAX_PIXELS / pixels);
                }

                ImageIO.write((RenderedImage) image, "png", baos);
                bytes = baos.toByteArray();
            }
        } catch (IOException ex) {
            LoggerManager.getInstance().error(ImageEntity.class, ex);
        }

    }

    private void initImage() {
        try {
            image = ImageIO.read(new ByteArrayInputStream(getBytes()));
        } catch (IOException ex) {
            LoggerManager.getInstance().error(ImageEntity.class, ex);
        }
    }
}