package de.htwg.se.rummikub.aview.gui

import scala.swing._
import scala.swing.event._

import java.awt.Font
import java.awt.Dimension

import de.htwg.se.rummikub.model.tokenComponent.{TokenInterface, Color}
import de.htwg.se.rummikub.controller.controllerComponent.{ControllerInterface, UpdateEvent}
import de.htwg.se.rummikub.model.tokenComponent.tokenBaseImpl.{NumToken, Joker}

case class TokenPanel(token: TokenInterface, controller: ControllerInterface, dropZones: List[DropZone], selectionManager: SelectionManager) extends BorderPanel {
  preferredSize = new Dimension(40, 60)
  background = new java.awt.Color(245, 245, 220)
  opaque = true
  border = Swing.LineBorder(java.awt.Color.BLACK)

  var selected: Boolean = false
  var originalLocation: Option[Point] = None
  private var dragStartPoint: Option[Point] = None
  private var dragOffsets: Map[TokenPanel, (Int, Int)] = Map()

  def updateBorder(): Unit = {
    border = if (selected) Swing.LineBorder(java.awt.Color.BLUE, 3)
             else Swing.LineBorder(java.awt.Color.BLACK)
  }

  def select: Unit = {
    if (!selected) {
      selected = true
      selectionManager.addToken(this)
      updateBorder()
    }
  }

  def toggleSelection: Unit = {
    if (selected) {
      selected = false
      selectionManager.removeToken(this)
    } else {
      selected = true
      selectionManager.addToken(this)
    }
    updateBorder()
  }

  val color = token match {
    case NumToken(_, c) => c match {
      case Color.RED => java.awt.Color.RED
      case Color.BLUE => java.awt.Color.BLUE
      case Color.GREEN => new java.awt.Color(0, 153, 0)
      case Color.BLACK => java.awt.Color.BLACK
    }
    case Joker(c) => c match {
      case Color.RED => java.awt.Color.RED
      case Color.BLACK => java.awt.Color.BLACK
      case _ => java.awt.Color.YELLOW
    }
  }

  val label = new Label(token match {
    case NumToken(value, _) => value.toString
    case Joker(_) => "J"
  }) {
    foreground = color
    font = new Font("Arial", Font.BOLD, 16)
    horizontalAlignment = Alignment.Center
    verticalAlignment = Alignment.Center
  }

  layout(label) = BorderPanel.Position.Center

  listenTo(mouse.clicks, mouse.moves, keys)

  reactions += {
    case e: MouseClicked =>
    val isCtrlDown = (e.modifiers & Key.Modifier.Control) != 0
    val isMetaDown = (e.modifiers & Key.Modifier.Meta) != 0

    if (isCtrlDown || isMetaDown) {
      toggleSelection
    } else {
      selectionManager.clearSelection()
      select
    }

    case e: MousePressed =>
      dragStartPoint = Some(e.point)
      originalLocation = Some(peer.getLocation)
      peer.getParent.setComponentZOrder(peer, 0)
      selectionManager.setActiveDragSource(this)

      val tokensToDrag = if (selected) selectionManager.getSelectedTokens else List(this)
      val safeTokensToDrag = tokensToDrag.filter(tp => tp.peer.isShowing)

      if (safeTokensToDrag.nonEmpty) {
        val thisScreenPos = peer.getLocationOnScreen
        val mouseScreenPos = new Point(thisScreenPos.x + e.point.x, thisScreenPos.y + e.point.y)

        dragOffsets = safeTokensToDrag.map { tp =>
          val tpScreen = tp.peer.getLocationOnScreen
          val offset = (tpScreen.x - mouseScreenPos.x, tpScreen.y - mouseScreenPos.y)
          tp -> offset
        }.toMap
      }


    case e: MouseDragged =>
      if (selected) {
        selectionManager.setActiveDragSource(this)
        val dragTokens = selectionManager.getSelectedTokens.map(_.token)
        val tokensToDrag = if (selected) selectionManager.getSelectedTokens else List(this)
        dragStartPoint.foreach { start =>
          val dx = e.point.x - start.x
          val dy = e.point.y - start.y

          tokensToDrag.foreach { tokenPanel =>
            val oldLoc = tokenPanel.peer.getLocation
            tokenPanel.peer.setLocation(oldLoc.x + dx, oldLoc.y + dy)
          }
        }
      }

    case e: MouseReleased =>
      val tokensToDrop = if (selected) selectionManager.getSelectedTokens else List(this)

      tokensToDrop.foreach { token =>
        val screenLoc = token.peer.getLocationOnScreen

        dropZones.find { zone =>
          val zoneBounds = zone.peer.getBounds
          val zoneLoc = zone.peer.getLocationOnScreen
          val rect = new java.awt.Rectangle(zoneLoc.x, zoneLoc.y, zoneBounds.width, zoneBounds.height)

          rect.contains(screenLoc)
        } match {
          case Some(dropZone) =>
            println(s"Dropped token on ${if (dropZone.getIsRow) "Row" else "Group"} drop zone")
            dropZone.handleDrop(tokensToDrop.map(_.token))

          case None =>
            token.peer.setLocation(originalLocation.getOrElse(token.peer.getLocation))
        }
      }

      dragStartPoint = None
      dragOffsets = Map.empty
      selectionManager.setActiveDragSource(null)
  }

  private def tokenToString(token: TokenInterface): String = token match {
    case NumToken(value, color) => s"$value:${colorToString(color)}"
    case Joker(color) => s"J:${colorToString(color)}"
  }

  private def colorToString(color: Color): String = color match {
    case Color.RED => "red"
    case Color.BLUE => "blue"
    case Color.GREEN => "green"
    case Color.BLACK => "black"
  }
}