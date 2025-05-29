package de.htwg.se.rummikub.aview.gui

import scala.swing._
import java.awt.{Graphics2D, Image}
import javax.imageio.ImageIO
import scala.util.{Try, Success, Failure}

class HorizontalImagePanel(imagePath: String) extends FlowPanel {
  val backgroundImage: Option[Image] = Try {
    ImageIO.read(getClass.getResource(imagePath))
  } match {
    case Success(img) => Some(img)
    case Failure(ex) =>
      println(s"Could not load image at $imagePath: ${ex.getMessage}")
      None
  }

  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)
    backgroundImage.foreach { img =>
      g.drawImage(img, 0, 0, size.width, size.height, null)
    }
  }
}