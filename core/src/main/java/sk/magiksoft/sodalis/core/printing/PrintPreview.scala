package sk.magiksoft.sodalis.core.printing


import sk.magiksoft.sodalis.core.SodalisApplication
import sk.magiksoft.sodalis.icon.IconManager

import swing._
import java.lang.Math._
import java.awt.geom.AffineTransform
import sk.magiksoft.sodalis.core.locale.LocaleManager
import sk.magiksoft.sodalis.core.utils.Conversions._
import javax.swing.{JFrame, JToolBar}
import java.awt.{BorderLayout, Graphics2D}
import java.awt.print.{PrinterJob, Pageable}

/**
 * @author wladimiiir
 * @since 2011/2/11
 */
class PrintPreview(pageable: Pageable) extends JFrame {
  private val zoomSteps = Array(0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0, 2.5, 4.0, 8.0)
  private var zoomIndex = 2
  private var currentPage = 0

  initComponents()

  private def initComponents() = {
    val previewPanel = new PreviewPanel

    setLayout(new BorderLayout)
    add(createToolBar, BorderLayout.NORTH)
    add(new ScrollPane(previewPanel) {
      horizontalScrollBar.unitIncrement = 20
      verticalScrollBar.unitIncrement = 20
    }, BorderLayout.CENTER)
    setTitle(LocaleManager.getString("preview"))
    setSize(800, 600)
    setLocationRelativeTo(SodalisApplication.get().getMainFrame)
    refreshPreview()

    def createToolBar = new JToolBar {
      add(new Button(Action(null)({
        val job = PrinterJob.getPrinterJob
        job.setPageable(pageable)
        job.printDialog
      })) {
        icon = IconManager.getInstance.getIcon("printBW")
      })
      addSeparator()
      val firstPageButton = new Button(Action(null)({
        currentPage = 0
        reloadButtonEnability()
        refreshPreview()
      })) {
        icon = IconManager.getInstance.getIcon("firstPage")
      }
      val previousPageButton = new Button(Action(null)({
        currentPage -= 1
        reloadButtonEnability()
        refreshPreview()
      })) {
        icon = IconManager.getInstance.getIcon("previousPage")
      }
      val nextPageButton = new Button(Action(null)({
        currentPage += 1
        reloadButtonEnability()
        refreshPreview()
      })) {
        icon = IconManager.getInstance.getIcon("nextPage")
      }
      val lastPageButton = new Button(Action(null)({
        currentPage = pageable.getNumberOfPages - 1
        reloadButtonEnability()
        refreshPreview()
      })) {
        icon = IconManager.getInstance.getIcon("lastPage")
      }
      add(firstPageButton)
      add(previousPageButton)
      add(nextPageButton)
      add(lastPageButton)

      reloadButtonEnability()

      def reloadButtonEnability() {
        firstPageButton.enabled = currentPage > 0
        previousPageButton.enabled = currentPage > 0
        nextPageButton.enabled = currentPage < pageable.getNumberOfPages - 1
        lastPageButton.enabled = currentPage < pageable.getNumberOfPages - 1
      }

      addSeparator()

      val zoomOutButton = new Button(Action(null)({
        zoomIndex = max(0, zoomIndex - 1)
        refreshPreview()
      })) {
        icon = IconManager.getInstance.getIcon("zoomOut2")
      }
      val zoomInButton = new Button(Action(null)({
        zoomIndex = min(zoomSteps.size - 1, zoomIndex + 1)
        refreshPreview()
      })) {
        icon = IconManager.getInstance.getIcon("zoomIn2")
      }

      add(zoomOutButton)
      add(zoomInButton)
    }

    def refreshPreview() = {
      previewPanel.preferredSize = new Dimension(pageable.getPageFormat(currentPage).getWidth * zoom + 40, pageable.getPageFormat(currentPage).getHeight * zoom + 40)
      previewPanel.revalidate()
      previewPanel.repaint()
    }
  }

  def zoom = zoomSteps(zoomIndex)

  class PreviewPanel extends Component {

    override def paint(g: Graphics2D) = {
      val transform = g.getTransform.clone.asInstanceOf[AffineTransform]
      val pageFormat = pageable.getPageFormat(currentPage)

      g.translate((size.getWidth - pageFormat.getWidth * zoom) / 2d, (size.getHeight - pageFormat.getHeight * zoom) / 2d)
      g.transform(AffineTransform.getScaleInstance(zoom, zoom))
      pageable.getPrintable(currentPage).print(g, pageFormat, currentPage)
      g.setTransform(transform)
    }
  }

}
