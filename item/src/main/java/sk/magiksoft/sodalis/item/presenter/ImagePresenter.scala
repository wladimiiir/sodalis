
/***********************************************\
*  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
*  Sodalis 2007-2011                            *
*  http://www.sodalis.sk                        *
\***********************************************/
    
     
package sk.magiksoft.sodalis.item.presenter

import sk.magiksoft.sodalis.item.entity.ItemProperty
import java.io.Serializable
import sk.magiksoft.sodalis.core.ui.ImagePanel
import swing.Component
import javax.swing.BorderFactory
import sk.magiksoft.sodalis.core.entity.ImageEntity
import java.awt.image.BufferedImage
import java.awt.Dimension
import sk.magiksoft.sodalis.core.ui.ImagePanel.ImagePanelListener
import sk.magiksoft.sodalis.core.utils.Conversions._

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: Jun 20, 2010
 * Time: 9:01:12 AM
 * To change this template use File | Settings | File Templates.
 */

class ImagePresenter extends Presenter {
  def getValue(component: Component) = new ImageEntity(component.peer.asInstanceOf[ImagePanel].getImage)

  def getReadableValue(value: Serializable) = null

  def getComponent(itemProperty: ItemProperty, value: Serializable) = {
    val imagePanel: ImagePanel = new ImagePanel
    imagePanel.setBorder(BorderFactory.createTitledBorder(itemProperty.name))
    imagePanel.setPreferredSize(new Dimension(200, 200))
    imagePanel
  }

  def getEditorActions(itemProperty: ItemProperty, reloadAction: => Unit) = Nil

  def setValue(c: Component, value: Serializable) = c.peer.asInstanceOf[ImagePanel].setImage(value match {
    case ie: ImageEntity => ie.getImage.asInstanceOf[BufferedImage]
    case _ => null
  })

  def addName = false

  def addChangeListener(component: Component, listener: () => Unit) = {
    component.peer.asInstanceOf[ImagePanel].addImagePanelListener(new ImagePanelListener {
      def imageChanged = listener()
    })
  }
}