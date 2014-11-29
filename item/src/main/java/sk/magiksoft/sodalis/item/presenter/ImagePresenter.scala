package sk.magiksoft.sodalis.item.presenter

import sk.magiksoft.sodalis.item.entity.ItemProperty
import java.io.Serializable
import javax.swing.BorderFactory
import java.awt.image.BufferedImage
import java.awt.Dimension
import sk.magiksoft.sodalis.core.ui.ImagePanel
import sk.magiksoft.sodalis.core.entity.ImageEntity
import scala.swing.{Swing, Component}
import sk.magiksoft.sodalis.core.ui.ImagePanel.ImagePanelListener

/**
 * @author wladimiiir
 * @since 2010/6/20
 */

class ImagePresenter extends Presenter {
  def getValue(component: Component) = new ImageEntity(component.peer.asInstanceOf[ImagePanel].getImage)

  def getReadableValue(value: Serializable) = null

  def getComponent(itemProperty: ItemProperty, value: Serializable) = {
    val imagePanel: ImagePanel = new ImagePanel
    imagePanel.setBorder(BorderFactory.createTitledBorder(itemProperty.name))
    imagePanel.setPreferredSize(new Dimension(200, 200))
    Component.wrap(imagePanel)
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
